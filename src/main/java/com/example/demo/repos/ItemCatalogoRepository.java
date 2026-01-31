package com.example.demo.repos;

import com.example.demo.domain.entities.ItemCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemCatalogoRepository extends JpaRepository<ItemCatalogo, UUID> {
}

