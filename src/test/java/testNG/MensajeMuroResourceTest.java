package testNG;

import static org.mockito.Mockito.*;

import app.project.microblogging.domain.MensajeMuro;
import app.project.microblogging.domain.MensajeMuroTestSamples;
import app.project.microblogging.repository.MensajeMuroRepository;
import app.project.microblogging.web.rest.MensajeMuroResource;
import java.net.URISyntaxException;
import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MensajeMuroResourceTest {

    @Mock
    private MensajeMuroRepository mensajeMuroRepository;

    @InjectMocks
    private MensajeMuroResource mensajeMuroResource;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reset(mensajeMuroRepository);
    }

    @AfterMethod
    public void resetMethod() {
        mensajeMuroResource = null;
    }

    @Test
    public void testCreateMensajeMuro() throws URISyntaxException {
        // Crear un MensajeMuro de prueba
        MensajeMuro mensajeMuro = new MensajeMuro();
        mensajeMuro.setId(null); // Asegurarse de que el ID sea nulo
        mensajeMuro.setMensaje("Hola mundo!");
        mensajeMuro.setTags("tag1, tag2, tag3");

        // Configurar comportamiento de mock
        when(mensajeMuroRepository.save(any(MensajeMuro.class))).thenReturn(mensajeMuro);

        // Llamar al método que se va a probar
        ResponseEntity<MensajeMuro> responseEntity = mensajeMuroResource.createMensajeMuro(mensajeMuro);

        // Verificar resultados
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertEquals(mensajeMuro.getId(), responseEntity.getBody().getId());
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void testDeleteMensajeMuro() {
        Long id = 1L;

        doNothing().when(mensajeMuroRepository).deleteById(id);

        ResponseEntity<Void> responseEntity = mensajeMuroResource.deleteMensajeMuro(id);

        // Verificar que el método deleteById del repositorio se haya llamado con el ID del mensaje una vez
        verify(mensajeMuroRepository, times(1)).deleteById(id);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void testGetMensajeMuro() {
        Long id = 1L;
        MensajeMuro mensajeMuro = new MensajeMuro();
        mensajeMuro.setId(id);
        mensajeMuro.setMensaje("Hola mundo!");

        when(mensajeMuroRepository.findById(id)).thenReturn(Optional.of(mensajeMuro));

        ResponseEntity<MensajeMuro> responseEntity = mensajeMuroResource.getMensajeMuro(id);

        verify(mensajeMuroRepository, times(1)).findById(id);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertEquals(responseEntity.getBody().getId(), id);
    }

    @Test
    public void testEqualsVerifierMensajeMuro() {
        MensajeMuro mensajeMuroTest = new MensajeMuro();
        mensajeMuroTest.setId(1L);
        mensajeMuroTest.setMensaje("mensaje1");
        mensajeMuroTest.setTags("tags1");
        MensajeMuro mensajeMuro1 = MensajeMuroTestSamples.getMensajeMuroSample1();
        MensajeMuro mensajeMuro2 = MensajeMuroTestSamples.getMensajeMuroSample2();

        Assert.assertEquals(mensajeMuroTest, mensajeMuro1);
        Assert.assertNotEquals(mensajeMuroTest, mensajeMuro2);
    }
}
