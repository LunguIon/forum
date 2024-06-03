package md.forum.forum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimplifiedTopicDTO {
    private String email;
    private String title;
    private String content;
    private String imageURL;
}

