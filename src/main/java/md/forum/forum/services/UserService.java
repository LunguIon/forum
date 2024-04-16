package md.forum.forum.services;

import md.forum.forum.models.User;
import md.forum.forum.repositorys.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User>getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User updateUser(String email, User newUser) {
        //need a meeting to talk about update
        return userRepository.findByEmail(email).map(user -> {
            user.setUsername(newUser.getUsername());
            //idk what about 'email'
            user.setPasswordHash(newUser.getPasswordHash());
            user.setImageUrlProfile(newUser.getImageUrlProfile());
            user.setBanned(newUser.isBanned());
            //i also don't know about 'role'
            return userRepository.save(user);
        }).orElse(null);
    }

    // Delete operation
    public boolean deleteUser(String email) {
        if (userRepository.existsByEmail(email)) {
            userRepository.deleteByEmail(email);
            return true;
        }
        return false;
    }
}
