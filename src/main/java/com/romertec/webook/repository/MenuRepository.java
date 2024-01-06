package com.romertec.webook.repository;

import com.romertec.webook.entities.MenuEntity;
import com.romertec.webook.entities.RestaurantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {

    @Query(value = "SELECT * FROM menu WHERE restaurantId = :restaurantId ORDER BY RAND()", nativeQuery = true)
    Optional<List<MenuEntity>> findAllMenusByRestaurantId(@Param("restaurantId") int restaurantId);
}