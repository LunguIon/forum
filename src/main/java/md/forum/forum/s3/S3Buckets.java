package md.forum.forum.s3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "aws.s3.buckets")
public class S3Buckets {
    private String users;
    private String posts;

}
