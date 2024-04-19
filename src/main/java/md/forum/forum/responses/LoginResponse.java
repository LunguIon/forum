package md.forum.forum.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private long expiresIn;
}
