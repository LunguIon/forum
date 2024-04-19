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
    public User updateUserPassword(String email, String password) {
        return userRepository.findByEmail(email).map(user->{
            user.setPasswordHash(password);
            return userRepository.save(user);
        }).orElse(null);
    }
    public User banUser(String email) {
        return userRepository.findByEmail(email).map(user -> {
            user.setBanned(true);
            return userRepository.save(user);
        }).orElse(null);
    }

    //firstly need to create Authentication
    public boolean checkPassword(User user, String password) {
        return false;
    }
    public User updateUserProfile(String email, User newUser) {
        return userRepository.findByEmail(email).map(user -> {
            user.setUsername(newUser.getUsername());
            user.setImageUrlProfile(newUser.getImageUrlProfile());
            return userRepository.save(user);
        }).orElse(null);
    }
    public boolean updateUserUsername(String email, String newUsername) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null) {
            if(newUsername != null) {
                user.setUsername(newUsername);
                userRepository.save(user);
                return true;
            }
            return false;
        }
        return false;

    }
    public boolean updateUserProfileImage(String email, String newImageUrl) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null) {
            if(newImageUrl != null) {
                user.setImageUrlProfile(newImageUrl);
                userRepository.save(user);
                return true;
            }
            return false;
        }
        return false;
    }


    public boolean deleteUser(String email) {
        if (userRepository.existsByEmail(email)) {
            userRepository.deleteByEmail(email);
            return true;
        }
        return false;
    }
}
