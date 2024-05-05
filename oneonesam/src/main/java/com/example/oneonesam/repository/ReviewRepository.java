package com.example.oneonesam.repository;

import com.example.oneonesam.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
