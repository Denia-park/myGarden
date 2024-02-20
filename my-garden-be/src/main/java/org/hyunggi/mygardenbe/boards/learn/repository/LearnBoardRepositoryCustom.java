package org.hyunggi.mygardenbe.boards.learn.repository;

import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface LearnBoardRepositoryCustom {
    Page<LearnBoardEntity> searchLearnBoards(LocalDateTime startDateTime, LocalDateTime endDateTime, String category, String searchText, Pageable pageable);
}
