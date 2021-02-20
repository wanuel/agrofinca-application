package co.com.cima.agrofinca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.com.cima.agrofinca.web.rest.TestUtil;

public class AnimalSaludTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnimalSalud.class);
        AnimalSalud animalSalud1 = new AnimalSalud();
        animalSalud1.setId(1L);
        AnimalSalud animalSalud2 = new AnimalSalud();
        animalSalud2.setId(animalSalud1.getId());
        assertThat(animalSalud1).isEqualTo(animalSalud2);
        animalSalud2.setId(2L);
        assertThat(animalSalud1).isNotEqualTo(animalSalud2);
        animalSalud1.setId(null);
        assertThat(animalSalud1).isNotEqualTo(animalSalud2);
    }
}
