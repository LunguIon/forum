package md.forum.forum.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import md.forum.forum.dto.get.GetPostDTO;
import md.forum.forum.dto.simplified.SimplifiedPostDTO;
import md.forum.forum.models.Post;
import md.forum.forum.services.PostService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/posts")
@Tag(name = "Post controller methods")
public class PostController {
    private static final Logger logger = LogManager.getLogger(PostController.class);
    private final PostService postService;
    public PostController(PostService postService) {
        logger.info("PostController constructor was initialized");
        this.postService = postService;
    }

    @Operation(summary = "Find all posts")
    @GetMapping
    public ResponseEntity<List<GetPostDTO>> getAllPosts() {
        logger.info("getAllPosts was called");
        List<GetPostDTO> posts = postService.getAllPosts();
        logger.info("getAllPosts returned {} posts", posts.size());
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Find all posts by user's email")
    @GetMapping("/user/{email}")
    public ResponseEntity<List<GetPostDTO>> getAllPostsByUser(@PathVariable String email) {
        List<GetPostDTO> posts = postService.getPostsByUserName(email);
        logger.info("getAllPostsByUser was called with email: {}", email);
        return ResponseEntity.ok(posts);
    }
    @Operation(summary = "Find all posts by Topic")
    @GetMapping("/topic/{topic}")
    public ResponseEntity<List<GetPostDTO>> getAllPostsByTopic(@PathVariable String topic) {
        List<GetPostDTO> posts = postService.getPostsByTopic(topic);
        logger.info("getAllPostsByTopic was called with topic: {}", topic);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Find post by ID")
    @GetMapping("/{id}")
    public ResponseEntity<GetPostDTO> getPostById(@PathVariable String id) {
        logger.info("getPostById was called");
        return postService.getPostByPostId(id)
                .map(post -> {
                    logger.info("getPostById was called with id: {}", id);
                    return ResponseEntity.ok(post);
                })
                .orElseGet(() -> {
                    logger.info("post with id {} was not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Operation(summary = "Create new post")
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody SimplifiedPostDTO simplifiedPostDTO) {
        Post createdPost = postService.createPost(simplifiedPostDTO);
        if (createdPost == null) {
            logger.info("Post was not created");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            logger.info("Post was created with id: {}", createdPost.getPostId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        }
    }

    @Operation(summary = "Update post by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable String id, @RequestBody SimplifiedPostDTO simplifiedPostDTO) {
        Post updatedPost = postService.updatePostByPostId(id, simplifiedPostDTO);
        if (updatedPost != null) {
            logger.info("Post was updated with id: {}", updatedPost.getPostId());
            return ResponseEntity.ok(updatedPost);
        } else {
            logger.error("updatePost was not found");
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete post by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        boolean deleted = postService.deletePostById(id);
        if(deleted){
            logger.info("Post was deleted with id: {}", id);
            return ResponseEntity.noContent().build();
        }
        else {
            logger.error("Post was not found, and was not deleted");
            return ResponseEntity.notFound().build();
        }
    }

    //Images
    @PostMapping(
            value = "{postId}/image",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    public void uploadPostImage(@PathVariable("postId") String postId,
                                       @RequestParam("file") MultipartFile file) {
        postService.uploadPostImage(postId, file);

    }
    @PostMapping(
            value = "/image",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Post> createPostWithImage(@RequestBody SimplifiedPostDTO simplifiedPostDTO,
                                       @RequestParam("file") MultipartFile file
    ) {
        Post createdPost = postService.createPost(simplifiedPostDTO);
        if (createdPost == null) {
            logger.info("Post was not created");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            postService.uploadPostImage(createdPost.getPostId(), file);
            logger.info("Post was created with id: {}", createdPost.getPostId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        }



    }
    @GetMapping("{postId}/image")
    public byte [] uploadPostImage(@PathVariable("postId") String postId) {
        return postService.getPostImage(postId);

    }
}
