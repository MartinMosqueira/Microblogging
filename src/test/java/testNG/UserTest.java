package testNG;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import app.project.microblogging.domain.User;
import app.project.microblogging.repository.UserRepository;
import app.project.microblogging.service.UserService;
import app.project.microblogging.service.dto.AdminUserDTO;
import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Transactional
public class UserTest {

    private static final String DEFAULT_LOGIN = "example_user";

    private static final String DEFAULT_EMAIL = "example_user@localhost";

    @Mock
    private UserRepository userRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
        user.setActivated(true);

        when(cacheManager.getCache(anyString())).thenReturn(mock(Cache.class));
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");
    }

    @AfterMethod
    public void resetMethod() {
        userService = null;
    }

    @Test
    public void testCreateUser() {
        String login = "example_user";
        String email = "example_user@localhost";

        AdminUserDTO adminUserDTO = new AdminUserDTO();
        adminUserDTO.setLogin(login);
        adminUserDTO.setEmail(email);
        adminUserDTO.setActivated(true);

        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");

        User savedUser = userService.createUser(adminUserDTO);

        Assert.assertNotNull(savedUser);
        Assert.assertEquals(savedUser.getLogin(), login);
        Assert.assertEquals(savedUser.getEmail(), email);
        Assert.assertTrue(savedUser.isActivated());

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(any(CharSequence.class));
        verify(cacheManager, times(2)).getCache(anyString());
    }

    @Test
    public void testDeleteUser() {
        String login = "user";

        when(userRepository.findOneByLogin(anyString())).thenReturn(Optional.of(user));

        userService.deleteUser(login);

        Assert.assertNotNull(user);

        verify(userRepository, times(1)).delete(user);
        verify(cacheManager, times(2)).getCache(anyString());
    }
}
