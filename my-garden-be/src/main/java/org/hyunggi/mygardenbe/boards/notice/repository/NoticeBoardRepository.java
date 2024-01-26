package org.hyunggi.mygardenbe.boards.notice.repository;

import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoardEntity, Long> {
    //검색어 없음, 카테고리 없음
    @Query(value = "SELECT nb FROM NoticeBoardEntity nb WHERE nb.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd")
    List<NoticeBoardEntity> findAllInDateRange(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, Pageable pageable);

    //검색어 없음, 카테고리 있음
    @Query(value = "SELECT nb FROM NoticeBoardEntity nb WHERE nb.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd AND nb.category = :category")
    List<NoticeBoardEntity> findAllInDateRangeByCategory(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, String category, Pageable pageable);

    //검색어 있음, 카테고리 없음
    @Query(value = "SELECT nb FROM NoticeBoardEntity nb WHERE nb.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd AND (nb.title LIKE %:searchText% OR nb.content LIKE %:searchText%)")
    List<NoticeBoardEntity> findAllInDateRangeWithTextSearch(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, String searchText, Pageable pageable);

    //검색어 있음, 카테고리 있음
    @Query(value = "SELECT nb FROM NoticeBoardEntity nb WHERE nb.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd AND nb.category = :category AND (nb.title LIKE %:searchText% OR nb.content LIKE %:searchText%)")
    List<NoticeBoardEntity> findAllInDateRangeByCategoryWithTextSearch(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, String category, String searchText, Pageable pageable);
}
