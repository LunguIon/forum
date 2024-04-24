package md.forum.forum.services;

import md.forum.forum.exceptions.RefreshTokenExpiredException;
import md.forum.forum.models.RefreshToken;
import md.forum.forum.models.User;
import md.forum.forum.repository.RefreshTokenRepository;
import md.forum.forum.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    final RefreshTokenRepository refreshTokenRepository;

    final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String username) {
        Optional<User> userOptional = userRepository.findByEmail(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            RefreshToken refreshToken =  RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(600000))
                    .build();
            return refreshTokenRepository.save(refreshToken);
        }else{
            throw new IllegalArgumentException("User not found for username : " + username);
        }

    }


    public Optional<RefreshToken> findByToken(String username) {
        return refreshTokenRepository.findByToken(username);
    }
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenExpiredException(refreshToken.getToken());
        }
        return refreshToken;
    }

}
