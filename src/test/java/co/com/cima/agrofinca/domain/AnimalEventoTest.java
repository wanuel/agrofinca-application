package co.com.cima.agrofinca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.com.cima.agrofinca.web.rest.TestUtil;

public class AnimalEventoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnimalEvento.class);
        AnimalEvento animalEvento1 = new AnimalEvento();
        animalEvento1.setId(1L);
        AnimalEvento animalEvento2 = new AnimalEvento();
        animalEvento2.setId(animalEvento1.getId());
        assertThat(animalEvento1).isEqualTo(animalEvento2);
        animalEvento2.setId(2L);
        assertThat(animalEvento1).isNotEqualTo(animalEvento2);
        animalEvento1.setId(null);
        assertThat(animalEvento1).isNotEqualTo(animalEvento2);
    }
}
