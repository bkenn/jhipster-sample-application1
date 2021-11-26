package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.ExerciseCategory;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ExerciseCategory} entity.
 */
public interface ExerciseCategorySearchRepository
    extends ElasticsearchRepository<ExerciseCategory, Long>, ExerciseCategorySearchRepositoryInternal {}

interface ExerciseCategorySearchRepositoryInternal {
    Stream<ExerciseCategory> search(String query);
}

class ExerciseCategorySearchRepositoryInternalImpl implements ExerciseCategorySearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ExerciseCategorySearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<ExerciseCategory> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, ExerciseCategory.class).map(SearchHit::getContent).stream();
    }
}
