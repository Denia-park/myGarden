package org.hyunggi.mygardenbe.boards.common.category.repository;

import org.hyunggi.mygardenbe.boards.common.category.entity.BoardCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 게시판 분류 Repository
 */
public interface BoardCategoryRepository extends JpaRepository<BoardCategoryEntity, Long> {
    /**
     * 분류 코드와 게시판 타입으로 분류 조회
     *
     * @param categoryCode 분류 코드
     * @param boardType    게시판 타입
     * @return 분류 Entity의 Optional
     */
    Optional<BoardCategoryEntity> findByCodeAndBoardType(final String categoryCode, final String boardType);

    /**
     * 게시판 타입으로 분류 목록 조회
     *
     * @param boardType 게시판 타입
     * @return 분류 Entity 목록
     */
    List<BoardCategoryEntity> findAllByBoardType(final String boardType);
}
