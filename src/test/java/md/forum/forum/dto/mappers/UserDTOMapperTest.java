package md.forum.forum.dto.mappers;

import md.forum.forum.dto.get.UserDTO;
import md.forum.forum.models.Role;
import md.forum.forum.models.User;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;

@ExtendWith(MockitoExtension.class)
public class UserDTOMapperTest implements WithAssertions {
    @InjectMocks
    UserDTOMapper dtoMapper;
    @Mock
    Date date;
    @Mock
    Role role;
    @Test
    void testShouldMapToUserDTO() {
        User user = new User(1, "tanja", "je@gmail.com", "hash", "img", date, role , true);

        assertThat(dtoMapper.apply(user))
                .returns(user.getUsername(), UserDTO::username)
                .returns(user.getEmail(), UserDTO::email)
                .returns(user.getImageUrlProfile(), UserDTO::imageUrl);
    }

    @Test
    void testShouldMapToUserDTO_ToNull() {
        assertThatThrownBy(() -> dtoMapper.apply(null))
                .isInstanceOf(NullPointerException.class);
    }
}
