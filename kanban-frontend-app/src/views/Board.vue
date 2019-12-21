<template>
  <div class="my-container">
    <nav class="navbar navbar-expand navbar-dark bg-white" v-if="!$route.meta.hideNavigation">
      <div class="navbar-nav mr-auto">
        <li class="nav-item">
          <button>{{board.name}}</button>
        </li>
        <li class="nav-item">
          <button>{{team.name}}</button>
        </li>
      </div>
    </nav>
    <div class="list-container">
      <div v-for="index in 5" :key="index">
        <board-list v-bind:tasks="tasks"></board-list>
      </div>
      <create-list v-bind:tasks="tasks"></create-list>
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
  computed: {
    // currentUser() {
    //   return this.$store.state.auth.user;
    // }
  },
  data() {
    return {
      tasks: ["Task 1", "Task 2", "Task 3", "Task 4"],
      board: {},
      team: {}
    };
  },
  methods: {
    createTask() {
      let boardId = this.getBoard().boardId;
      let listId = this.getList().listId;
      this.$route.push({ name: "createTask", params: { boardId, listId } });
    },
    createList() {
      let boardId = this.getBoard().boardId;
      let listId = this.getList().listId;
      this.$route.push({ name: "createTask", params: { boardId, listId } });
    },
    getData() {
      this.getBoard();
      // this.getTeam();
    },
    getBoard() {
      UserService.getBoard(this.$route.params.boardId)
        .then(response => {
          console.log("status 200");
          this.board = response.data;
          this.team = response.data.team;
        })
        .catch(error => {
          console.log("status: " + error.response.status);
          if (error.response.status === 401) {
            this.$router.push("/home"); // to do component with info about not having perrmision redirection
          }
        });
    }
  },
  mounted() {
    console.log(this.$route.params);
    this.getData();
  },
  watch: {
    // $route: "getData"
  }
};
</script>
<style lang="css" scoped>
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
  padding-top:10px;
  display: grid;
  grid-template-columns: repeat(10, 1000fr);
  grid-gap: 10px;
  grid-auto-rows: 100px
}
</style>