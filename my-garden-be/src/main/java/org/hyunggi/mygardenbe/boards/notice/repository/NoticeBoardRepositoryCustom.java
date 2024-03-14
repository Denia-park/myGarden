package org.hyunggi.mygardenbe.boards.notice.repository;

import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * 공지사항 게시판 Repository Custom
 */
public interface NoticeBoardRepositoryCustom {
    /**
     * 공지사항 게시판 목록 조회
     *
     * @param startDateTime 조회 시작일
     * @param endDateTime   조회 종료일
     * @param category      조회할 분류
     * @param searchText    검색어
     * @param pageable      페이징
     * @return 공지사항 게시판 목록 (페이지)
     */
    Page<NoticeBoardEntity> searchNoticeBoards(LocalDateTime startDateTime, LocalDateTime endDateTime, String category, String searchText, Pageable pageable);
}
