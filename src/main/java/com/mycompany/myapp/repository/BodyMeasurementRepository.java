package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BodyMeasurement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BodyMeasurement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurement, Long> {}
