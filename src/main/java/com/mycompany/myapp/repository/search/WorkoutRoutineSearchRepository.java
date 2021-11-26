package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.WorkoutRoutine;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link WorkoutRoutine} entity.
 */
public interface WorkoutRoutineSearchRepository
    extends ElasticsearchRepository<WorkoutRoutine, Long>, WorkoutRoutineSearchRepositoryInternal {}

interface WorkoutRoutineSearchRepositoryInternal {
    Stream<WorkoutRoutine> search(String query);
}

class WorkoutRoutineSearchRepositoryInternalImpl implements WorkoutRoutineSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    WorkoutRoutineSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<WorkoutRoutine> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, WorkoutRoutine.class).map(SearchHit::getContent).stream();
    }
}
