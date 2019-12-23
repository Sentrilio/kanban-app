<template>
  <div class="my-container">
    <nav class="navbar navbar-expand navbar-dark bg-white" v-if="!$route.meta.hideNavigation">
      <div class="navbar-nav mr-auto">
        <li class="nav-item">
          <input class="board-name-box" type="text" v-model="currentBoard.name" />
        </li>
        <li class="nav-item">
          <button @click="switchToTeam(currentTeam)">{{team.name}}</button>
        </li>
      </div>
    </nav>
    <div class="column-container">
      <div v-for="column in columns" :key="column.position">
        <column v-bind:column="column" v-bind:tasks="tasks"></column>
      </div>
      <create-column v-on:refresh="getData" v-bind:tasks="tasks" v-bind:boardId="board.id"></create-column>
    </div>
  </div>
</template>

<script>
import CreateColumn from "../components/CreateColumn.vue";
import Column from "../components/Column.vue";
import UserService from "../services/user.service";

export default {
  name: "Board",
  components: {
    CreateColumn,
    Column
  },
  data() {
    return {
      columns: [],
      tasks: ["Task 1", "Task 2", "Task 3", "Task 4"],
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
    },
    orderedColumns() {
      return this.columns;
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
    refresh() {
      this.getData();
    },
    compare(a, b) {
      return a.position - b.position;
    },
    sortColumns() {
      this.columns.sort(this.compare);
    },
    createTask() {
      // let boardId = this.getBoard().id;
      // let columnId = this.getColumn().id;
      // this.$route.push({ name: "createTask", params: { boardId, columnId } });
    },

    getData() {
      this.getBoard();
      this.getTeam();
    },
    getBoard() {
      UserService.getBoard(this.$route.params.boardId)
        .then(response => {
          console.log("status 200");
          this.board = response.data;
          this.columns = this.board.columns;
          this.sortColumns();
        })
        .catch(error => {
          console.log(error);
          // console.log("status: " + error.response.status);
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
    }
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
.my-container {
  align-content: left;
}
.column-container {
  padding-top: 10px;
  display: grid;
  grid-template-columns: repeat(10, 1000fr);
  grid-gap: 10px;
  grid-auto-rows: 100px;
}
</style>