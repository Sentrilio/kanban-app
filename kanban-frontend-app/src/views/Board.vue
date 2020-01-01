<template>
  <div class="board">
    <div class="row">
      <div class="column">
        <div v-for="column in columns" :key="column.id">
          <h3>{{column.name}}</h3>
          <vue-draggable @refresh="refresh" @boardUpdate="boardUpdate" :column="column"></vue-draggable>
        </div>
        <create-column @refresh="refresh" v-bind:boardId="board.id"></create-column>
      </div>
    </div>
  </div>
</template>

<script>
import CreateColumn from "../components/CreateColumn.vue";
import UserService from "../services/user.service";
import VueDraggable from "../components/VueDraggable.vue";

export default {
  name: "Board",
  components: {
    CreateColumn,
    VueDraggable
  },
  data() {
    return {
      columns: [],
      board: {},
      team: {}
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
      UserService.updateBoard(this.board.id, this.board.columns)
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
    compare(a, b) {
      return a.position - b.position;
    },
    sortColumns() {
      this.columns.sort(this.compare);
    },
    sortTasks() {
      this.column.tasks.sort(this.compare);
    },

    getData() {
      this.getBoard();
      this.getTeam();
    },
    getBoard() {
      UserService.getBoard(this.$route.params.boardId)
        .then(response => {
          console.log("board retrieved");
          this.board = response.data;
          this.columns = this.board.columns;
          // this.sortColumns();
          // this.columns.forEach(column => {
            // column.tasks.sort(this.compare);
          // });
        })
        .catch(error => {
          console.log(error);
          if (error.response.status === 401) {
            this.$router.push("/home"); // to do component with info about not having perrmision redirection
          }
        });
    },
    getTeam() {
      UserService.getTeam(this.$route.params.teamId)
        .then(response => {
          this.team = response.data;
        })
        .catch(err => {
          console.log(err);
        });
    },
  },
  created() {
    this.getData();
  },
  watch: {
    $route: "getData"
  }
};
</script>
<style lang="css" scoped>
.board-name-box {
  /* outline-style: none; */
  border-style: none;
}
.board-name-box:hover {
  /* outline-style: none; */
  background-color: #f5f5f5;
}
.nav-item {
  padding-left: 20px;
  /* padding-right: 20px; */
}
.navbar-nav {
  margin-left: 40px;
}
.board {
  margin: 15px;
  padding: 15px;
  display: inline-block;
  /* align-content: left; */
  /* display: flexbox; */
  /* display: flex; */
  /* flex-direction: row; */
  /* flex-wrap: wrap; */
}
.column {
  margin: 15px;
  padding: 15px;
  /* display: inline-block; */
  display: flex;
}
.column-container {
  padding-top: 10px;
  display: grid;
  grid-template-columns: repeat(10, 1000fr);
  grid-gap: 10px;
  grid-auto-rows: 100px;
}
</style>