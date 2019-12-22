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
    <div class="list-container">
      <div v-for="blist in blists" :key="blist.position">
        <board-list v-bind:blist="blist" v-bind:tasks="tasks"></board-list>
      </div>
      <create-list v-on:refresh="getData" v-bind:tasks="tasks" v-bind:boardId="board.boardId"></create-list>
    </div>
  </div>
</template>

<script>
import CreateList from "../components/CreateList.vue";
import BoardList from "../components/List.vue";
import UserService from "../services/user.service";

export default {
  name: "Board",
  components: {
    CreateList,
    BoardList
  },
  data() {
    return {
      tasks: ["Task 1", "Task 2", "Task 3", "Task 4"],
      board: {},
      team: {},
      blists: []
    };
  },
  computed: {
    currentBoard() {
      return this.board;
    },
    currentTeam() {
      return this.team;
    },
    orderedBList() {
      return this.blists;
    }
  },

  methods: {
    switchToTeam(team) {
      if (team) {
        console.log("team switching");
        let teamId = team.teamId;
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
    sortBList() {
      this.blists.sort(this.compare);
    },
    createTask() {
      let boardId = this.getBoard().boardId;
      let listId = this.getList().listId;
      this.$route.push({ name: "createTask", params: { boardId, listId } });
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
          this.blists = this.board.blists;
          this.sortBList();
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
.list-container {
  padding-top: 10px;
  display: grid;
  grid-template-columns: repeat(10, 1000fr);
  grid-gap: 10px;
  grid-auto-rows: 100px;
}
</style>