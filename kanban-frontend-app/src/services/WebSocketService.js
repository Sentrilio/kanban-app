import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

var stompClient = null;
// var boardId = null;

class WebSocketService {
    boardId = 0;

    constructor(boardId) {
        this.boardId = boardId;
    }

    connect() {
        var socket = new SockJS('http://localhost:8000/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/greetings', function (greeting) {
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
    sendTask(name) {
        stompClient.send("/app/task/update", {}, JSON.stringify({ 'name': name }));
    }

}

export default new WebSocketService();