package md.forum.forum;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class ForumApplication {

	static Logger logger = Logger.getLogger(ForumApplication.class.getName());

	public static void main(String[] args) {
		logger.info("App starts");
		SpringApplication.run(ForumApplication.class, args);
		logger.info("App ends");
	}

}
