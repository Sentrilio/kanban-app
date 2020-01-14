import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

var stompClient = null;

class WebSocketService {

    connect(boardId, func) {
        var socket = new SockJS('http://localhost:8000/websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/board/' + boardId, func)
        });
    }

    disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
    }

}

export default new WebSocketService();