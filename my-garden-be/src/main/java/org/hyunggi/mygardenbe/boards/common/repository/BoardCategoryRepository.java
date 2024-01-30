package org.hyunggi.mygardenbe.boards.common.repository;

import org.hyunggi.mygardenbe.boards.common.entity.BoardCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardCategoryRepository extends JpaRepository<BoardCategoryEntity, Long> {
    Optional<BoardCategoryEntity> findByCodeAndBoardType(final String categoryCode, final String boardType);

    List<BoardCategoryEntity> findAllByBoardType(final String boardType);
}
