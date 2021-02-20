package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.Parametros;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Parametros} entity.
 */
public interface ParametrosSearchRepository extends ElasticsearchRepository<Parametros, Long> {
}
