package co.com.cima.agrofinca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.com.cima.agrofinca.web.rest.TestUtil;

public class SocioTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Socio.class);
        Socio socio1 = new Socio();
        socio1.setId(1L);
        Socio socio2 = new Socio();
        socio2.setId(socio1.getId());
        assertThat(socio1).isEqualTo(socio2);
        socio2.setId(2L);
        assertThat(socio1).isNotEqualTo(socio2);
        socio1.setId(null);
        assertThat(socio1).isNotEqualTo(socio2);
    }
}
