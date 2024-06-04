package md.forum.forum.services;

import lombok.RequiredArgsConstructor;
import md.forum.forum.dto.get.UserDTO;
import md.forum.forum.dto.mappers.UserDTOMapper;
import md.forum.forum.models.User;
import md.forum.forum.repository.UserRepository;
import md.forum.forum.s3.S3Buckets;
import md.forum.forum.s3.S3Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final UserDTOMapper userDTOMapper;
    public User createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists", ex);
        }
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .map(userDTOMapper)
                .orElseThrow(() -> new NoSuchElementException("User with email [%s] was not found!".formatted(email)
                )));
    }
    public Optional<User> getUserByEmailFull(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userDTOMapper);
    }

    public User updateUserPassword(String email, String password) {
        return userRepository.findByEmail(email).map(user -> {
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


    public User updateUserProfile(String email, User newUser) {
        return userRepository.findByEmail(email).map(user -> {
            user.setUsername(newUser.getUsername());
            user.setImageUrlProfile(newUser.getImageUrlProfile());
            return userRepository.save(user);
        }).orElse(null);
    }

    public boolean updateUserUsername(String email, String newUsername) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            if (newUsername != null) {
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
        if (user != null) {
            if (newImageUrl != null) {
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

    public void uploadUserProfileImage(String userEmail, MultipartFile file) {
        if(!userRepository.existsByEmail(userEmail)) {
            try {
                throw new ChangeSetPersister.NotFoundException();
            } catch (ChangeSetPersister.NotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            String profileImageId = UUID.randomUUID().toString();
            try {
                s3Service.putObject(
                        s3Buckets.getUsers(),
                        "profile-images/%s/%s".formatted(userEmail, profileImageId),
                        file.getBytes()
                );
                userRepository.updateUserImageUrl(userEmail,profileImageId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public byte[] getUserProfileImage(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        byte[] profileImage = null;
        if(user != null) {
            if(user.getImageUrlProfile() != null) {
                if(!user.getImageUrlProfile().contains("https")){
                    String profileImageId = user.getImageUrlProfile();
                    profileImage = s3Service.getObject(
                            s3Buckets.getUsers(),
                            "profile-images/%s/%s".formatted(userEmail,profileImageId)
                    );
                }

            }
        }
        return profileImage;
    }

}
