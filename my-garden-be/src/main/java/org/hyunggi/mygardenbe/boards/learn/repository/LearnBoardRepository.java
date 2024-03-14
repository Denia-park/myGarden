package org.hyunggi.mygardenbe.boards.learn.repository;

import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TIL 게시판 Entity Repository
 */
public interface LearnBoardRepository extends JpaRepository<LearnBoardEntity, Long>, LearnBoardRepositoryCustom {
}
