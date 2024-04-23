package md.forum.forum.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String username;
    private String password;
    private String email;
}
