package co.com.cima.agrofinca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.com.cima.agrofinca.web.rest.TestUtil;

public class AnimalLoteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnimalLote.class);
        AnimalLote animalLote1 = new AnimalLote();
        animalLote1.setId(1L);
        AnimalLote animalLote2 = new AnimalLote();
        animalLote2.setId(animalLote1.getId());
        assertThat(animalLote1).isEqualTo(animalLote2);
        animalLote2.setId(2L);
        assertThat(animalLote1).isNotEqualTo(animalLote2);
        animalLote1.setId(null);
        assertThat(animalLote1).isNotEqualTo(animalLote2);
    }
}
