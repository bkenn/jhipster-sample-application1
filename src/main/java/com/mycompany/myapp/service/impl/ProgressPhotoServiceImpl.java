package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.ProgressPhoto;
import com.mycompany.myapp.repository.ProgressPhotoRepository;
import com.mycompany.myapp.repository.search.ProgressPhotoSearchRepository;
import com.mycompany.myapp.service.ProgressPhotoService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProgressPhoto}.
 */
@Service
@Transactional
public class ProgressPhotoServiceImpl implements ProgressPhotoService {

    private final Logger log = LoggerFactory.getLogger(ProgressPhotoServiceImpl.class);

    private final ProgressPhotoRepository progressPhotoRepository;

    private final ProgressPhotoSearchRepository progressPhotoSearchRepository;

    public ProgressPhotoServiceImpl(
        ProgressPhotoRepository progressPhotoRepository,
        ProgressPhotoSearchRepository progressPhotoSearchRepository
    ) {
        this.progressPhotoRepository = progressPhotoRepository;
        this.progressPhotoSearchRepository = progressPhotoSearchRepository;
    }

    @Override
    public ProgressPhoto save(ProgressPhoto progressPhoto) {
        log.debug("Request to save ProgressPhoto : {}", progressPhoto);
        ProgressPhoto result = progressPhotoRepository.save(progressPhoto);
        progressPhotoSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<ProgressPhoto> partialUpdate(ProgressPhoto progressPhoto) {
        log.debug("Request to partially update ProgressPhoto : {}", progressPhoto);

        return progressPhotoRepository
            .findById(progressPhoto.getId())
            .map(existingProgressPhoto -> {
                if (progressPhoto.getNote() != null) {
                    existingProgressPhoto.setNote(progressPhoto.getNote());
                }
                if (progressPhoto.getImage() != null) {
                    existingProgressPhoto.setImage(progressPhoto.getImage());
                }
                if (progressPhoto.getImageContentType() != null) {
                    existingProgressPhoto.setImageContentType(progressPhoto.getImageContentType());
                }
                if (progressPhoto.getWeightDate() != null) {
                    existingProgressPhoto.setWeightDate(progressPhoto.getWeightDate());
                }

                return existingProgressPhoto;
            })
            .map(progressPhotoRepository::save)
            .map(savedProgressPhoto -> {
                progressPhotoSearchRepository.save(savedProgressPhoto);

                return savedProgressPhoto;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressPhoto> findAll() {
        log.debug("Request to get all ProgressPhotos");
        return progressPhotoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgressPhoto> findOne(Long id) {
        log.debug("Request to get ProgressPhoto : {}", id);
        return progressPhotoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProgressPhoto : {}", id);
        progressPhotoRepository.deleteById(id);
        progressPhotoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressPhoto> search(String query) {
        log.debug("Request to search ProgressPhotos for query {}", query);
        return StreamSupport.stream(progressPhotoSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
