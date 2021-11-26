package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.WorkoutExerciseSet;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link WorkoutExerciseSet} entity.
 */
public interface WorkoutExerciseSetSearchRepository
    extends ElasticsearchRepository<WorkoutExerciseSet, Long>, WorkoutExerciseSetSearchRepositoryInternal {}

interface WorkoutExerciseSetSearchRepositoryInternal {
    Stream<WorkoutExerciseSet> search(String query);
}

class WorkoutExerciseSetSearchRepositoryInternalImpl implements WorkoutExerciseSetSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    WorkoutExerciseSetSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<WorkoutExerciseSet> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, WorkoutExerciseSet.class).map(SearchHit::getContent).stream();
    }
}
