package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.WorkoutExercise;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link WorkoutExercise} entity.
 */
public interface WorkoutExerciseSearchRepository
    extends ElasticsearchRepository<WorkoutExercise, Long>, WorkoutExerciseSearchRepositoryInternal {}

interface WorkoutExerciseSearchRepositoryInternal {
    Stream<WorkoutExercise> search(String query);
}

class WorkoutExerciseSearchRepositoryInternalImpl implements WorkoutExerciseSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    WorkoutExerciseSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<WorkoutExercise> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, WorkoutExercise.class).map(SearchHit::getContent).stream();
    }
}
