package md.forum.forum.services;

import md.forum.forum.models.Comment;
import md.forum.forum.repositorys.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public Comment updateComment(Long id, Comment newComment) {
        return commentRepository.findById(id).map(comment -> {
            comment.setContent(newComment.getContent());
            comment.setValue_of_like(newComment.getValue_of_like());
            comment.setUpdate_date(newComment.getUpdate_date());
            return commentRepository.save(comment);
        }).orElse(null);
    }


    public boolean deleteComment(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
