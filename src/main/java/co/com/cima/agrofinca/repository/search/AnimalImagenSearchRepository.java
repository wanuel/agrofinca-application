package co.com.cima.agrofinca.repository.search;

import co.com.cima.agrofinca.domain.AnimalImagen;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link AnimalImagen} entity.
 */
public interface AnimalImagenSearchRepository extends ElasticsearchRepository<AnimalImagen, Long> {
}
