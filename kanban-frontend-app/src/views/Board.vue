<template>
  <div class="board">
    <div v-for="column in columns" :key="column.id">
      <column-draggable @refresh="refresh" @boardUpdate="boardUpdate" :column="column"></column-draggable>
    </div>
    <create-column @refresh="refresh" v-bind:boardId="board.id"></create-column>
    <button @click="sendName">send name</button>
    <button @click="subscribe">Subscribe</button>

  </div>
</template>

<script>
import CreateColumn from "../components/CreateColumn.vue";
import ColumnDraggable from "../components/ColumnDraggable.vue";
import BoardService from "../services/BoardService";
import TeamService from "../services/TeamService";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import WebSockerService from "../services/WebSocketService.js";
// import authHeader from '../services/AuthHeader';

export default {
  name: "Board",
  components: {
    CreateColumn,
    ColumnDraggable
  },
  data() {
    return {
      columns: [],
      board: {},
      team: {},
      stompClient: {}
    };
  },
  computed: {
    currentBoard() {
      return this.board;
    },
    currentTeam() {
      return this.team;
    }
  },
  methods: {
    switchToTeam(team) {
      if (team) {
        console.log("team switching");
        let teamId = team.id;
        let teamName = team.name;
        this.$router.push({
          name: "team",
          params: { teamId, teamName }
        });
      }
    },
    boardUpdate() {
      console.log("sending updated board into backend...");
      BoardService.updateBoard(this.board.id, this.board.columns)
        .then(() => {
          this.getBoard();
        })
        .catch(err => {
          console.log(err);
        });
    },
    refresh() {
      console.log("refreshing");
      this.getData();
    },
    getData() {
      this.getBoard();
      this.getTeam();
    },
    getBoard() {
      BoardService.getBoard(this.$route.params.boardId)
        .then(response => {
          console.log("board retrieved");
          this.board = response.data;
          this.columns = this.board.columns;
        })
        .catch(error => {
          console.log(error);
          if (error.response.status === 401) {
            this.$router.push("/home"); // to do component with info about not having perrmision redirection
          }
        });
    },
    getTeam() {
      TeamService.getTeam(this.$route.params.teamId)
        .then(response => {
          this.team = response.data;
        })
        .catch(err => {
          console.log(err);
        });
    },

    connect() {
      var socket = new SockJS("http://localhost:8000/gs-guide-websocket");
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect();
    },
    sendName() {
      // WebSockerService.sendName("Chris");
      WebSockerService.sendTask("Toby");

      // this.stompClient.send("app/hello", {}, JSON.stringify({ name: "name" }));
    },
    subscribe() {
      this.stompClient.subscribe("/topic/greetings", function(greeting) {
        console.log(JSON.parse(greeting.body).content);
      });
    },
    disconnect() {
      if (this.stompClient !== null) {
        this.stompClient.disconnect();
      }
      console.log("Disconnected");
    },

    showGreeting(message) {
      console.log(message);
    },
    setSockJS() {
      WebSockerService.connect();
      // this.connect();
      // this.sendName();
    }
  },

  created() {
    this.getData();
    this.setSockJS();
  },
  watch: {
    $route: "getData"
  }
};
</script>
<style lang="css" scoped>
.board {
  display: flex;
  justify-content: space-between;
  background-color: grey;
  height: 92vh;
}
</style>