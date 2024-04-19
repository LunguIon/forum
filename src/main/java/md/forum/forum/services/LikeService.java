package md.forum.forum.services;

import md.forum.forum.models.Comment;
import md.forum.forum.models.Like;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import md.forum.forum.repositorys.CommentRepository;
import md.forum.forum.repositorys.LikeRepository;
import md.forum.forum.repositorys.PostRepository;
import md.forum.forum.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    public LikeService(@Qualifier("likeRepository") LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public List<Like> findAllByUser(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        return likeRepository.findAllByUser(user.orElse(null));
    }

    public List<Like> findAllByPost(int postId) {
        Optional<Post> post = postRepository.findById((long) postId);
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

    public List<Like> findAllForComments(long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        return likeRepository.findAllForComments(comment.orElse(null));
    }


}
