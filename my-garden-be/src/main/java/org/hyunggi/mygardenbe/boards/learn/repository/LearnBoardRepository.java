package org.hyunggi.mygardenbe.boards.learn.repository;

import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface LearnBoardRepository extends JpaRepository<LearnBoardEntity, Long> {
    //검색어 없음, 카테고리 없음
    @Query(value = "SELECT lbe FROM LearnBoardEntity lbe WHERE lbe.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd")
    Page<LearnBoardEntity> findAllInDateRange(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, Pageable pageable);

    //검색어 없음, 카테고리 있음
    @Query(value = "SELECT lbe FROM LearnBoardEntity lbe WHERE lbe.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd AND lbe.category = :category")
    Page<LearnBoardEntity> findAllInDateRangeByCategory(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, String category, Pageable pageable);

    //검색어 있음, 카테고리 없음
    @Query(value = "SELECT lbe FROM LearnBoardEntity lbe WHERE lbe.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd AND (lbe.title LIKE %:searchText% OR lbe.content LIKE %:searchText%)")
    Page<LearnBoardEntity> findAllInDateRangeWithTextSearch(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, String searchText, Pageable pageable);

    //검색어 있음, 카테고리 있음
    @Query(value = "SELECT lbe FROM LearnBoardEntity lbe WHERE lbe.writtenAt BETWEEN :writtenAtStart AND :writtenAtEnd AND lbe.category = :category AND (lbe.title LIKE %:searchText% OR lbe.content LIKE %:searchText%)")
    Page<LearnBoardEntity> findAllInDateRangeByCategoryWithTextSearch(LocalDateTime writtenAtStart, LocalDateTime writtenAtEnd, String category, String searchText, Pageable pageable);
}
