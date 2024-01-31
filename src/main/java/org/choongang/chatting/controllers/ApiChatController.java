package org.choongang.chatting.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.choongang.chatting.entities.ChatHistory;
import org.choongang.chatting.entities.ChatRoom;
import org.choongang.chatting.service.ChatHistoryInfoService;
import org.choongang.chatting.service.ChatHistorySaveService;
import org.choongang.chatting.service.ChatRoomInfoService;
import org.choongang.chatting.service.ChatRoomSaveService;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.ExceptionRestProcessor;
import org.choongang.commons.ListData;
import org.choongang.commons.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ApiChatController implements ExceptionRestProcessor {

    private final ChatRoomInfoService chatRoomInfoService;
    private final ChatRoomSaveService chatRoomSaveService;
    private final ChatHistorySaveService chatHistorySaveService;
    private final ChatHistoryInfoService chatHistoryInfoService;

    /**
     * 방 목록 조회
     * @param search
     * @return
     *
     * ChatRoomSearch   HTTP 요청의 본문에 있는 JSON 데이터를 자바 객체로 변환하는 데 사용되는 클래스
     */
    @GetMapping("/room")
    public JSONData<ListData<ChatRoom>> getRooms(@RequestBody ChatRoomSearch search){
        ListData<ChatRoom> data = chatRoomInfoService.getList(search);
        return new JSONData<>(data);
    }

    @PostMapping("/room")
    public ResponseEntity saveRoom(@RequestBody RequestChatRoom form){
        chatRoomSaveService.save(form);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/room/{roomId}")
    public JSONData<ChatRoom> getRoom(@PathVariable("roomId") String roomId){
        ChatRoom data = chatRoomInfoService.get(roomId);

        return new JSONData<>(data);
    }

    /**
     *
     * 채팅 메세지 기록 저장
     *
     * @param form
     * @return
     */
    @PostMapping
    public ResponseEntity messageSave(@RequestBody RequestChatHistory form){
        chatHistorySaveService.save(form);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/messages/{roomId}")
    public JSONData<List<ChatHistory>> getMessage(@PathVariable("roomId") String roomId, @RequestBody ChatHistorySearch search){
        List<ChatHistory> messages = chatHistoryInfoService.getList(roomId, search);
        return new JSONData<>(messages);
    }
}
