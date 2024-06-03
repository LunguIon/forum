package md.forum.forum.dto;

public record TopicDTO(
        String title,
        String content,
        String imageUrl,
        UserDTO user
) {
}
