package md.forum.forum.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest implements WithAssertions {
    public static final String TEST_USER = "testUser";
    public static final String WRONG_USER = "wrongUser";
    public static final String ROLE = "role";
    public static final String ADMIN = "admin";
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

        when(userDetails.getUsername()).thenReturn(TEST_USER);
    }

    @Test
    void testIsTokenValid_withValidToken() {
        assertThat(jwtService.isTokenValid(jwtService.generateToken(userDetails), userDetails)).isTrue();
    }

    @Test
    void testIsTokenValid_withInvalidUsername() {
        String token = jwtService.generateToken(userDetails);
        when(userDetails.getUsername()).thenReturn(WRONG_USER);

        assertThat(jwtService.isTokenValid(token, userDetails)).isFalse();
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);

        assertThat(jwtService.extractUsername(token)).isEqualTo(TEST_USER);
    }

    @Test
    void testExtractClaim() {
        String token = jwtService.generateToken(userDetails);
        assertThat(jwtService.extractClaim(token, Claims::getIssuedAt)).isNotNull();
    }

    @Test
    void testGenerateTokenWithClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLE, ADMIN);

        String token = jwtService.generateToken(claims, userDetails);

        assertThat(ADMIN).isEqualTo(jwtService.extractClaim(token, c -> c.get(ROLE, String.class)));
        assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    void testEmptyClaims() {
        String token = jwtService.generateToken(new HashMap<>(), userDetails);

        assertThat(jwtService.generateToken(new HashMap<>(), userDetails)).isNotNull();
        assertThat(jwtService.isTokenValid(token,userDetails)).isTrue();
    }
}

