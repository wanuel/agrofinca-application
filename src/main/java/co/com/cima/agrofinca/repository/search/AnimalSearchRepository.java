package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.Animal;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Animal} entity.
 */
public interface AnimalSearchRepository extends ElasticsearchRepository<Animal, Long> {
}
