package md.forum.forum.services;

import lombok.RequiredArgsConstructor;
import md.forum.forum.dto.simplified.SimplifiedLikeDTO;
import md.forum.forum.models.Comment;
import md.forum.forum.models.Like;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import md.forum.forum.repository.CommentRepository;
import md.forum.forum.repository.LikeRepository;
import md.forum.forum.repository.PostRepository;
import md.forum.forum.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Like> findAll() {
        return likeRepository.findAll();
    }

    public List<Like> findAllByUser(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        return likeRepository.findAllByUser(user.orElse(null));
    }

    public List<Like> findAllByPost(String postId) {
        Optional<Post> post = postRepository.findPostByPostId(postId);
        return likeRepository.findAllByPost(post.orElse(null));
    }

    public List<Like> findAllForPost() {
        return likeRepository.findAllForPost();
    }

    public List<Like> findAllForPost(long postId) {
        Optional<Post> post = postRepository.findById( postId);
        return likeRepository.findAllForPost(post.orElse(null));
    }

    public List<Like> findAllForComments() {
        return likeRepository.findAllForComments();
    }

    public List<Like> findAllForComments(String commentId) {
        Optional<Comment> comment = commentRepository.findCommentByCommentId(commentId);
        return likeRepository.findAllForCommentsBy(comment.orElse(null));
    }
    public Optional<Like> findById(String id) {
        return likeRepository.findByLikeId(id);
    }

    public Like createLike(SimplifiedLikeDTO simplifiedLikeDTO) {
        Like like = new Like();
        LocalDate now = LocalDate.now();
        like.setLikeId();
        like.setCreateDate(Date.valueOf(dtf.format(now)));
        User user = userRepository.findByEmail(simplifiedLikeDTO.getUserEmail()).orElseThrow();
        like.setUser(user);
        Post post = postRepository.findPostByPostId(simplifiedLikeDTO.getPostId()).orElseThrow();
        like.setPost(post);
        Comment comment = commentRepository.findCommentByCommentId(simplifiedLikeDTO.getCommentId()).orElse(null);
        like.setComment(comment);
        like.setUpvote(simplifiedLikeDTO.isUpvote());
        return likeRepository.save(like);
    }

    public Optional<Like> updateLike(String likeId) {
        LocalDate now = LocalDate.now();
        return likeRepository.findByLikeId(likeId).map(like -> {
            like.setUpvote(!like.isUpvote());
            like.setCreateDate(Date.valueOf(dtf.format(now)));
            return likeRepository.save(like);
        });
    }

    public void deleteLike(String likeId) {
        likeRepository.deleteByLikeId(likeId);
    }


}
