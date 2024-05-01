package md.forum.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2UserInfoDto {
    private String id;
    private String name;
    private String email;
    private String picture;
}
