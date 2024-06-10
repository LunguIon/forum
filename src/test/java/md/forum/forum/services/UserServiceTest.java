package md.forum.forum.services;

import md.forum.forum.dto.get.UserDTO;
import md.forum.forum.dto.mappers.UserDTOMapper;
import md.forum.forum.models.User;
import md.forum.forum.repository.UserRepository;
import md.forum.forum.s3.S3Buckets;
import md.forum.forum.s3.S3Service;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest implements WithAssertions {
    static final String USERNAME = "Username";
    public static final String NEW_USERNAME = "usernamenew";
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
    @Mock
    private UserDTO userDTO;
    @Mock
    private UserDTO userDTOSecond;
    @Mock
    private UserDTOMapper userDTOMapper;
    @Mock
    private MultipartFile file;
    @Mock
    private S3Buckets s3Buckets;
    @Mock
    private S3Service s3Service;

    @InjectMocks
    private UserService userService;

    @Test
    void testCrateUser() {
        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.createUser(user))
                .isNotNull()
                .isEqualTo(user);

        verify(userRepository).save(user);
    }

    @Test
    void testCrateUser_Exception() {
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user, userSecond));

        when(userDTOMapper.apply(user)).thenReturn(userDTO);
        when(userDTOMapper.apply(userSecond)).thenReturn(userDTOSecond);

        assertThat(userService.getAllUsers())
                .contains(userDTO, userDTOSecond);

        verify(userRepository).findAll();
        verify(userDTOMapper).apply(user);
        verify(userDTOMapper).apply(userSecond);
    }

    @Test
    void testGetAllUsers_IsNull() {
        assertThat(userService.getAllUsers()).isEmpty();
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
    void getUserByEmail() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userDTOMapper.apply(user)).thenReturn(userDTO);
        when(userDTO.email()).thenReturn(EMAIL);

        assertThat(userService.getUserByEmail(EMAIL))
                .isNotNull()
                .isEqualTo(Optional.of(userDTO))
                .get().extracting(UserDTO::email)
                .isEqualTo(EMAIL);

        verify(userRepository).findByEmail(EMAIL);
        verify(userDTOMapper).apply(user);
    }

    @Test
    void getUserByEmail_IsNull()  {
        assertThatThrownBy(() -> userService.getUserByEmail(EMAIL))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User with email ["+ EMAIL +"] was not found!");
    }

    @Test
    void testGetUserByEmailFull() {
        when(user.getEmail()).thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThat(userService.getUserByEmailFull(EMAIL))
                .isEqualTo(Optional.of(user))
                .get().extracting(User::getEmail)
                .isEqualTo(EMAIL);

        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void testGetUserByEmailFull_IsEmpty() {
        assertThat(userService.getUserByEmailFull(EMAIL)).isEmpty();
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(userDTOMapper.apply(user)).thenReturn(userDTO);

        assertThat(userService.getUserByUsername(USERNAME))
                .contains(userDTO);

        verify(userRepository).findByUsername(USERNAME);
        verify(userDTOMapper).apply(user);
    }

    @Test
    void testGetUserByUsername_IsEmpty() {
        assertThat(userService.getUserByUsername(USERNAME)).isEmpty();
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
    void  testUpdateUserUsername() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.updateUserUsername(EMAIL, NEW_USERNAME)).isTrue();

        verify(userRepository).findByEmail(EMAIL);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUserUsername_NullUsername() {
        assertThat(userService.updateUserUsername(EMAIL, null)).isFalse();
    }

    @Test
    void testUpdateUserUsername_NullUser() {
        assertThat(userService.updateUserUsername(null, NEW_USERNAME)).isFalse();
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
    void testUpdateUserProfileImage_IsNullURL() {
        assertThat(userService.updateUserProfileImage(EMAIL, null)).isFalse();
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

    @Test
    void testUploadUserProfileImage_PostNotFound() {
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);

        assertThatThrownBy(() -> userService.uploadUserProfileImage(EMAIL, file))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(ChangeSetPersister.NotFoundException.class);
    }

    @Test
    void testUploadUserProfileImage_Successful() {
        UUID fixedUUID = UUID.fromString("cebb4ccf-d203-40c0-938a-a5c79a40a7d7");
        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID).thenReturn(fixedUUID);

            when(userRepository.existsByEmail(EMAIL)).thenReturn(true);
            when(file.getBytes()).thenReturn(new byte[] {1, 2, 3, 4});
            when(s3Buckets.getUsers()).thenReturn(USERNAME);

            userService.uploadUserProfileImage(EMAIL, file);

            verify(s3Service).putObject(
                    eq(USERNAME),
                    eq("profile-images/" + EMAIL + "/" + fixedUUID),
                    any(byte[].class)
            );
            verify(userRepository).updateUserImageUrl(EMAIL, fixedUUID.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





    @Test
    void testGetPostImage_PostIdContainsHttps() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThat(userService.getUserProfileImage(EMAIL)).isNull();
    }


    @Test
    void testGetUserProfileImage_WithHttpsUrl() {
        when(user.getImageUrlProfile()).thenReturn("https://example.com/image.jpg");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThat(userService.getUserProfileImage("user@example.com")).isNull();

        verify(s3Service, never()).getObject(anyString(), anyString());
    }

    @Test
    void testGetUserProfileImage_WithNonHttpsUrl() {
        when(user.getImageUrlProfile()).thenReturn(IMG_URL);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(s3Buckets.getUsers()).thenReturn(USERNAME);
        when(s3Service.getObject(eq(USERNAME), eq("profile-images/user@example.com/" + IMG_URL)))
                .thenReturn(new byte[]{1, 2, 3, 4});

        assertThat(userService.getUserProfileImage("user@example.com"))
                .isEqualTo(new byte[]{1, 2, 3, 4});

        verify(s3Service).getObject(
                USERNAME,
                "profile-images/user@example.com/" + IMG_URL
        );
    }

}
