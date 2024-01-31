package org.hyunggi.mygardenbe.boards.notice.repository;

import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoardEntity, Long> {
    //검색어 없음, 카테고리 없음
    @Query(value = "SELECT nbe FROM NoticeBoardEntity nbe WHERE nbe.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd")
    Page<NoticeBoardEntity> findAllInDateRange(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, Pageable pageable);

    //검색어 없음, 카테고리 있음
    @Query(value = "SELECT nbe FROM NoticeBoardEntity nbe WHERE nbe.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd AND nbe.category = :category")
    Page<NoticeBoardEntity> findAllInDateRangeByCategory(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, String category, Pageable pageable);

    //검색어 있음, 카테고리 없음
    @Query(value = "SELECT nbe FROM NoticeBoardEntity nbe WHERE nbe.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd AND (nbe.title LIKE %:searchText% OR nbe.content LIKE %:searchText%)")
    Page<NoticeBoardEntity> findAllInDateRangeWithTextSearch(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, String searchText, Pageable pageable);

    //검색어 있음, 카테고리 있음
    @Query(value = "SELECT nbe FROM NoticeBoardEntity nbe WHERE nbe.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd AND nbe.category = :category AND (nbe.title LIKE %:searchText% OR nbe.content LIKE %:searchText%)")
    Page<NoticeBoardEntity> findAllInDateRangeByCategoryWithTextSearch(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, String category, String searchText, Pageable pageable);
}
