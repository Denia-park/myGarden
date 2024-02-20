package org.hyunggi.mygardenbe.boards.learn.repository;

import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnBoardRepository extends JpaRepository<LearnBoardEntity, Long>, LearnBoardRepositoryCustom {
}
