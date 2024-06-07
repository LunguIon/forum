package md.forum.forum.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import md.forum.forum.dto.get.GetUserDTO;
import md.forum.forum.models.User;
import md.forum.forum.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/users")
@Tag(name = "User controller methods")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
        logger.info("UserController was initialized");
    }

    @Operation(summary = "Find all users")
    @GetMapping
    public ResponseEntity<List<GetUserDTO>> getAllUsers() {
        logger.info("getAllUsers was called");
        List<GetUserDTO> users = userService.getAllUsers();
        logger.info("getAllUsers returned {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Find user by email")
    @GetMapping("/{email}")
    public ResponseEntity<GetUserDTO> getUserByEmail(@PathVariable String email) {
        logger.info("getUserByEmail called for email: {}", email);
        return userService.getUserByEmail(email)
                .map(user -> {
                    logger.info("User found with email: {}", email);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.error("User not found with email: {}", email);
                    return ResponseEntity.notFound().build();
                });
    }

    @Operation(summary = "Create new user")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        if(createdUser == null) {
            logger.error("User creation failed: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }else {
            logger.info("User created: {}", createdUser.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }


    }

    @Operation(summary = "Update user by email")
    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User user) {
        User updatedUser = userService.updateUserProfile(email, user);
        if (updatedUser != null) {
            logger.info("User updated: {}", updatedUser.getEmail());
            return ResponseEntity.ok(updatedUser);
        } else {
            logger.error("User update failed: {}", user.getEmail());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update user's password by email")
    @PutMapping("/{email}/password")
    public ResponseEntity<User> updateUserPassword(@PathVariable String email, @RequestParam String password) {
        User updatedUser = userService.updateUserPassword(email, password);
        if (updatedUser != null) {
            logger.info("User: {} updated password", updatedUser.getEmail());
            return ResponseEntity.ok(updatedUser);
        }
        else {
            logger.error("User: {} update password failed: ",email);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Ban user by email")
    @PutMapping("/{email}/ban")
    public ResponseEntity<User> banUser(@PathVariable String email) {
        User bannedUser = userService.banUser(email);
        if (bannedUser != null) {
            logger.info("User: {} banned", bannedUser.getEmail());
            return ResponseEntity.ok(bannedUser);
        }
        else {
            logger.error("User: {} banned failed: ", email);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update user's username by email")
    @PutMapping("/{email}/username")
    public ResponseEntity<Boolean> updateUserUsername(@PathVariable String email, @RequestParam String newUsername) {
        boolean updated = userService.updateUserUsername(email, newUsername);
        if (updated) {
            logger.info("User: {} updated username", email);
            return ResponseEntity.ok(true);
        }
        else {
            logger.error("User: {} username update failed: ", email);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update user's profile image by email")
    @PutMapping("/{email}/profileImage")
    public ResponseEntity<Boolean> updateUserProfileImage(@PathVariable String email, @RequestParam String newImageUrl) {
        boolean updated = userService.updateUserProfileImage(email, newImageUrl);
        if (updated) {
            logger.info("User: {} updated profile image", email);
            return ResponseEntity.ok(true);
        }
        else{
            logger.error("User: {} profile image update failed: ", email);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete user by email")
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        boolean deleted = userService.deleteUser(email);
        if (deleted) {
            logger.info("User: {} deleted", email);
            return ResponseEntity.noContent().build();
        }
        else {
            logger.error("User: {} deletion failed: ", email);
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping(
            value = "{userEmail}/profile-image",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    public void uploadUserProfileImage(@PathVariable("userEmail") String userEmail,
                                       @RequestParam("file") MultipartFile file) {
        userService.uploadUserProfileImage(userEmail, file);

    }
    @GetMapping("{userEmail}/profile-image")
    public byte [] uploadUserProfileImage(@PathVariable("userEmail") String userEmail) {
        return userService.getUserProfileImage(userEmail);

    }
}
