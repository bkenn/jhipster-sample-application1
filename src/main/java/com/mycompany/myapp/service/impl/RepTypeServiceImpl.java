package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.RepType;
import com.mycompany.myapp.repository.RepTypeRepository;
import com.mycompany.myapp.repository.search.RepTypeSearchRepository;
import com.mycompany.myapp.service.RepTypeService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RepType}.
 */
@Service
@Transactional
public class RepTypeServiceImpl implements RepTypeService {

    private final Logger log = LoggerFactory.getLogger(RepTypeServiceImpl.class);

    private final RepTypeRepository repTypeRepository;

    private final RepTypeSearchRepository repTypeSearchRepository;

    public RepTypeServiceImpl(RepTypeRepository repTypeRepository, RepTypeSearchRepository repTypeSearchRepository) {
        this.repTypeRepository = repTypeRepository;
        this.repTypeSearchRepository = repTypeSearchRepository;
    }

    @Override
    public RepType save(RepType repType) {
        log.debug("Request to save RepType : {}", repType);
        RepType result = repTypeRepository.save(repType);
        repTypeSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<RepType> partialUpdate(RepType repType) {
        log.debug("Request to partially update RepType : {}", repType);

        return repTypeRepository
            .findById(repType.getId())
            .map(existingRepType -> {
                if (repType.getName() != null) {
                    existingRepType.setName(repType.getName());
                }
                if (repType.getDisplay() != null) {
                    existingRepType.setDisplay(repType.getDisplay());
                }

                return existingRepType;
            })
            .map(repTypeRepository::save)
            .map(savedRepType -> {
                repTypeSearchRepository.save(savedRepType);

                return savedRepType;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepType> findAll() {
        log.debug("Request to get all RepTypes");
        return repTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RepType> findOne(Long id) {
        log.debug("Request to get RepType : {}", id);
        return repTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RepType : {}", id);
        repTypeRepository.deleteById(id);
        repTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepType> search(String query) {
        log.debug("Request to search RepTypes for query {}", query);
        return StreamSupport.stream(repTypeSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
