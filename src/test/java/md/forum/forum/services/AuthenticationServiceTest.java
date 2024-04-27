package md.forum.forum.services;

import md.forum.forum.dto.LoginUserDto;
import md.forum.forum.dto.RegisterUserDto;
import md.forum.forum.models.User;
import md.forum.forum.repositorys.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "1213";
    public static final String HASH = "haaaash";
    @Mock
    User user;
    @Mock
    User userExist;
    @Mock
    LoginUserDto loginUserDto;
    @Mock
    RegisterUserDto registerUserDto;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    UsernamePasswordAuthenticationToken token;
    @InjectMocks
    AuthenticationService authenticationService;
    @Test
    void testSingUp() {
        try (MockedConstruction<User> userMockedConstruction = mockConstruction(User.class,
                (mock, context) -> when(userRepository.save(mock)).thenReturn(user))
        ) {
            when(registerUserDto.getUsername()).thenReturn(USERNAME);
            when(registerUserDto.getEmail()).thenReturn(EMAIL);
            when(registerUserDto.getPassword()).thenReturn(PASSWORD);
            when(passwordEncoder.encode(PASSWORD)).thenReturn(HASH);

            assertThat(authenticationService.signUp(registerUserDto)).isEqualTo(user);

            verify(passwordEncoder).encode(PASSWORD);
        }
    }

    @Test
    void testSingUp_IsNull() {
        try (MockedConstruction<User> userMockedConstruction = mockConstruction(User.class,
                (mock, context) -> when(userRepository.save(mock)).thenReturn(null))
        ) {
            assertThat(authenticationService.signUp(registerUserDto)).isNull();
        }
    }

    @Test
    void testSingUp_AlreadyExist() {
        try (MockedConstruction<User> userMockedConstruction = mockConstruction(User.class,
                (mock, context) -> when(userRepository.save(mock)).thenReturn(user))
        ) {
            assertThat(authenticationService.signUp(registerUserDto)).isNotEqualTo(userExist);
        }
    }

    @Test
    void testAuthenticate() {
        when(loginUserDto.getEmail()).thenReturn(EMAIL);
        when(loginUserDto.getPassword()).thenReturn(PASSWORD);
        try (MockedConstruction<UsernamePasswordAuthenticationToken> userMockedConstruction = mockConstruction(UsernamePasswordAuthenticationToken.class,
                (mock, context) -> when(authenticationManager.authenticate(mock)).thenReturn(token))
        ) {
            when(userRepository.findByEmail(loginUserDto.getEmail())).thenReturn(Optional.of(user));
            assertThat(authenticationService.authenticate(loginUserDto)).isEqualTo(user);
            verify(userRepository).findByEmail(loginUserDto.getEmail());
        }
    }

    @Test
    void testAuthenticate_IsNull() {
        try (MockedConstruction<UsernamePasswordAuthenticationToken> userMockedConstruction = mockConstruction(UsernamePasswordAuthenticationToken.class,
                (mock, context) -> when(authenticationManager.authenticate(mock)).thenReturn(token))
        ) {
            assertThatThrownBy(()->authenticationService.authenticate(null))
                    .isInstanceOf(NullPointerException.class);

            verifyNoMoreInteractions(userRepository);
        }
    }

    @Test
    void testAuthenticate_IsEmpty() {
        when(loginUserDto.getEmail()).thenReturn(EMAIL);
        when(loginUserDto.getPassword()).thenReturn(PASSWORD);
        try (MockedConstruction<UsernamePasswordAuthenticationToken> userMockedConstruction = mockConstruction(UsernamePasswordAuthenticationToken.class,
                (mock, context) -> when(authenticationManager.authenticate(mock)).thenReturn(token))
        ) {
            when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

            assertThatThrownBy(()->authenticationService.authenticate(loginUserDto))
                    .isInstanceOf(NoSuchElementException.class);

            verify(userRepository).findByEmail(EMAIL);
        }
    }
}
