package org.hyunggi.mygardenbe.boards.common.repository;

import org.hyunggi.mygardenbe.boards.common.entity.BoardCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardCategoryRepository extends JpaRepository<BoardCategoryEntity, Long> {
    Optional<BoardCategoryEntity> findByCode(final String categoryCode);
}
