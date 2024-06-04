package md.forum.forum.services;

import lombok.RequiredArgsConstructor;
import md.forum.forum.dto.get.GetCommentDTO;
import md.forum.forum.dto.mappers.CommentDTOMapper;
import md.forum.forum.dto.simplified.SimplifiedCommentDTO;
import md.forum.forum.models.Comment;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import md.forum.forum.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class  CommentService {
    private final CommentRepository commentRepository;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final UserService userService;
    private final PostService postService;
    private final CommentDTOMapper commentDTOMapper;

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<GetCommentDTO> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(commentDTOMapper)
                .collect(Collectors.toList());
    }

    public Optional<GetCommentDTO> getCommentByCommentId(String commentId) {
        return commentRepository.findCommentByCommentId(commentId)
                .map(commentDTOMapper);
    }

    public Optional<Comment> getCommentByCommentIdFull(String commentId) {
        return commentRepository.findCommentByCommentId(commentId);
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public List<GetCommentDTO> getCommentsByPostId(String postId) {
        return commentRepository.findAllByPostPostId(postId)
                .stream()
                .map(commentDTOMapper)
                .collect(Collectors.toList());
    }

    public Comment createComment(SimplifiedCommentDTO simplifiedCommentDTO){
        Comment comment = new Comment();
        LocalDate now = LocalDate.now();
        comment.setCommentId();
        comment.setContent(simplifiedCommentDTO.getContent());
        comment.setCreationDate(Date.valueOf(dtf.format(now)));
        User user = userService.getUserByEmailFull(simplifiedCommentDTO.getEmail()).orElseThrow();
        comment.setUser(user);
        Post post = postService.getPostByPostIdFull(simplifiedCommentDTO.getPostId()).orElseThrow();
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long id, Comment newComment) {
        return commentRepository.findById(id).map(comment -> {
            comment.setContent(newComment.getContent());
            comment.setNumberOfLikes(newComment.getNumberOfLikes());
            comment.setUpdateDate(newComment.getUpdateDate());
            return commentRepository.save(comment);
        }).orElse(null);
    }

    public Comment updateComment(String id, SimplifiedCommentDTO newComment) {
        LocalDate now = LocalDate.now();
        return commentRepository.findCommentByCommentId(id).map(comment -> {
            comment.setContent(newComment.getContent());
            comment.setUpdateDate(Date.valueOf(dtf.format(now)));
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
    public boolean deleteComment(String id) {
        if (commentRepository.existsCommentByCommentId(id)) {
            commentRepository.deleteCommentByCommentId(id);
            return true;
        }
        return false;
    }
}
