package md.forum.forum.security.service;

import md.forum.forum.exceptions.RefreshTokenExpiredException;
import md.forum.forum.security.model.RefreshToken;
import md.forum.forum.models.User;
import md.forum.forum.security.repository.RefreshTokenRepository;
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

    public RefreshToken createRefreshToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            if(!existsByUser(userOptional.get())) {
                User user = userOptional.get();
                RefreshToken refreshToken = RefreshToken.builder()
                        .user(user)
                        .token(UUID.randomUUID().toString())
                        .expiryDate(Instant.now().plusMillis(18000000))
                        .build();
                return refreshTokenRepository.save(refreshToken);
            }
            else {
                throw new IllegalArgumentException("Tokes was created already");
            }
        } else {
            throw new IllegalArgumentException("User not found for username : " + email);
        }

    }

    public boolean existsByUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        return refreshTokenRepository.existsByUser(userOptional.get());
    }
    public boolean existsByUser(User user) {

        return refreshTokenRepository.existsByUser(user);
    }


    public Optional<RefreshToken> findByToken(String email) {
        return refreshTokenRepository.findByToken(email);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenExpiredException(refreshToken.getToken());
        }
        return refreshToken;
    }

}
