package md.forum.forum.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import md.forum.forum.security.service.JwtService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.when;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest implements WithAssertions {
    public static final String TEST_USER = "testUser";
    public static final String WRONG_USER = "wrongUser";

    @Mock
    UserDetails userDetails;

    @InjectMocks
    JwtService jwtService;

    @BeforeEach
    void setup() {
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        String base64Secret = Base64.getEncoder().encodeToString(keyBytes);
        ReflectionTestUtils.setField(jwtService, "secretKey", base64Secret);

        long jwtExpiration = 3600000;
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", jwtExpiration);
    }

    @Test
    void testIsTokenValid_withValidToken() {
        when(userDetails.getUsername()).thenReturn(TEST_USER);
        String token = jwtService.generateToken(TEST_USER);
        assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    void testIsTokenValid_withInvalidUsername() {
        when(userDetails.getUsername()).thenReturn(TEST_USER);
        String token = jwtService.generateToken(TEST_USER);
        when(userDetails.getUsername()).thenReturn(WRONG_USER);
        assertThat(jwtService.isTokenValid(token, userDetails)).isFalse();
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(TEST_USER);
        assertThat(jwtService.extractUsername(token)).isEqualTo(TEST_USER);
    }

    @Test
    void testExtractClaim() {
        String token = jwtService.generateToken(TEST_USER);
        assertThat(jwtService.extractClaim(token, Claims::getIssuedAt)).isNotNull();
    }

    @Test
    void testIsTokenExpired() throws InterruptedException {
        long shortJwtExpiration = 1000;
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", shortJwtExpiration);
        String token = jwtService.generateToken(TEST_USER);
        Thread.sleep(1001);
        try {
            assertThat(jwtService.isTokenValid(token, userDetails)).isFalse();
        } catch (ExpiredJwtException e) {
            assertThat(e).isInstanceOf(ExpiredJwtException.class);
        }
    }

    @Test
    void testTokenValidityWithChangedSecretKey() {
        String token = jwtService.generateToken(TEST_USER);
        byte[] newKeyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        String newBase64Secret = Base64.getEncoder().encodeToString(newKeyBytes);
        ReflectionTestUtils.setField(jwtService, "secretKey", newBase64Secret);
        try {
            assertThat(jwtService.isTokenValid(token, userDetails)).isFalse();
        } catch (SignatureException e) {
            assertThat(e).isInstanceOf(SignatureException.class);
        }
    }
}
