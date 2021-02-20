package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.AnimalEvento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link AnimalEvento} entity.
 */
public interface AnimalEventoSearchRepository extends ElasticsearchRepository<AnimalEvento, Long> {
}
