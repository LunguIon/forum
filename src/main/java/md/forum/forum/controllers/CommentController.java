package md.forum.forum.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import md.forum.forum.dto.get.GetCommentDTO;
import md.forum.forum.dto.simplified.SimplifiedCommentDTO;
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
@Tag(name = "Comment controller methods")
public class CommentController {
    private static final Logger logger = LogManager.getLogger(CommentController.class);
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        logger.info("CommentController was initialized");
        this.commentService = commentService;
    }

    @Operation(summary = "Get all comments")
    @GetMapping
    public ResponseEntity<List<GetCommentDTO>> getAllComments() {
        logger.info("getAllComments was called");
        List<GetCommentDTO> comments = commentService.getAllComments();
        logger.info("getAllComments returned {} comments", comments.size());
        return ResponseEntity.ok(comments);
    }
    @Operation(summary = "Get comments by post ID")
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<GetCommentDTO>> getCommentsByPostId(@PathVariable String postId) {
        logger.info("getCommentsByPostId was called");
        List<GetCommentDTO> comments = commentService.getCommentsByPostId(postId);
        logger.info("getCommentsByPostId returned {} comments", comments.size());
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Get comment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<GetCommentDTO> getCommentById(@PathVariable String id) {
        return commentService.getCommentByCommentId(id)
                .map(comment -> {
                    logger.info("getCommentById was called with id {}", id);
                    return ResponseEntity.ok(comment);
                })
                .orElseGet(() -> {
                    commentNotFoundLog(id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Operation(summary = "Create new comment")
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody SimplifiedCommentDTO comment) {
        logger.info("createComment was called");
        Comment createdComment = commentService.createComment(comment);
        if(createdComment != null) {
            logger.info("Comment with id {} was created", createdComment.getCommentId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        }
        else {
            logger.error("Comment was not created");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Update comment by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable String id, @RequestBody SimplifiedCommentDTO comment) {
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

    @Operation(summary = "Delete comment by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
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
    public void commentNotFoundLog(String id){
        logger.error("Comment with id {} was not found", id);
    }
}
