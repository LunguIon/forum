package md.forum.forum.dto.auth;

import lombok.Data;

@Data
public class LoginUserDto {
    private String email;
    private String password;
}
