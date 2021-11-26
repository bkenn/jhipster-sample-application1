package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Muscle;
import com.mycompany.myapp.repository.MuscleRepository;
import com.mycompany.myapp.repository.search.MuscleSearchRepository;
import com.mycompany.myapp.service.MuscleService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Muscle}.
 */
@Service
@Transactional
public class MuscleServiceImpl implements MuscleService {

    private final Logger log = LoggerFactory.getLogger(MuscleServiceImpl.class);

    private final MuscleRepository muscleRepository;

    private final MuscleSearchRepository muscleSearchRepository;

    public MuscleServiceImpl(MuscleRepository muscleRepository, MuscleSearchRepository muscleSearchRepository) {
        this.muscleRepository = muscleRepository;
        this.muscleSearchRepository = muscleSearchRepository;
    }

    @Override
    public Muscle save(Muscle muscle) {
        log.debug("Request to save Muscle : {}", muscle);
        Muscle result = muscleRepository.save(muscle);
        muscleSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Muscle> partialUpdate(Muscle muscle) {
        log.debug("Request to partially update Muscle : {}", muscle);

        return muscleRepository
            .findById(muscle.getId())
            .map(existingMuscle -> {
                if (muscle.getName() != null) {
                    existingMuscle.setName(muscle.getName());
                }
                if (muscle.getDescription() != null) {
                    existingMuscle.setDescription(muscle.getDescription());
                }
                if (muscle.getMuscleOrder() != null) {
                    existingMuscle.setMuscleOrder(muscle.getMuscleOrder());
                }
                if (muscle.getImageUrlMain() != null) {
                    existingMuscle.setImageUrlMain(muscle.getImageUrlMain());
                }
                if (muscle.getImageUrlSecondary() != null) {
                    existingMuscle.setImageUrlSecondary(muscle.getImageUrlSecondary());
                }
                if (muscle.getFront() != null) {
                    existingMuscle.setFront(muscle.getFront());
                }

                return existingMuscle;
            })
            .map(muscleRepository::save)
            .map(savedMuscle -> {
                muscleSearchRepository.save(savedMuscle);

                return savedMuscle;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Muscle> findAll() {
        log.debug("Request to get all Muscles");
        return muscleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Muscle> findOne(Long id) {
        log.debug("Request to get Muscle : {}", id);
        return muscleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Muscle : {}", id);
        muscleRepository.deleteById(id);
        muscleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Muscle> search(String query) {
        log.debug("Request to search Muscles for query {}", query);
        return StreamSupport.stream(muscleSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
