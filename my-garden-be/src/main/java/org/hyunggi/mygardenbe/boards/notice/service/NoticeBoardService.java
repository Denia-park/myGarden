package org.hyunggi.mygardenbe.boards.notice.service;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.notice.service.response.NoticeBoardResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class NoticeBoardService {
    public CustomPage<NoticeBoardResponse> getNoticeBoards(final LocalDate startDate, final LocalDate endDate, final String category, final String searchText, final Pageable pageable) {
        return null;
    }
}
