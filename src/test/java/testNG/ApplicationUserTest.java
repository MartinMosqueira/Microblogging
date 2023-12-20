package testNG;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import app.project.microblogging.domain.ApplicationUser;
import app.project.microblogging.repository.ApplicationUserRepository;
import app.project.microblogging.service.ApplicationUserService;
import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ApplicationUserTest {

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @InjectMocks
    private ApplicationUserService applicationUserService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddContactUser() {
        ApplicationUser contacto = new ApplicationUser();
        contacto.setId(2L);

        ApplicationUser user = new ApplicationUser();
        user.setId(1L);
        user.getContactos().add(contacto);

        when(applicationUserRepository.findWithContactosById(1L)).thenReturn(Optional.of(user));
        when(applicationUserRepository.findApplicationUserById(2L)).thenReturn(Optional.of(contacto));

        applicationUserService.addContactoService(user.getId(), contacto.getId());

        Assert.assertTrue(user.getContactos().contains(contacto));

        verify(applicationUserRepository).save(user);
    }
}
