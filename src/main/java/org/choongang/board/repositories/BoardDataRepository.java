package org.choongang.board.repositories;

import org.choongang.board.entites.BoardData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardDataRepository extends JpaRepository<BoardData, Long> {
}
