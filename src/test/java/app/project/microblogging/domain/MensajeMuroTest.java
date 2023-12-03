package app.project.microblogging.domain;

import static app.project.microblogging.domain.ApplicationUserTestSamples.*;
import static app.project.microblogging.domain.MensajeMuroTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import app.project.microblogging.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MensajeMuroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MensajeMuro.class);
        MensajeMuro mensajeMuro1 = getMensajeMuroSample1();
        MensajeMuro mensajeMuro2 = new MensajeMuro();
        assertThat(mensajeMuro1).isNotEqualTo(mensajeMuro2);

        mensajeMuro2.setId(mensajeMuro1.getId());
        assertThat(mensajeMuro1).isEqualTo(mensajeMuro2);

        mensajeMuro2 = getMensajeMuroSample2();
        assertThat(mensajeMuro1).isNotEqualTo(mensajeMuro2);
    }

    @Test
    void userTest() throws Exception {
        MensajeMuro mensajeMuro = getMensajeMuroRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        mensajeMuro.setUser(applicationUserBack);
        assertThat(mensajeMuro.getUser()).isEqualTo(applicationUserBack);

        mensajeMuro.user(null);
        assertThat(mensajeMuro.getUser()).isNull();
    }
}
