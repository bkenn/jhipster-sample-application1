package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.WorkoutRoutineGroup;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link WorkoutRoutineGroup} entity.
 */
public interface WorkoutRoutineGroupSearchRepository
    extends ElasticsearchRepository<WorkoutRoutineGroup, Long>, WorkoutRoutineGroupSearchRepositoryInternal {}

interface WorkoutRoutineGroupSearchRepositoryInternal {
    Stream<WorkoutRoutineGroup> search(String query);
}

class WorkoutRoutineGroupSearchRepositoryInternalImpl implements WorkoutRoutineGroupSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    WorkoutRoutineGroupSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<WorkoutRoutineGroup> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, WorkoutRoutineGroup.class).map(SearchHit::getContent).stream();
    }
}
