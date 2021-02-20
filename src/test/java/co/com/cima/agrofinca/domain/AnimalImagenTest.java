package co.com.cima.agrofinca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.com.cima.agrofinca.web.rest.TestUtil;

public class AnimalImagenTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnimalImagen.class);
        AnimalImagen animalImagen1 = new AnimalImagen();
        animalImagen1.setId(1L);
        AnimalImagen animalImagen2 = new AnimalImagen();
        animalImagen2.setId(animalImagen1.getId());
        assertThat(animalImagen1).isEqualTo(animalImagen2);
        animalImagen2.setId(2L);
        assertThat(animalImagen1).isNotEqualTo(animalImagen2);
        animalImagen1.setId(null);
        assertThat(animalImagen1).isNotEqualTo(animalImagen2);
    }
}
