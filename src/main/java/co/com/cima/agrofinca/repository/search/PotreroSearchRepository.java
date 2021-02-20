package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.Potrero;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Potrero} entity.
 */
public interface PotreroSearchRepository extends ElasticsearchRepository<Potrero, Long> {
}
