package md.forum.forum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class ForumApplication {

	private static final Logger logger = LogManager.getLogger(ForumApplication.class);

	public static void main(String[] args) {
		logger.info("App started");
		SpringApplication.run(ForumApplication.class, args);
	}

}
