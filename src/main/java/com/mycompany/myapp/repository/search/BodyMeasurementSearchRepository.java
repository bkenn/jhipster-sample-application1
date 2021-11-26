package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.BodyMeasurement;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link BodyMeasurement} entity.
 */
public interface BodyMeasurementSearchRepository
    extends ElasticsearchRepository<BodyMeasurement, Long>, BodyMeasurementSearchRepositoryInternal {}

interface BodyMeasurementSearchRepositoryInternal {
    Stream<BodyMeasurement> search(String query);
}

class BodyMeasurementSearchRepositoryInternalImpl implements BodyMeasurementSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    BodyMeasurementSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<BodyMeasurement> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, BodyMeasurement.class).map(SearchHit::getContent).stream();
    }
}
