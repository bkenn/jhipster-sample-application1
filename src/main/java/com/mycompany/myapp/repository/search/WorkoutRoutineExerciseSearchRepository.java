package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.WorkoutRoutineExercise;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link WorkoutRoutineExercise} entity.
 */
public interface WorkoutRoutineExerciseSearchRepository
    extends ElasticsearchRepository<WorkoutRoutineExercise, Long>, WorkoutRoutineExerciseSearchRepositoryInternal {}

interface WorkoutRoutineExerciseSearchRepositoryInternal {
    Stream<WorkoutRoutineExercise> search(String query);
}

class WorkoutRoutineExerciseSearchRepositoryInternalImpl implements WorkoutRoutineExerciseSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    WorkoutRoutineExerciseSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<WorkoutRoutineExercise> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, WorkoutRoutineExercise.class).map(SearchHit::getContent).stream();
    }
}
