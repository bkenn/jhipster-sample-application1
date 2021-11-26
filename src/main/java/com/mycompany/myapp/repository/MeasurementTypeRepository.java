package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MeasurementType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MeasurementType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeasurementTypeRepository extends JpaRepository<MeasurementType, Long> {}
