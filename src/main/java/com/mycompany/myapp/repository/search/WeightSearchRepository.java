package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.Weight;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Weight} entity.
 */
public interface WeightSearchRepository extends ElasticsearchRepository<Weight, Long>, WeightSearchRepositoryInternal {}

interface WeightSearchRepositoryInternal {
    Stream<Weight> search(String query);
}

class WeightSearchRepositoryInternalImpl implements WeightSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    WeightSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Weight> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Weight.class).map(SearchHit::getContent).stream();
    }
}
