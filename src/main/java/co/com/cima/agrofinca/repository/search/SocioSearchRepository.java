package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.Socio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Socio} entity.
 */
public interface SocioSearchRepository extends ElasticsearchRepository<Socio, Long> {
}
