package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.MeasurementType;
import com.mycompany.myapp.repository.MeasurementTypeRepository;
import com.mycompany.myapp.repository.search.MeasurementTypeSearchRepository;
import com.mycompany.myapp.service.MeasurementTypeService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MeasurementType}.
 */
@Service
@Transactional
public class MeasurementTypeServiceImpl implements MeasurementTypeService {

    private final Logger log = LoggerFactory.getLogger(MeasurementTypeServiceImpl.class);

    private final MeasurementTypeRepository measurementTypeRepository;

    private final MeasurementTypeSearchRepository measurementTypeSearchRepository;

    public MeasurementTypeServiceImpl(
        MeasurementTypeRepository measurementTypeRepository,
        MeasurementTypeSearchRepository measurementTypeSearchRepository
    ) {
        this.measurementTypeRepository = measurementTypeRepository;
        this.measurementTypeSearchRepository = measurementTypeSearchRepository;
    }

    @Override
    public MeasurementType save(MeasurementType measurementType) {
        log.debug("Request to save MeasurementType : {}", measurementType);
        MeasurementType result = measurementTypeRepository.save(measurementType);
        measurementTypeSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<MeasurementType> partialUpdate(MeasurementType measurementType) {
        log.debug("Request to partially update MeasurementType : {}", measurementType);

        return measurementTypeRepository
            .findById(measurementType.getId())
            .map(existingMeasurementType -> {
                if (measurementType.getName() != null) {
                    existingMeasurementType.setName(measurementType.getName());
                }
                if (measurementType.getDescription() != null) {
                    existingMeasurementType.setDescription(measurementType.getDescription());
                }
                if (measurementType.getMeasurementOrder() != null) {
                    existingMeasurementType.setMeasurementOrder(measurementType.getMeasurementOrder());
                }
                if (measurementType.getMeasurementUnit() != null) {
                    existingMeasurementType.setMeasurementUnit(measurementType.getMeasurementUnit());
                }

                return existingMeasurementType;
            })
            .map(measurementTypeRepository::save)
            .map(savedMeasurementType -> {
                measurementTypeSearchRepository.save(savedMeasurementType);

                return savedMeasurementType;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeasurementType> findAll() {
        log.debug("Request to get all MeasurementTypes");
        return measurementTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MeasurementType> findOne(Long id) {
        log.debug("Request to get MeasurementType : {}", id);
        return measurementTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MeasurementType : {}", id);
        measurementTypeRepository.deleteById(id);
        measurementTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeasurementType> search(String query) {
        log.debug("Request to search MeasurementTypes for query {}", query);
        return StreamSupport.stream(measurementTypeSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
