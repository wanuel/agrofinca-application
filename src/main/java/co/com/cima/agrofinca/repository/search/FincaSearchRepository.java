package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.Finca;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Finca} entity.
 */
public interface FincaSearchRepository extends ElasticsearchRepository<Finca, Long> {
}
