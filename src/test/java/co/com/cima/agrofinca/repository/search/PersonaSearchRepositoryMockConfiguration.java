package co.com.cima.agrofinca.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PersonaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PersonaSearchRepositoryMockConfiguration {

    @MockBean
    private PersonaSearchRepository mockPersonaSearchRepository;

}
