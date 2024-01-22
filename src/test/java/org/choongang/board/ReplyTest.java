package org.choongang.board;

import org.choongang.board.repositories.BoardDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReplyTest {

    @Autowired
    private BoardDataRepository boardDataRepository;

    @Test
    void Test1(){
        Long lastReplyListOrder2 = boardDataRepository.getLastReplyListOrder(2L);
        System.out.println(lastReplyListOrder2);

        Long lastReplyListOrder1 = boardDataRepository.getLastReplyListOrder(1L);
        System.out.println(lastReplyListOrder1);

    }

}
