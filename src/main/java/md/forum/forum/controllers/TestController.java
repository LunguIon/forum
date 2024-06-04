package md.forum.forum.controllers;


import md.forum.forum.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/test")
public class TestController {
    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello World!");
    }
    @PostMapping(value="/put",
            consumes = MULTIPART_FORM_DATA_VALUE)
    public byte[] testPut(@RequestParam("file") MultipartFile file) {
       userService.uploadUserProfileImage("Liebe@gmail.com",file);
      return userService.getUserProfileImage("Liebe@gmail.com");
    }

}
