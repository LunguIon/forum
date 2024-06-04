package md.forum.forum.services;



import md.forum.forum.dto.get.GetPostDTO;
import md.forum.forum.dto.mappers.PostDTOMapper;
import md.forum.forum.dto.simplified.SimplifiedPostDTO;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import md.forum.forum.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final TopicService topicService;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final PostDTOMapper postDTOMapper;

    public PostService(PostRepository postRepository, UserService userService, TopicService topicService, PostDTOMapper postDTOMapper) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.topicService = topicService;
        this.postDTOMapper = postDTOMapper;
    }


    public Post createPost(Post post) {
        return postRepository.save(post);
    }
    public Post createPost(SimplifiedPostDTO simplifiedPostDTO) {
        LocalDate now = LocalDate.now();
        Post post = new Post();
        post.setPostId();
        post.setTitle(simplifiedPostDTO.getTitle());
        post.setContent(simplifiedPostDTO.getContent());
        post.setImageURL(simplifiedPostDTO.getImageURL());
        post.setTopic(topicService.findTopicByTitleFull(simplifiedPostDTO.getTopicTitle()).orElse(null));
        post.setCreateDate(Date.valueOf(dtf.format(now)));
        User user = userService.getUserByEmailFull(simplifiedPostDTO.getEmail()).orElseThrow();
        post.setUser(user);
        return postRepository.save(post);

    }

    public List<GetPostDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postDTOMapper)
                .collect(Collectors.toList());
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }
    public Optional<GetPostDTO> getPostByPostId(String postId) {
        return postRepository.findPostByPostId(postId)
                .map(postDTOMapper);
    }
    public Optional<Post> getPostByPostIdFull(String postId) {
        return postRepository.findPostByPostId(postId);
    }


    public List<GetPostDTO> getPostsByUserName(String email) {
        return postRepository.findAllByUser(userService.getUserByEmailFull(email).orElseThrow())
                .stream()
                .map(postDTOMapper)
                .collect(Collectors.toList());
    }

    public List<GetPostDTO> getPostsByTopic(String topicTitle) {
        return postRepository.findAllByTopic(topicService.findTopicByTitleFull(topicTitle).orElseThrow())
                .stream()
                .map(postDTOMapper)
                .collect(Collectors.toList());
    }
    //Don't
    public Post updatePost(Long id, Post newPost) {
        LocalDate now = LocalDate.now();
        return postRepository.findById(id).map(post -> {
            post.setTitle(newPost.getTitle());
            post.setContent(newPost.getContent());
            post.setUpdateDate(Date.valueOf(dtf.format(now)));
            post.setImageURL(newPost.getImageURL());
            post.setTopic(newPost.getTopic());
            return postRepository.save(post);
        }).orElse(null);
    }

    // Use this
    public Post updatePostByPostId(String id, SimplifiedPostDTO newPost) {
        LocalDate now = LocalDate.now();
        return postRepository.findPostByPostId(id).map(post -> {
            post.setTitle(newPost.getTitle());
            post.setContent(newPost.getContent());
            post.setUpdateDate(Date.valueOf(dtf.format(now)));
            post.setImageURL(newPost.getImageURL());
            post.setTopic(topicService.findTopicByTitleFull(newPost.getTopicTitle()).orElse(null));
            return postRepository.save(post);
        }).orElse(null);
    }
    //Don't
    public boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }
    //Use this
    public boolean deletePostById(String id) {
        if (postRepository.existsByPostId(id)) {
            postRepository.deleteByPostId(id);
            return true;
        }
        return false;
    }




}
