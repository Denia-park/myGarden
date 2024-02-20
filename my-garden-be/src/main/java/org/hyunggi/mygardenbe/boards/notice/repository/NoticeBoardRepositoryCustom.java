package org.hyunggi.mygardenbe.boards.notice.repository;

import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface NoticeBoardRepositoryCustom {
    Page<NoticeBoardEntity> searchNoticeBoards(LocalDateTime startDateTime, LocalDateTime endDateTime, String category, String searchText, Pageable pageable);
}
