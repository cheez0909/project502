window.addEventListener("DOMContentLoaded", function(){

    // 웹소켓 주소/네임
    const webSocket = new WebSocket("ws://localhost:4321/chat");

    webSocket.onopen = function(e){
        console.log("연결 성립!", e); // 이벤트 객체 확인

        webSocket.send("메세지!");

    };

    webSocket.onclose = function(e) {
        console.log("연결 종료!", e);
    };

    // 서버쪽에서 전송 -> 수신
    webSocket.onmessage = function(msg){
        console.log(msg.data);
    };



});