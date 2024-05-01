package md.forum.forum.controllers;

import md.forum.forum.models.Comment;
import md.forum.forum.services.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private static final Logger logger = LogManager.getLogger(CommentController.class);
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        logger.info("CommentController was initialized");
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        logger.info("getAllComments was called");
        List<Comment> comments = commentService.getAllComments();
        logger.info("getAllComments returned {} comments", comments.size());
        return ResponseEntity.ok(comments);
    }
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        logger.info("getCommentsByPostId was called");
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        logger.info("getCommentsByPostId returned {} comments", comments.size());
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id)
                .map(comment -> {
                    logger.info("getCommentById was called with id {}", id);
                    return ResponseEntity.ok(comment);
                })
                .orElseGet(() -> {
                    commentNotFoundLog(id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        logger.info("createComment was called");
        Comment createdComment = commentService.createComment(comment);
        if(createdComment != null) {
            logger.info("Comment with id {} was created", createdComment.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        }
        else {
            logger.error("Comment was not created");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        logger.info("updateComment was called");
        Comment updatedComment = commentService.updateComment(id, comment);
        if (updatedComment != null) {
            logger.info("Comment with id {} was updated", id);
            return ResponseEntity.ok(updatedComment);
        } else {
            commentNotFoundLog(id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        logger.info("deleteComment was called");
        boolean deleted = commentService.deleteComment(id);
        if (deleted){
            logger.info("Comment with id {} was deleted", id);
            return ResponseEntity.noContent().build();
        }
        else {
            commentNotFoundLog(id);
            return ResponseEntity.notFound().build();
        }

    }
    public void commentNotFoundLog(Long id){
        logger.error("Comment with id {} was not found", id);
    }
}
