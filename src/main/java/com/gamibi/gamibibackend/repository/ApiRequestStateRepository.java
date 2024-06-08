package com.gamibi.gamibibackend.repository;

import com.gamibi.gamibibackend.entity.ApiRequestState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad ApiRequestState.
 */
@Repository
public interface ApiRequestStateRepository extends JpaRepository<ApiRequestState, Long> {
}