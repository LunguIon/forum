package md.forum.forum.dto.get;

public record GetTopicDTO(
        String title,
        String content,
        String imageUrl,
        GetUserDTO user
) {
}
