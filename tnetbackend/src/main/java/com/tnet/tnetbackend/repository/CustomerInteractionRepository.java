package com.tnet.tnetbackend.repository;

import com.tnet.tnetbackend.entity.CustomerInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerInteractionRepository extends JpaRepository<CustomerInteraction, Long> {
}