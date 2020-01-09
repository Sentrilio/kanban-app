import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

var stompClient = null;

class WebSocketService {

    connect() {
        var socket = new SockJS('http://localhost:8000/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/greetings', function (greeting) {
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

    sendName() {
        stompClient.send("/app/hello", {}, JSON.stringify({ 'name': "#name" }));
    }

}

export default new WebSocketService();