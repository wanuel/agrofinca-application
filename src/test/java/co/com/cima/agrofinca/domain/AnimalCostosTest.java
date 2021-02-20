package co.com.cima.agrofinca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.com.cima.agrofinca.web.rest.TestUtil;

public class AnimalCostosTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnimalCostos.class);
        AnimalCostos animalCostos1 = new AnimalCostos();
        animalCostos1.setId(1L);
        AnimalCostos animalCostos2 = new AnimalCostos();
        animalCostos2.setId(animalCostos1.getId());
        assertThat(animalCostos1).isEqualTo(animalCostos2);
        animalCostos2.setId(2L);
        assertThat(animalCostos1).isNotEqualTo(animalCostos2);
        animalCostos1.setId(null);
        assertThat(animalCostos1).isNotEqualTo(animalCostos2);
    }
}
