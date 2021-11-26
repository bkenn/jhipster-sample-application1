package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.Exercise;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Exercise} entity.
 */
public interface ExerciseSearchRepository extends ElasticsearchRepository<Exercise, Long>, ExerciseSearchRepositoryInternal {}

interface ExerciseSearchRepositoryInternal {
    Stream<Exercise> search(String query);
}

class ExerciseSearchRepositoryInternalImpl implements ExerciseSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ExerciseSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Exercise> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Exercise.class).map(SearchHit::getContent).stream();
    }
}
