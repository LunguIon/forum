package md.forum.forum.controllers;

import md.forum.forum.models.User;
import md.forum.forum.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserById(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User user) {
        User updatedUser = userService.updateUserProfile(email, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{email}/password")
    public ResponseEntity<User> updateUserPassword(@PathVariable String email, @RequestParam String password) {
        User updatedUser = userService.updateUserPassword(email, password);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{email}/ban")
    public ResponseEntity<User> banUser(@PathVariable String email) {
        User bannedUser = userService.banUser(email);
        return bannedUser != null ? ResponseEntity.ok(bannedUser) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{email}/username")
    public ResponseEntity<Boolean> updateUserUsername(@PathVariable String email, @RequestParam String newUsername) {
        boolean updated = userService.updateUserUsername(email, newUsername);
        return updated ? ResponseEntity.ok(true) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{email}/profileImage")
    public ResponseEntity<Boolean> updateUserProfileImage(@PathVariable String email, @RequestParam String newImageUrl) {
        boolean updated = userService.updateUserProfileImage(email, newImageUrl);
        return updated ? ResponseEntity.ok(true) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        boolean deleted = userService.deleteUser(email);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
