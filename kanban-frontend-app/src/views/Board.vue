<template>
  <div class="wrapper">
    <draggable
      :group="{ name: 'column'}"
      class="board"
      :list="board.columns"
      @change="columnChange($event)"
    >
      <div v-for="column in columns" :key="column.id">
        <column-draggable @refresh="refresh" @boardUpdate="boardUpdate" :column="column"></column-draggable>
      </div>
    </draggable>

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
import ColumnService from "../services/ColumnService";
import draggable from "vuedraggable";
import Operation from "../models/Operation";
import WebSocketService from "../services/WebSocketService.js";

export default {
  name: "Board",
  components: {
    CreateColumn,
    ColumnDraggable,
    draggable
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
      console.log("getting board from board.vue");
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
      console.log("getting team from board.vue");
      TeamService.getTeam(this.$route.params.teamId)
        .then(response => {
          this.team = response.data;
        })
        .catch(err => {
          console.log(err);
        });
    },

    setSockJS() {
      WebSocketService.connect(this.$route.params.boardId, this.messageHandle);
    },
    messageHandle(data) {
      console.log("Message came up:");
      JSON.parse(data.body).message;
      if (JSON.parse(data.body).message === "board updated") {
        this.getData();
      }
    },
    setBoard() {
      this.getData();
      this.setSockJS();
    },
    columnChange: function(event) {
      if (event.moved) {
        console.log("moving...");
        this.updateColumn(event.moved, Operation.MOVE);
      }
      console.log(event);
    },
    updateColumn(event, operation) {
      console.log(event);
      let updateObject = {
        columnId: event.element.id,
        newIndex: event.newIndex,
        oldIndex: event.oldIndex,
        operation: operation
      };
      console.log(updateObject);
      ColumnService.changeColumnPosition(updateObject)
        .then(response => {
          console.log(response);
        })
        .catch(err => {
          console.log(err);
        });
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
  flex-direction: row;
  justify-content: space-between;
}
.wrapper {
  display: flex;
  flex-direction: row;
  overflow-x: auto;
  /* overflow-y: auto; */
  background-color: grey;
}
div.wrapper {
  height: 92vh;
}
</style>