package md.forum.forum.exceptions;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException(String token) {
        super("Refresh token expired: " + token + ". Please make a new login.");
    }
}
