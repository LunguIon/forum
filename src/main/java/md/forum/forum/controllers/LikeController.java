package md.forum.forum.controllers;

import md.forum.forum.models.Like;
import md.forum.forum.services.LikeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;
    Logger logger = LogManager.getLogger(LikeController.class);
    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;

    }

    @GetMapping
    public ResponseEntity<List<Like>> getAllLikes() {
        logger.info("Get all likes");
        List<Like> likes = likeService.findAll();
        if (likes.isEmpty()) {
            logger.info("No likes found");
            return ResponseEntity.notFound().build();
        }
        logger.info("Found {} likes", likes.size());
        return ResponseEntity.ok(likes);
    }
    // Endpoint to find all likes by a specific user
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<Like>> findAllByUser(@PathVariable("userEmail") String userEmail) {
        logger.info("Find all likes by user email: {}", userEmail);
        List<Like> likes = likeService.findAllByUser(userEmail);
        logger.info("Found {} likes by user email: {}", likes.size(), userEmail);
        if (!likes.isEmpty()) {
            return ResponseEntity.ok(likes);
        }
        else {
            return ResponseEntity.noContent().build();
        }

    }

    // Endpoint to find all likes for a specific post, useless rn
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Like>> findAllByPost(@PathVariable("postId") int postId) {
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
    @GetMapping("/post/withoutComments")
    public ResponseEntity<List<Like>> findAllForPostWithoutComments() {
        logger.info("Find all likes without comments");
        List<Like> likes = likeService.findAllForPost();
        if (!likes.isEmpty()) {
            return ResponseEntity.ok(likes);
        }
        else {
            logger.info("Likes was not found");
            return ResponseEntity.noContent().build();
        }
    }

    // Endpoint to find all likes for a specific post with no comments
    @GetMapping("/post/{postId}/withoutComments")
    public ResponseEntity<List<Like>> findAllForPostWithoutComments(@PathVariable("postId") int postId) {
        logger.info("Find all likes without comments for post id: {}", postId);
        List<Like> likes = likeService.findAllByPost(postId);
        if (!likes.isEmpty()) {
            return ResponseEntity.ok(likes);
        }
        else {
            logger.info("Likes was not found for post {}",postId);
            return ResponseEntity.noContent().build();
        }

    }

    // Endpoint to find all likes for comments, useless rn
    @GetMapping("/comments")
    public ResponseEntity<List<Like>> findAllForComments() {
        logger.info("Find all likes For comments");
        List<Like> likes = likeService.findAllForComments();
        logger.info("Found {} likes For comments", likes.size());
        if (!likes.isEmpty()) {
            return ResponseEntity.ok(likes);
        }
        else{
            logger.info("Likes was not found for comments");
            return ResponseEntity.noContent().build();
        }

    }

    // Endpoint to find all likes for a specific comment
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<List<Like>>findAllForComments(@PathVariable("commentId") int commentId) {
        logger.info("Find all likes for comment id: {}", commentId);
        List<Like> likes = likeService.findAllForComments(commentId);

        if (!likes.isEmpty()) {
            logger.info("Found {} likes for comment id: {}", likes.size(), commentId);
            return ResponseEntity.ok(likes);
        }
        else{
            logger.info("Likes was not found for comments");
            return ResponseEntity.noContent().build();
        }
    }

}
