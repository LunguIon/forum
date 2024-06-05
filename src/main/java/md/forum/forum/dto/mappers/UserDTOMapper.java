package md.forum.forum.dto.mappers;

import md.forum.forum.dto.get.UserDTO;
import md.forum.forum.models.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getImageUrlProfile()
        );
    }
}
