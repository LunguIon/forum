package md.forum.forum.controllers;

import md.forum.forum.models.Post;
import md.forum.forum.services.PostService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private static final Logger logger = LogManager.getLogger(PostController.class);
    private final PostService postService;

    public PostController(PostService postService) {
        logger.info("PostController constructor was initialized");
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        logger.info("getAllPosts was called");
        List<Post> posts = postService.getAllPosts();
        logger.info("getAllPosts returned {} posts", posts.size());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Post>> getAllPostsByUser(@PathVariable String email) {
        logger.info("getAllPostsByUser was called");
        List<Post> posts = postService.getPostsByUserName(email);
        logger.info("getAllPostsByUser was called with email: {}", email);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        logger.info("getPostById was called");
        return postService.getPostById(id)
                .map(post -> {
                    logger.info("getPostById was called with id: {}", id);
                    return ResponseEntity.ok(post);
                })
                .orElseGet(() -> {
                    logger.info("post with id {} was not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        logger.info("createPost was called");
        Post createdPost = postService.createPost(post);
        if (createdPost == null) {
            logger.info("Post was not created");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            logger.info("Post was created with id: {}", createdPost.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
        Post updatedPost = postService.updatePost(id, post);
        logger.info("updatePost was called with id: {}", id);
        if (updatedPost != null) {
            logger.info("Post was updated with id: {}", updatedPost.getId());
            return ResponseEntity.ok(updatedPost);
        } else {
            logger.error("updatePost was not found");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        logger.info("deletePost was called with id: {}", id);
        boolean deleted = postService.deletePost(id);
        if(deleted){
            logger.info("Post was deleted with id: {}", id);
            return ResponseEntity.noContent().build();
        }
        else {
            logger.error("Post was not found, and was not deleted");
            return ResponseEntity.notFound().build();
        }
    }
}
