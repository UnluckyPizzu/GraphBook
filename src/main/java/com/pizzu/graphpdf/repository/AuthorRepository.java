package com.pizzu.graphpdf.repository;

import com.pizzu.graphpdf.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {
}
