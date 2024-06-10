package md.forum.forum.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import md.forum.forum.dto.simplified.SimplifiedLikeDTO;
import md.forum.forum.models.Like;
import md.forum.forum.services.LikeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/likes")
@Tag(name = "Like controller methods")
public class LikeController {
    private final LikeService likeService;
    Logger logger = LogManager.getLogger(LikeController.class);

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(summary = "Get all likes")
    @GetMapping
    public ResponseEntity<List<Like>> getAllLikes() {
        logger.info("Get all likes");
        List<Like> likes = likeService.findAll();
        if (likes.isEmpty()) {
            logger.info("No likes found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        logger.info("Found {} likes", likes.size());
        return ResponseEntity.ok(likes);
    }
    // Endpoint to find all likes by a specific user
    @Operation(summary = "Find all likes by a specific user")
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<Like>> findAllByUser(@PathVariable("userEmail") String userEmail) {
        logger.info("Find all likes by user email: {}", userEmail);
        List<Like> likes = likeService.findAllByUser(userEmail);
        logger.info("Found {} likes by user email: {}", likes.size(), userEmail);
        if (!likes.isEmpty()) {
            return ResponseEntity.ok(likes);
        } else {
            return ResponseEntity.noContent().build();
        }

    }

    // Endpoint to find all likes for a specific post, useless rn
    @Operation(summary = "Find all likes for a specific post")
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Like>> findAllByPost(@PathVariable("postId") String postId) {
        logger.info("Find all likes by post id: {}", postId);
        List<Like> likes = likeService.findAllByPost(postId);
        logger.info("Found {} likes by post id: {}", likes.size(), postId);
        if (!likes.isEmpty()) {
            return ResponseEntity.ok(likes);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    // Endpoint to find all likes for posts with no comments, useless rn
    @Operation(summary = "Find all likes for posts with no comments")
    @GetMapping("/post/withoutComments")
    public ResponseEntity<List<Like>> findAllForPostWithoutComments() {
        logger.info("Find all likes without comments");
        List<Like> likes = likeService.findAllForPost();
        if (!likes.isEmpty()) {
            return ResponseEntity.ok(likes);
        } else {
            logger.info("Likes was not found");
            return ResponseEntity.noContent().build();
        }
    }

    // Endpoint to find all likes for a specific post with no comments
    @Operation(summary = "Find all likes for a specific post")
    @GetMapping("/post/{postId}/withoutComments")
    public ResponseEntity<List<Like>> findAllForPostWithoutComments(@PathVariable("postId") String postId) {
        logger.info("Find all likes without comments for post id: {}", postId);
        List<Like> likes = likeService.findAllByPost(postId);
        if (!likes.isEmpty()) {
            return ResponseEntity.ok(likes);
        } else {
            logger.info("Likes was not found for post {}", postId);
            return ResponseEntity.noContent().build();
        }

    }

    // Endpoint to find all likes for comments, useless rn
    @Operation(summary = "Find all likes for comments")
    @GetMapping("/comments")
    public ResponseEntity<List<Like>> findAllForComments() {
        logger.info("Find all likes For comments");
        List<Like> likes = likeService.findAllForComments();
        logger.info("Found {} likes For comments", likes.size());
        if (!likes.isEmpty()) {
            return ResponseEntity.ok(likes);
        } else {
            logger.info("Likes was not found for comments");
            return ResponseEntity.noContent().build();
        }
    }

    // Endpoint to find all likes for a specific comment
    @Operation(summary = "Find all likes for a specific comment")
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<List<Like>> findAllForComments(@PathVariable("commentId") String commentId) {
        logger.info("Find all likes for comment id: {}", commentId);
        List<Like> likes = likeService.findAllForComments(commentId);

        if (!likes.isEmpty()) {
            logger.info("Found {} likes for comment id: {}", likes.size(), commentId);
            return ResponseEntity.ok(likes);
        } else {
            logger.info("Like for comment id {} was not found", commentId);
            return ResponseEntity.noContent().build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Like> getLikeById(@PathVariable String id) {
        Optional<Like> like = likeService.findById(id);
        return like.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Like> createLike(@RequestBody SimplifiedLikeDTO simplifiedLikeDTO) {
        Like like = likeService.createLike(simplifiedLikeDTO);
        return ResponseEntity.ok(like);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Like> updateLike(@PathVariable String id) {
        Optional<Like> updatedLike = likeService.updateLike(id);
        return updatedLike.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable String id) {
        likeService.deleteLike(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/count/postId/{postId}")
    public ResponseEntity<Integer> countLikesByPostId(@PathVariable String postId) {
        int count = likeService.countAllByPost(postId);
        return ResponseEntity.ok(count);
    }
}
