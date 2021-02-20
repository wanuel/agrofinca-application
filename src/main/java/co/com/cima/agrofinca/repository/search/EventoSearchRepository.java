package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.Evento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Evento} entity.
 */
public interface EventoSearchRepository extends ElasticsearchRepository<Evento, Long> {
}
