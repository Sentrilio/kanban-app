import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

var stompClient = null;

class WebSocketService {

    connect(boardId) {
        var socket = new SockJS('http://localhost:8000/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/greetings/' + boardId, function (greeting) {
                // stompClient.subscribe('/board/' + 26, function (greeting) {
                console.log(JSON.parse(greeting.body).content);
            });
        });
    }

    disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    sendName(name) {
        stompClient.send("/app/hello", {}, JSON.stringify({ 'name': name }));
    }
    updateTask(data) {
        stompClient.send("/app/task/update", {}, JSON.stringify(data));
    }

}

export default new WebSocketService();