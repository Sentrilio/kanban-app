<template>
  <div class="board">
    <div v-for="column in columns" :key="column.id">
      <column-draggable
        @updateTask="updateTask"
        @refresh="refresh"
        @boardUpdate="boardUpdate"
        :column="column"
      ></column-draggable>
    </div>
    <create-column @refresh="refresh" v-bind:boardId="board.id"></create-column>
    <!-- <button @click="sendName">send name</button> -->
    <!-- <button @click="subscribe">Subscribe</button> -->
  </div>
</template>

<script>
import CreateColumn from "../components/CreateColumn.vue";
import ColumnDraggable from "../components/ColumnDraggable.vue";
import BoardService from "../services/BoardService";
import TeamService from "../services/TeamService";
// import SockJS from "sockjs-client";
// import Stomp from "stompjs";
import WebSocketService from "../services/WebSocketService.js";
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
      console.log("getting board from board")
      BoardService.getBoard(this.$route.params.boardId)
        .then(response => {
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
      console.log("getting team from board");
      TeamService.getTeam(this.$route.params.teamId)
        .then(response => {
          this.team = response.data;
        })
        .catch(err => {
          console.log(err);
        });
    },

    sendName() {
      // WebSockerService.sendName("Chris");
      WebSocketService.sendTask("Toby");
      // this.stompClient.send("app/hello", {}, JSON.stringify({ name: "name" }));
    },
    updateTask(data) {
      console.log(data);
      // WebSocketService.updateTask(data);
    },

    setSockJS() {
      WebSocketService.connect(this.$route.params.boardId);
    },
    setBoard() {
      this.getData();
      this.setSockJS();
    }
  },

  created() {
    this.setBoard();
    // this.getData();
    // this.setSockJS();
  },
  mounted() {
    // this.setBoard();
  },
  updated() {
    // this.setBoard();
  },
  destroyed() {
    WebSocketService.disconnect();
  },
  watch: {
    $route(to, from) {
      console.log(to);
      console.log(from);
      this.setBoard();
      // this.show = false;
    }
    // $route: "setBoard"
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