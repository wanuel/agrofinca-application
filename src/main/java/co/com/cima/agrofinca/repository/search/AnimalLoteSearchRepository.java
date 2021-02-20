package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.AnimalLote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link AnimalLote} entity.
 */
public interface AnimalLoteSearchRepository extends ElasticsearchRepository<AnimalLote, Long> {
}
