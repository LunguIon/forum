package md.forum.forum.dto.mappers;

import md.forum.forum.dto.get.GetPostDTO;
import md.forum.forum.models.Post;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class PostDTOMapper implements Function<Post, GetPostDTO> {

    @Override
    public GetPostDTO apply(Post post) {
        return new GetPostDTO(
                post.getPostId(),
                new UserDTOMapper().apply(post.getUser()),
                post.getTitle(),
                post.getContent(),
                post.getImageURL(),
                post.getCreateDate(),
                post.getUpdateDate()
        );
    }
}
