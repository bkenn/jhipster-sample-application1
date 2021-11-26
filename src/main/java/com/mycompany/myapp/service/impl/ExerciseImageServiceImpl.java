package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.ExerciseImage;
import com.mycompany.myapp.repository.ExerciseImageRepository;
import com.mycompany.myapp.repository.search.ExerciseImageSearchRepository;
import com.mycompany.myapp.service.ExerciseImageService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExerciseImage}.
 */
@Service
@Transactional
public class ExerciseImageServiceImpl implements ExerciseImageService {

    private final Logger log = LoggerFactory.getLogger(ExerciseImageServiceImpl.class);

    private final ExerciseImageRepository exerciseImageRepository;

    private final ExerciseImageSearchRepository exerciseImageSearchRepository;

    public ExerciseImageServiceImpl(
        ExerciseImageRepository exerciseImageRepository,
        ExerciseImageSearchRepository exerciseImageSearchRepository
    ) {
        this.exerciseImageRepository = exerciseImageRepository;
        this.exerciseImageSearchRepository = exerciseImageSearchRepository;
    }

    @Override
    public ExerciseImage save(ExerciseImage exerciseImage) {
        log.debug("Request to save ExerciseImage : {}", exerciseImage);
        ExerciseImage result = exerciseImageRepository.save(exerciseImage);
        exerciseImageSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<ExerciseImage> partialUpdate(ExerciseImage exerciseImage) {
        log.debug("Request to partially update ExerciseImage : {}", exerciseImage);

        return exerciseImageRepository
            .findById(exerciseImage.getId())
            .map(existingExerciseImage -> {
                if (exerciseImage.getUuid() != null) {
                    existingExerciseImage.setUuid(exerciseImage.getUuid());
                }
                if (exerciseImage.getImage() != null) {
                    existingExerciseImage.setImage(exerciseImage.getImage());
                }
                if (exerciseImage.getImageContentType() != null) {
                    existingExerciseImage.setImageContentType(exerciseImage.getImageContentType());
                }
                if (exerciseImage.getMain() != null) {
                    existingExerciseImage.setMain(exerciseImage.getMain());
                }

                return existingExerciseImage;
            })
            .map(exerciseImageRepository::save)
            .map(savedExerciseImage -> {
                exerciseImageSearchRepository.save(savedExerciseImage);

                return savedExerciseImage;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseImage> findAll() {
        log.debug("Request to get all ExerciseImages");
        return exerciseImageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseImage> findOne(Long id) {
        log.debug("Request to get ExerciseImage : {}", id);
        return exerciseImageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseImage : {}", id);
        exerciseImageRepository.deleteById(id);
        exerciseImageSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseImage> search(String query) {
        log.debug("Request to search ExerciseImages for query {}", query);
        return StreamSupport.stream(exerciseImageSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
