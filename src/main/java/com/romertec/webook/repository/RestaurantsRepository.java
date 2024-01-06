package com.romertec.webook.repository;

import com.romertec.webook.entities.RestaurantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantsRepository extends JpaRepository<RestaurantsEntity, Integer> {


    @Query(value = "SELECT r.* FROM restaurants r " +
            "INNER JOIN menu m ON r.id = m.restaurantId " +
            "WHERE r.street IS NOT NULL AND r.city IS NOT NULL AND r.state IS NOT NULL AND r.zip IS NOT NULL " +
            "GROUP BY r.id " +
            "ORDER BY RAND() LIMIT 1",
            nativeQuery = true)
    Optional<RestaurantsEntity> findRandomRestaurant();
}
