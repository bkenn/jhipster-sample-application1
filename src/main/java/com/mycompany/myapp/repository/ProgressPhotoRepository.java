package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProgressPhoto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProgressPhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgressPhotoRepository extends JpaRepository<ProgressPhoto, Long> {}
