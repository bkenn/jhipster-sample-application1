package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RepType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RepType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepTypeRepository extends JpaRepository<RepType, Long> {}
