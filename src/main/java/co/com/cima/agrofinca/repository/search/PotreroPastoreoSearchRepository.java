package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.PotreroPastoreo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link PotreroPastoreo} entity.
 */
public interface PotreroPastoreoSearchRepository extends ElasticsearchRepository<PotreroPastoreo, Long> {
}
