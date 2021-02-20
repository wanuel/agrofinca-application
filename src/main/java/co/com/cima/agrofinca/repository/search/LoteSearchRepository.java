package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.Lote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Lote} entity.
 */
public interface LoteSearchRepository extends ElasticsearchRepository<Lote, Long> {
}
