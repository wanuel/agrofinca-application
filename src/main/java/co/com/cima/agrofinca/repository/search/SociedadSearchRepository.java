package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.Sociedad;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Sociedad} entity.
 */
public interface SociedadSearchRepository extends ElasticsearchRepository<Sociedad, Long> {
}
