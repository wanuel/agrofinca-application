package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.AnimalCostos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link AnimalCostos} entity.
 */
public interface AnimalCostosSearchRepository extends ElasticsearchRepository<AnimalCostos, Long> {
}
