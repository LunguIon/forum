package md.forum.forum.controllers;

import md.forum.forum.dto.SimplifiedTopicDTO;
import md.forum.forum.models.Topic;
import md.forum.forum.services.TopicService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topics")

public class TopicController {

    private final TopicService topicService;
    private static final Logger logger = LogManager.getLogger(TopicController.class);
    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
        logger.info("TopicController created");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable int id) {
        Optional<Topic> topicOptional = topicService.findTopicById(id);
        return topicOptional.map(topic -> ResponseEntity
                        .ok()
                        .body(topic))
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Topic> getTopicByTitle(@PathVariable String title) {
        Optional<Topic> topicOptional = topicService.findTopicByTitle(title);
        return topicOptional.map(topic -> ResponseEntity
                        .ok()
                        .body(topic))
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Topic>> getTopicsByUserEmail(@PathVariable String email) {
        Optional<List<Topic>> topicsOptional = topicService.findTopicsByUserEmail(email);
        return topicsOptional.map(topics -> ResponseEntity
                        .ok()
                        .body(topics))
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Topic>> getAllTopicsOrderedByTitleDesc() {
        Optional<List<Topic>> topicsOptional = topicService.findAllTopicsOrderByTitleDesc();
        return topicsOptional.map(topics -> ResponseEntity
                        .ok()
                        .body(topics))
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody SimplifiedTopicDTO simplifiedTopicDTO) {
        Topic topic = topicService.createTopic(simplifiedTopicDTO);
        if (topic == null) {
            logger.info("Topic creation failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        else {
            logger.info("Topic created");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteTopicByTitle(@PathVariable String title) {
        topicService.deleteTopicByTitle(title);
        return ResponseEntity
                .ok()
                .build();
    }

}