package co.com.cima.agrofinca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.com.cima.agrofinca.web.rest.TestUtil;

public class PotreroPastoreoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PotreroPastoreo.class);
        PotreroPastoreo potreroPastoreo1 = new PotreroPastoreo();
        potreroPastoreo1.setId(1L);
        PotreroPastoreo potreroPastoreo2 = new PotreroPastoreo();
        potreroPastoreo2.setId(potreroPastoreo1.getId());
        assertThat(potreroPastoreo1).isEqualTo(potreroPastoreo2);
        potreroPastoreo2.setId(2L);
        assertThat(potreroPastoreo1).isNotEqualTo(potreroPastoreo2);
        potreroPastoreo1.setId(null);
        assertThat(potreroPastoreo1).isNotEqualTo(potreroPastoreo2);
    }
}
