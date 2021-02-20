package co.com.cima.agrofinca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.com.cima.agrofinca.web.rest.TestUtil;

public class PotreroTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Potrero.class);
        Potrero potrero1 = new Potrero();
        potrero1.setId(1L);
        Potrero potrero2 = new Potrero();
        potrero2.setId(potrero1.getId());
        assertThat(potrero1).isEqualTo(potrero2);
        potrero2.setId(2L);
        assertThat(potrero1).isNotEqualTo(potrero2);
        potrero1.setId(null);
        assertThat(potrero1).isNotEqualTo(potrero2);
    }
}
