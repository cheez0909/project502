package org.choongang.chatting.configs;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatHandler extends TextWebSocketHandler {
    private static List<WebSocketSession> sessions = new ArrayList<>();

    /**
     * 연결 됐을 때
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); // 접속자 세션 추가
    }

    /**
     * 넘겨 받은 메세지를 그대로 넘겨 줌
     * @param session
     * @param message -> .getPayload()에서 메세지 확인 가능
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        for(WebSocketSession s : sessions){
            s.sendMessage(message);
        }
    }

    /**
     * 연결 종료 됐을 때
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session); // 접속자 세션 제거
    }
}
