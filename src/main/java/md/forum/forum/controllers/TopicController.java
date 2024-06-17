package md.forum.forum.controllers;

import io.swagger.v3.oas.annotations.Operation;
import md.forum.forum.dto.get.GetTopicDTO;
import md.forum.forum.dto.simplified.SimplifiedTopicDTO;
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

    @Operation(summary = "Get Topic by ID")
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

    @Operation(summary = "Get Topic by Title")
    @GetMapping("/title/{title}")
    public ResponseEntity<GetTopicDTO> getTopicByTitle(@PathVariable String title) {
        Optional<GetTopicDTO> topicOptional = topicService.findTopicByTitle(title);
        return topicOptional.map(topic -> ResponseEntity
                        .ok()
                        .body(topic))
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @Operation(summary = "Get Topic by user email")
    @GetMapping("/user/{email}")
    public ResponseEntity<List<GetTopicDTO>> getTopicsByUserEmail(@PathVariable String email) {
        List<GetTopicDTO> topics = topicService.findTopicsByUserEmail(email);

        if (topics == null || topics.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(topics);
        }
    }

    @Operation(summary = "Get all topics ordered by title desc")
    @GetMapping("/all")
    public ResponseEntity<List<GetTopicDTO>> getAllTopicsOrderedByTitleDesc() {
        List<GetTopicDTO> topics = topicService.findAllTopicsOrderByTitleDesc();
        if (topics == null || topics.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(topics); 
        }
    }

    @Operation(summary = "Create topic")
    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody SimplifiedTopicDTO simplifiedTopicDTO) {
        Topic topic = topicService.createTopic(simplifiedTopicDTO);
        if (topic == null) {
            logger.info("Topic creation failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            logger.info("Topic created");
            return ResponseEntity.status(HttpStatus.CREATED).body(topic);
        }
    }

    @Operation(summary = "Delete topic")
    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteTopicByTitle(@PathVariable String title) {
        topicService.deleteTopicByTitle(title);
        return ResponseEntity
                .ok()
                .build();
    }

}