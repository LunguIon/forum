package md.forum.forum.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimplifiedPostDTO {

        private String email;
        private String title;
        private String content;
        private String imageURL;

}
