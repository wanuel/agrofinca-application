package co.com.cima.agrofinca.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, co.com.cima.agrofinca.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, co.com.cima.agrofinca.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, co.com.cima.agrofinca.domain.User.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Authority.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.User.class.getName() + ".authorities");
            createCache(cm, co.com.cima.agrofinca.domain.Finca.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Finca.class.getName() + ".potreros");
            createCache(cm, co.com.cima.agrofinca.domain.Potrero.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Potrero.class.getName() + ".pastoreos");
            createCache(cm, co.com.cima.agrofinca.domain.Lote.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Lote.class.getName() + ".pastoreos");
            createCache(cm, co.com.cima.agrofinca.domain.Lote.class.getName() + ".animales");
            createCache(cm, co.com.cima.agrofinca.domain.PotreroPastoreo.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Animal.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Animal.class.getName() + ".lotes");
            createCache(cm, co.com.cima.agrofinca.domain.Animal.class.getName() + ".imagenes");
            createCache(cm, co.com.cima.agrofinca.domain.Animal.class.getName() + ".eventos");
            createCache(cm, co.com.cima.agrofinca.domain.AnimalLote.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Parametros.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Parametros.class.getName() + ".eventos");
            createCache(cm, co.com.cima.agrofinca.domain.Parametros.class.getName() + ".medicamentos");
            createCache(cm, co.com.cima.agrofinca.domain.Parametros.class.getName() + ".parametros");
            createCache(cm, co.com.cima.agrofinca.domain.Parametros.class.getName() + ".tipoLotes");
            createCache(cm, co.com.cima.agrofinca.domain.Parametros.class.getName() + ".tipos");
            createCache(cm, co.com.cima.agrofinca.domain.Parametros.class.getName() + ".razas");
            createCache(cm, co.com.cima.agrofinca.domain.AnimalCostos.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.AnimalEvento.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.AnimalEvento.class.getName() + ".tratamientos");
            createCache(cm, co.com.cima.agrofinca.domain.AnimalEvento.class.getName() + ".pesos");
            createCache(cm, co.com.cima.agrofinca.domain.AnimalEvento.class.getName() + ".costos");
            createCache(cm, co.com.cima.agrofinca.domain.AnimalPeso.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.AnimalImagen.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.AnimalSalud.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Evento.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Evento.class.getName() + ".eventos");
            createCache(cm, co.com.cima.agrofinca.domain.Persona.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Sociedad.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Socio.class.getName());
            createCache(cm, co.com.cima.agrofinca.domain.Socio.class.getName() + ".personas");
            createCache(cm, co.com.cima.agrofinca.domain.Socio.class.getName() + ".sociedades");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
