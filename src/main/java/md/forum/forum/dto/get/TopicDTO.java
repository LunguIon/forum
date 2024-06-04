package md.forum.forum.dto.get;

public record TopicDTO(
        String title,
        String content,
        String imageUrl,
        UserDTO user
) {
}
