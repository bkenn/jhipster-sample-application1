package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.ExerciseImage;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ExerciseImage} entity.
 */
public interface ExerciseImageSearchRepository
    extends ElasticsearchRepository<ExerciseImage, Long>, ExerciseImageSearchRepositoryInternal {}

interface ExerciseImageSearchRepositoryInternal {
    Stream<ExerciseImage> search(String query);
}

class ExerciseImageSearchRepositoryInternalImpl implements ExerciseImageSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ExerciseImageSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<ExerciseImage> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, ExerciseImage.class).map(SearchHit::getContent).stream();
    }
}
