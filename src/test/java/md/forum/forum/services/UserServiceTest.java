package md.forum.forum.services;

import md.forum.forum.models.User;
import md.forum.forum.repositorys.UserRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest implements WithAssertions {
    static final String USERNAME = "Username";
    static final String EMAIL = "johndoe@gmail.com";
    static final String PASSWORD = "password";
    static final String IMG_URL = "image/url";
    static final Long USER_ID = 2L;

    @Mock
    private UserRepository userRepository;
    @Mock
    private User user;
    @Mock
    private User userSecond;
    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser() {
        when(user.getUsername()).thenReturn(USERNAME);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPasswordHash()).thenReturn(PASSWORD);
        when(user.getImageUrlProfile()).thenReturn(IMG_URL);
        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.createUser(user))
                .returns(USERNAME, User::getUsername)
                .returns(EMAIL, User::getEmail)
                .returns(PASSWORD, User::getPasswordHash)
                .returns(IMG_URL, User::getImageUrlProfile);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser_IsNull() {
        when(userRepository.save(user)).thenReturn(null);

        assertThat(userService.createUser(user)).isNull();

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user, userSecond));

        assertThat(userService.getAllUsers()).contains(user, userSecond);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsers_IsEmpty() {
        when(userRepository.findAll()).thenReturn(List.of());

        assertThat(userService.getAllUsers()).isEmpty();

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById() {
        when(user.getId()).thenReturn(Math.toIntExact(USER_ID));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        assertThat(userService.getUserById(USER_ID))
                .isPresent().isEqualTo(Optional.of(user))
                .get().extracting(User::getId)
                .isEqualTo(Math.toIntExact(USER_ID));

        verify(userRepository, times(1)).findById(USER_ID);
    }

    @Test
    void getUserById_EmptyList() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThat(userService.getUserById(USER_ID)).isEmpty();

        verify(userRepository, times(1)).findById(USER_ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testGetUserByEmail() {
        when(user.getId()).thenReturn(Math.toIntExact(USER_ID));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThat(userService.getUserByEmail(EMAIL))
                .isPresent()
                .get()
                .extracting(User::getId)
                .isEqualTo(Math.toIntExact(USER_ID));

        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test
    void testGetUserByEmail_EmptyUser() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThat(userService.getUserByEmail(EMAIL)).isEmpty();

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testGetUserByUsername() {
        when(user.getId()).thenReturn(Math.toIntExact(USER_ID));
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        assertThat(userService.getUserByUsername(USERNAME))
                .isEqualTo(Optional.of(user))
                .get()
                .extracting(User::getId)
                .isEqualTo(Math.toIntExact(USER_ID));

        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    void testUpdatePassword() {
        when(user.getPasswordHash()).thenReturn(PASSWORD);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.updateUserPassword(EMAIL, PASSWORD))
                .isNotNull()
                .isEqualTo(user)
                .returns(PASSWORD, User::getPasswordHash);

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdatePassword_IsNull() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThat(userService.updateUserPassword(EMAIL, PASSWORD)).isNull();

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testBanUser() {
        when(user.isBanned()).thenReturn(true);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.banUser(EMAIL)).returns(true, User::isBanned);

        verify(userRepository).findByEmail(EMAIL);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUserProfile() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userSecond.getUsername()).thenReturn(USERNAME);
        when(userSecond.getImageUrlProfile()).thenReturn(IMG_URL);
        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.updateUserProfile(EMAIL, userSecond)).isEqualTo(user);

        verify(userRepository).findByEmail(EMAIL);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUserProfile_IsNull() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThat(userService.updateUserProfile(EMAIL, userSecond)).isNull();

        verify(userRepository).findByEmail(EMAIL);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testUpdateUserUsername() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userSecond.getUsername()).thenReturn(USERNAME);
        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.updateUserProfile(EMAIL, userSecond)).isEqualTo(user);

        verify(userRepository).findByEmail(EMAIL);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUserUsername_IsNull() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThat(userService.updateUserUsername(EMAIL, USERNAME)).isFalse();

        verify(userRepository).findByEmail(EMAIL);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testUpdateUserProfileImage() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.updateUserProfileImage(EMAIL, IMG_URL)).isTrue();

        verify(userRepository).findByEmail(EMAIL);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUserProfileImage_IsNull() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThat(userService.updateUserProfileImage(EMAIL, IMG_URL)).isFalse();

        verify(userRepository).findByEmail(EMAIL);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testDeleteComment() {
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);
        doNothing().when(userRepository).deleteByEmail(EMAIL);

        assertThat(userService.deleteUser(EMAIL)).isTrue();

        verify(userRepository).deleteByEmail(EMAIL);
    }

    @Test
    void testDeleteComment_NotExist() {
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);

        assertThat(userService.deleteUser(EMAIL)).isFalse();

        verify(userRepository, never()).deleteByEmail(EMAIL);
    }
}
