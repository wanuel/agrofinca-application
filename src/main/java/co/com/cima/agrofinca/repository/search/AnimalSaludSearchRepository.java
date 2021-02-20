package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.AnimalSalud;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link AnimalSalud} entity.
 */
public interface AnimalSaludSearchRepository extends ElasticsearchRepository<AnimalSalud, Long> {
}
