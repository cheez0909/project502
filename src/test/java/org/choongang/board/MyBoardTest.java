package org.choongang.board;

import org.choongang.board.repositories.BoardDataRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MyBoardTest {

    @Autowired
    private BoardDataRepository repository;

    @Test @Disabled
    void test(){
         List<String> bids = repository.getUserBoards("user01");
    }
}
