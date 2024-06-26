package md.forum.forum.services;



import md.forum.forum.dto.get.GetPostDTO;
import md.forum.forum.dto.mappers.PostDTOMapper;
import md.forum.forum.dto.simplified.SimplifiedPostDTO;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import md.forum.forum.repository.PostRepository;
import md.forum.forum.s3.S3Buckets;
import md.forum.forum.s3.S3Service;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final TopicService topicService;
    private final PostDTOMapper postDTOMapper;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public PostService(PostRepository postRepository, UserService userService, TopicService topicService, PostDTOMapper postDTOMapper, S3Service s3Service, S3Buckets s3Buckets) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.topicService = topicService;
        this.postDTOMapper = postDTOMapper;
        this.s3Service = s3Service;
        this.s3Buckets = s3Buckets;
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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

    public void uploadPostImage(String postId, MultipartFile file) {
        if(!postRepository.existsByPostId(postId)) {
            try {
                throw new ChangeSetPersister.NotFoundException();
            } catch (ChangeSetPersister.NotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            String postImageId = UUID.randomUUID().toString();
            try {
                s3Service.putObject(
                        s3Buckets.getPosts(),
                        "postImage/%s/%s".formatted(postId, postImageId),
                        file.getBytes()
                );
                postRepository.updateImageByPostId(postId, postImageId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public byte[] getPostImage(String postId) {
        Post post = postRepository.findPostByPostId(postId).orElseThrow();
        byte[] profileImage = null;
        if(post != null) {
            if(post.getPostId() != null) {
                if(!post.getPostId().contains("https")){
                    String profileImageId = post.getPostId();
                    profileImage = s3Service.getObject(
                            s3Buckets.getPosts(),
                            "postImage/%s/%s".formatted(postId,profileImageId)
                    );
                }

            }
        }
        return profileImage;
    }




}
