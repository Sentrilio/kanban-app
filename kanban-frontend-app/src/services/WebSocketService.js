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
        stompClient.onclose = function (e) {
            console.log("onclose: " + e);
        };
    }

    disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
            console.log("Disconnected");
        } else {
            console.log("there was no connection");
        }

    }

}

export default new WebSocketService();