package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Weight;
import com.mycompany.myapp.repository.WeightRepository;
import com.mycompany.myapp.repository.search.WeightSearchRepository;
import com.mycompany.myapp.service.WeightService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Weight}.
 */
@Service
@Transactional
public class WeightServiceImpl implements WeightService {

    private final Logger log = LoggerFactory.getLogger(WeightServiceImpl.class);

    private final WeightRepository weightRepository;

    private final WeightSearchRepository weightSearchRepository;

    public WeightServiceImpl(WeightRepository weightRepository, WeightSearchRepository weightSearchRepository) {
        this.weightRepository = weightRepository;
        this.weightSearchRepository = weightSearchRepository;
    }

    @Override
    public Weight save(Weight weight) {
        log.debug("Request to save Weight : {}", weight);
        Weight result = weightRepository.save(weight);
        weightSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Weight> partialUpdate(Weight weight) {
        log.debug("Request to partially update Weight : {}", weight);

        return weightRepository
            .findById(weight.getId())
            .map(existingWeight -> {
                if (weight.getValue() != null) {
                    existingWeight.setValue(weight.getValue());
                }
                if (weight.getWeightDateTime() != null) {
                    existingWeight.setWeightDateTime(weight.getWeightDateTime());
                }

                return existingWeight;
            })
            .map(weightRepository::save)
            .map(savedWeight -> {
                weightSearchRepository.save(savedWeight);

                return savedWeight;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Weight> findAll() {
        log.debug("Request to get all Weights");
        return weightRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Weight> findOne(Long id) {
        log.debug("Request to get Weight : {}", id);
        return weightRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Weight : {}", id);
        weightRepository.deleteById(id);
        weightSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Weight> search(String query) {
        log.debug("Request to search Weights for query {}", query);
        return StreamSupport.stream(weightSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
