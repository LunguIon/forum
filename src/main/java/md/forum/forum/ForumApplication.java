package md.forum.forum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ForumApplication {
    private static final Logger logger = LoggerFactory.getLogger(ForumApplication.class);

    public static void main(String[] args) {
        logger.info("\n\n\n      NEW SESSION\n");
        SpringApplication.run(ForumApplication.class, args);
    }
}
