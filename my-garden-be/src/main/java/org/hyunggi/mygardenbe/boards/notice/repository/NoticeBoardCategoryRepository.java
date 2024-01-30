package org.hyunggi.mygardenbe.boards.notice.repository;

import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeBoardCategoryRepository extends JpaRepository<NoticeBoardCategoryEntity, Long> {
    Optional<NoticeBoardCategoryEntity> findByCode(final String categoryCode);
}
