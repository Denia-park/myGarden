package org.hyunggi.mygardenbe.boards.notice.repository;

import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoardEntity, Long>, NoticeBoardRepositoryCustom {
    List<NoticeBoardEntity> findAllByIsImportant(boolean isImportant);
}
