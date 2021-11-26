package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.ProgressPhoto;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ProgressPhoto} entity.
 */
public interface ProgressPhotoSearchRepository
    extends ElasticsearchRepository<ProgressPhoto, Long>, ProgressPhotoSearchRepositoryInternal {}

interface ProgressPhotoSearchRepositoryInternal {
    Stream<ProgressPhoto> search(String query);
}

class ProgressPhotoSearchRepositoryInternalImpl implements ProgressPhotoSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ProgressPhotoSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<ProgressPhoto> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, ProgressPhoto.class).map(SearchHit::getContent).stream();
    }
}
