package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.BodyMeasurement;
import com.mycompany.myapp.repository.BodyMeasurementRepository;
import com.mycompany.myapp.repository.search.BodyMeasurementSearchRepository;
import com.mycompany.myapp.service.BodyMeasurementService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BodyMeasurement}.
 */
@Service
@Transactional
public class BodyMeasurementServiceImpl implements BodyMeasurementService {

    private final Logger log = LoggerFactory.getLogger(BodyMeasurementServiceImpl.class);

    private final BodyMeasurementRepository bodyMeasurementRepository;

    private final BodyMeasurementSearchRepository bodyMeasurementSearchRepository;

    public BodyMeasurementServiceImpl(
        BodyMeasurementRepository bodyMeasurementRepository,
        BodyMeasurementSearchRepository bodyMeasurementSearchRepository
    ) {
        this.bodyMeasurementRepository = bodyMeasurementRepository;
        this.bodyMeasurementSearchRepository = bodyMeasurementSearchRepository;
    }

    @Override
    public BodyMeasurement save(BodyMeasurement bodyMeasurement) {
        log.debug("Request to save BodyMeasurement : {}", bodyMeasurement);
        BodyMeasurement result = bodyMeasurementRepository.save(bodyMeasurement);
        bodyMeasurementSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<BodyMeasurement> partialUpdate(BodyMeasurement bodyMeasurement) {
        log.debug("Request to partially update BodyMeasurement : {}", bodyMeasurement);

        return bodyMeasurementRepository
            .findById(bodyMeasurement.getId())
            .map(existingBodyMeasurement -> {
                if (bodyMeasurement.getValue() != null) {
                    existingBodyMeasurement.setValue(bodyMeasurement.getValue());
                }
                if (bodyMeasurement.getBodyMeasurementDateTime() != null) {
                    existingBodyMeasurement.setBodyMeasurementDateTime(bodyMeasurement.getBodyMeasurementDateTime());
                }

                return existingBodyMeasurement;
            })
            .map(bodyMeasurementRepository::save)
            .map(savedBodyMeasurement -> {
                bodyMeasurementSearchRepository.save(savedBodyMeasurement);

                return savedBodyMeasurement;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<BodyMeasurement> findAll() {
        log.debug("Request to get all BodyMeasurements");
        return bodyMeasurementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BodyMeasurement> findOne(Long id) {
        log.debug("Request to get BodyMeasurement : {}", id);
        return bodyMeasurementRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BodyMeasurement : {}", id);
        bodyMeasurementRepository.deleteById(id);
        bodyMeasurementSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BodyMeasurement> search(String query) {
        log.debug("Request to search BodyMeasurements for query {}", query);
        return StreamSupport.stream(bodyMeasurementSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
