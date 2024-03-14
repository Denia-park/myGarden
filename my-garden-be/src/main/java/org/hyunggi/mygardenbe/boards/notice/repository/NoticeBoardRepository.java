package org.hyunggi.mygardenbe.boards.notice.repository;

import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 공지사항 게시판 Repository
 */
public interface NoticeBoardRepository extends JpaRepository<NoticeBoardEntity, Long>, NoticeBoardRepositoryCustom {
    /**
     * 중요 공지사항 조회
     *
     * @param isImportant 중요 여부
     * @return 중요 공지사항 목록
     */
    List<NoticeBoardEntity> findAllByIsImportantOrderByWrittenAtDesc(boolean isImportant);
}
