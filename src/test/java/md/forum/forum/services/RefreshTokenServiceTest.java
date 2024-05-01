package md.forum.forum.services;

import md.forum.forum.exceptions.RefreshTokenExpiredException;
import md.forum.forum.models.User;
import md.forum.forum.repository.UserRepository;
import md.forum.forum.security.model.RefreshToken;
import md.forum.forum.security.repository.RefreshTokenRepository;
import md.forum.forum.security.service.RefreshTokenService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest implements WithAssertions {
    public static final String USERNAME = "username";
    public static final String TOKEN = "this is token";
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    User user;
    @Mock
    RefreshToken.RefreshTokenBuilder refreshTokenBuilder;
    @Mock
    RefreshToken refreshToken;
    @InjectMocks
    RefreshTokenService refreshTokenService;

    @Test
    void testCreateRefreshToken() {
        when(userRepository.findByEmail(USERNAME)).thenReturn(Optional.of(user));

        when(refreshTokenBuilder.user(user)).thenReturn(refreshTokenBuilder);
        when(refreshTokenBuilder.expiryDate(any(Instant.class))).thenReturn(refreshTokenBuilder);
        when(refreshTokenBuilder.token(anyString())).thenReturn(refreshTokenBuilder);
        when(refreshTokenBuilder.build()).thenReturn(refreshToken);
        when(refreshTokenRepository.save(refreshToken)).thenReturn(refreshToken);
        try(MockedStatic<RefreshToken> staticMock = mockStatic(RefreshToken.class)) {
            staticMock.when(RefreshToken::builder).thenReturn(refreshTokenBuilder);

            assertThat(refreshTokenService.createRefreshToken(USERNAME))
                    .isNotNull()
                    .isEqualTo(refreshToken);

            staticMock.verify(RefreshToken::builder);
        }

        verify(userRepository).findByEmail(USERNAME);
        verify(refreshTokenRepository).save(refreshToken);
    }

    @Test
    void testCreateRefreshToken_UsernameNull() {
        when(userRepository.findByEmail(USERNAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenService.createRefreshToken(USERNAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found for username : " + USERNAME);

        verify(userRepository).findByEmail(USERNAME);

        verifyNoInteractions(refreshTokenBuilder);
        verifyNoInteractions(refreshTokenRepository);
    }


    @Test
    void testFindByToken() {
        when(refreshTokenRepository.findByToken(TOKEN)).thenReturn(Optional.of(refreshToken));

        assertThat(refreshTokenService.findByToken(TOKEN))
                .isNotNull()
                .isEqualTo(Optional.of(refreshToken));

        verify(refreshTokenRepository).findByToken(TOKEN);
    }

    @Test
    void testFindByToken_UsernameNull() {
        when(refreshTokenRepository.findByToken(TOKEN)).thenReturn(Optional.empty());

        assertThat(refreshTokenService.findByToken(TOKEN))
                .isEqualTo(Optional.empty());

        verify(refreshTokenRepository).findByToken(TOKEN);
    }

    @Test
    void testVerifyExpiration(){
        when(refreshToken.getExpiryDate()).thenReturn(Instant.now().plusSeconds(3600));

        assertThat(refreshTokenService.verifyExpiration(refreshToken))
                .isEqualTo(refreshToken);

        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    void testVerifyExpiration_TokenExpired() {
        when(refreshToken.getExpiryDate()).thenReturn(Instant.now().minusSeconds(3600));
        when(refreshToken.getToken()).thenReturn(TOKEN);

        assertThrows(RefreshTokenExpiredException.class, () -> refreshTokenService.verifyExpiration(refreshToken));

        verify(refreshTokenRepository).delete(refreshToken);
    }
}
