package md.forum.forum.dto.mappers;

import md.forum.forum.dto.get.GetUserDTO;
import md.forum.forum.models.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class UserDTOMapper implements Function<User, GetUserDTO> {
    @Override
    public GetUserDTO apply(User user) {
        return new GetUserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getImageUrlProfile()
        );
    }
}
