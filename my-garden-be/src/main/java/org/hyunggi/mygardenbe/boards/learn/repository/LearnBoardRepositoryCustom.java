package org.hyunggi.mygardenbe.boards.learn.repository;

import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * TIL 게시판 Entity Repository Custom
 */
public interface LearnBoardRepositoryCustom {
    /**
     * 게시글 검색
     *
     * @param startDateTime 조회 시작일
     * @param endDateTime   조회 종료일
     * @param category      조회할 분류
     * @param searchText    검색어
     * @param pageable      페이징
     * @return 게시글 목록
     */
    Page<LearnBoardEntity> searchLearnBoards(LocalDateTime startDateTime, LocalDateTime endDateTime, String category, String searchText, Pageable pageable);
}
