package app.project.microblogging.domain;

import static app.project.microblogging.domain.ApplicationUserTestSamples.*;
import static app.project.microblogging.domain.ApplicationUserTestSamples.*;
import static app.project.microblogging.domain.MensajeMuroTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import app.project.microblogging.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = getApplicationUserSample1();
        ApplicationUser applicationUser2 = new ApplicationUser();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);

        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);

        applicationUser2 = getApplicationUserSample2();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }

    @Test
    void mensajeTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        MensajeMuro mensajeMuroBack = getMensajeMuroRandomSampleGenerator();

        applicationUser.addMensaje(mensajeMuroBack);
        assertThat(applicationUser.getMensajes()).containsOnly(mensajeMuroBack);
        assertThat(mensajeMuroBack.getUser()).isEqualTo(applicationUser);

        applicationUser.removeMensaje(mensajeMuroBack);
        assertThat(applicationUser.getMensajes()).doesNotContain(mensajeMuroBack);
        assertThat(mensajeMuroBack.getUser()).isNull();

        applicationUser.mensajes(new HashSet<>(Set.of(mensajeMuroBack)));
        assertThat(applicationUser.getMensajes()).containsOnly(mensajeMuroBack);
        assertThat(mensajeMuroBack.getUser()).isEqualTo(applicationUser);

        applicationUser.setMensajes(new HashSet<>());
        assertThat(applicationUser.getMensajes()).doesNotContain(mensajeMuroBack);
        assertThat(mensajeMuroBack.getUser()).isNull();
    }

    @Test
    void contactoTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        applicationUser.addContacto(applicationUserBack);
        assertThat(applicationUser.getContactos()).containsOnly(applicationUserBack);

        applicationUser.removeContacto(applicationUserBack);
        assertThat(applicationUser.getContactos()).doesNotContain(applicationUserBack);

        applicationUser.contactos(new HashSet<>(Set.of(applicationUserBack)));
        assertThat(applicationUser.getContactos()).containsOnly(applicationUserBack);

        applicationUser.setContactos(new HashSet<>());
        assertThat(applicationUser.getContactos()).doesNotContain(applicationUserBack);
    }

    @Test
    void seguidoTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        applicationUser.addSeguido(applicationUserBack);
        assertThat(applicationUser.getSeguidos()).containsOnly(applicationUserBack);

        applicationUser.removeSeguido(applicationUserBack);
        assertThat(applicationUser.getSeguidos()).doesNotContain(applicationUserBack);

        applicationUser.seguidos(new HashSet<>(Set.of(applicationUserBack)));
        assertThat(applicationUser.getSeguidos()).containsOnly(applicationUserBack);

        applicationUser.setSeguidos(new HashSet<>());
        assertThat(applicationUser.getSeguidos()).doesNotContain(applicationUserBack);
    }

    @Test
    void userCTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        applicationUser.addUserC(applicationUserBack);
        assertThat(applicationUser.getUserCS()).containsOnly(applicationUserBack);
        assertThat(applicationUserBack.getContactos()).containsOnly(applicationUser);

        applicationUser.removeUserC(applicationUserBack);
        assertThat(applicationUser.getUserCS()).doesNotContain(applicationUserBack);
        assertThat(applicationUserBack.getContactos()).doesNotContain(applicationUser);

        applicationUser.userCS(new HashSet<>(Set.of(applicationUserBack)));
        assertThat(applicationUser.getUserCS()).containsOnly(applicationUserBack);
        assertThat(applicationUserBack.getContactos()).containsOnly(applicationUser);

        applicationUser.setUserCS(new HashSet<>());
        assertThat(applicationUser.getUserCS()).doesNotContain(applicationUserBack);
        assertThat(applicationUserBack.getContactos()).doesNotContain(applicationUser);
    }

    @Test
    void userSTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        applicationUser.addUserS(applicationUserBack);
        assertThat(applicationUser.getUserS()).containsOnly(applicationUserBack);
        assertThat(applicationUserBack.getSeguidos()).containsOnly(applicationUser);

        applicationUser.removeUserS(applicationUserBack);
        assertThat(applicationUser.getUserS()).doesNotContain(applicationUserBack);
        assertThat(applicationUserBack.getSeguidos()).doesNotContain(applicationUser);

        applicationUser.userS(new HashSet<>(Set.of(applicationUserBack)));
        assertThat(applicationUser.getUserS()).containsOnly(applicationUserBack);
        assertThat(applicationUserBack.getSeguidos()).containsOnly(applicationUser);

        applicationUser.setUserS(new HashSet<>());
        assertThat(applicationUser.getUserS()).doesNotContain(applicationUserBack);
        assertThat(applicationUserBack.getSeguidos()).doesNotContain(applicationUser);
    }
}
