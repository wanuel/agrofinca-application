package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.AnimalPeso;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link AnimalPeso} entity.
 */
public interface AnimalPesoSearchRepository extends ElasticsearchRepository<AnimalPeso, Long> {
}
