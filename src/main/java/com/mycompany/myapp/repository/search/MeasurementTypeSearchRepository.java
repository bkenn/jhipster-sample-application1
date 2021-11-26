package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.MeasurementType;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MeasurementType} entity.
 */
public interface MeasurementTypeSearchRepository
    extends ElasticsearchRepository<MeasurementType, Long>, MeasurementTypeSearchRepositoryInternal {}

interface MeasurementTypeSearchRepositoryInternal {
    Stream<MeasurementType> search(String query);
}

class MeasurementTypeSearchRepositoryInternalImpl implements MeasurementTypeSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    MeasurementTypeSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<MeasurementType> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, MeasurementType.class).map(SearchHit::getContent).stream();
    }
}
