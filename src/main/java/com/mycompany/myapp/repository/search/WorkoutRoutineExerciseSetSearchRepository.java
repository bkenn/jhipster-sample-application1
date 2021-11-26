package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.WorkoutRoutineExerciseSet;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link WorkoutRoutineExerciseSet} entity.
 */
public interface WorkoutRoutineExerciseSetSearchRepository
    extends ElasticsearchRepository<WorkoutRoutineExerciseSet, Long>, WorkoutRoutineExerciseSetSearchRepositoryInternal {}

interface WorkoutRoutineExerciseSetSearchRepositoryInternal {
    Stream<WorkoutRoutineExerciseSet> search(String query);
}

class WorkoutRoutineExerciseSetSearchRepositoryInternalImpl implements WorkoutRoutineExerciseSetSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    WorkoutRoutineExerciseSetSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<WorkoutRoutineExerciseSet> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, WorkoutRoutineExerciseSet.class).map(SearchHit::getContent).stream();
    }
}
