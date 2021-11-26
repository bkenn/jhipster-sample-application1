package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.Muscle;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Muscle} entity.
 */
public interface MuscleSearchRepository extends ElasticsearchRepository<Muscle, Long>, MuscleSearchRepositoryInternal {}

interface MuscleSearchRepositoryInternal {
    Stream<Muscle> search(String query);
}

class MuscleSearchRepositoryInternalImpl implements MuscleSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    MuscleSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Muscle> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Muscle.class).map(SearchHit::getContent).stream();
    }
}
