package md.forum.forum.services;



import md.forum.forum.dto.SimplifiedPostDTO;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import md.forum.forum.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }


    public Post createPost(Post post) {
        return postRepository.save(post);
    }
    public Post createPost(SimplifiedPostDTO simplifiedPostDTO) {
        Post post = new Post();
        post.setTitle(simplifiedPostDTO.getTitle());
        post.setContent(simplifiedPostDTO.getContent());
        post.setImageURL(simplifiedPostDTO.getImageURL());
        User user = userService.getUserByEmail(simplifiedPostDTO.getEmail()).orElseThrow();
        post.setUser(user);
        return postRepository.save(post);

    }


    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getPostsByUserName(String email) {
        return postRepository.findAllByUser(userService.getUserByEmail(email).orElseThrow());
    }

    public Post updatePost(Long id, Post newPost) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        return postRepository.findById(id).map(post -> {
            post.setTitle(newPost.getTitle());
            post.setContent(newPost.getContent());
            post.setUpdateDate(Date.valueOf(dtf.format(now)));
            post.setImageURL(newPost.getImageURL());
            return postRepository.save(post);
        }).orElse(null);
    }

    public boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }



}
