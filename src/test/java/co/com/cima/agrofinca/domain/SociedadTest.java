package co.com.cima.agrofinca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.com.cima.agrofinca.web.rest.TestUtil;

public class SociedadTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sociedad.class);
        Sociedad sociedad1 = new Sociedad();
        sociedad1.setId(1L);
        Sociedad sociedad2 = new Sociedad();
        sociedad2.setId(sociedad1.getId());
        assertThat(sociedad1).isEqualTo(sociedad2);
        sociedad2.setId(2L);
        assertThat(sociedad1).isNotEqualTo(sociedad2);
        sociedad1.setId(null);
        assertThat(sociedad1).isNotEqualTo(sociedad2);
    }
}
