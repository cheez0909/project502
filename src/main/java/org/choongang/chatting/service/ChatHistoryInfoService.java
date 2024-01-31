package org.choongang.chatting.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.choongang.chatting.controllers.ChatHistorySearch;
import org.choongang.chatting.entities.ChatHistory;
import org.choongang.chatting.entities.QChatHistory;
import org.choongang.chatting.repositories.ChatHistoryRepository;
import org.choongang.commons.ListData;
import org.choongang.commons.Utils;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatHistoryInfoService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatRoomInfoService chatRoomInfoService;
    private final MemberUtil memberUtil;
    private final EntityManager em;

    /**
     * 방 아이디로 조회
     * @param roomId
     * @param search
     * @return
     */
    public List<ChatHistory> getList(String roomId, ChatHistorySearch search){

        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 20);
        int offset = (page - 1) * limit;

        PathBuilder<ChatHistory> pathBuilder = new PathBuilder<>(ChatHistory.class, "chatHistory");

        QChatHistory chatHistory = QChatHistory.chatHistory;
        BooleanBuilder andBulder = new BooleanBuilder();

        andBulder.and(chatHistory.chatRoom.roomId.eq(roomId));

        List<ChatHistory> items = new JPAQueryFactory(em).selectFrom(chatHistory)
                .leftJoin(chatHistory.member)
                .fetchJoin()
                .where(andBulder)
                .orderBy(new OrderSpecifier(Order.ASC, pathBuilder.get("createdAt")))
                .fetch();

        return items;
    }

}
