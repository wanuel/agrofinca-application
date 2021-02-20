package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.Persona;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Persona} entity.
 */
public interface PersonaSearchRepository extends ElasticsearchRepository<Persona, Long> {
}
