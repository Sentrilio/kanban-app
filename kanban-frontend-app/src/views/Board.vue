<template>
  <div class="container">
    <a>Board name: {{board.name}}</a>
    <br />
    <a>Team name: {{team.name}}</a>
    <br />
    <a>Tasks:</a>
    <ul>
      <!-- <li v-for="index in 5" :key="index"> -->
      <list-component v-bind:tasks="tasks"></list-component>
      <!-- </li> -->
    </ul>
    <!-- <button v-bind="createTask">Create task</button> -->
  </div>
</template>

<script>
import ListComponent from "../components/list.vue";
import UserService from "../services/user.service";

export default {
  name: "Board",
  components: {
    ListComponent
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
    getData() {
      this.getBoard();
      this.getTeam();
    },
    getBoard() {
      UserService.getBoard(this.$route.params.boardId).then(
        response => {
          this.board = response.data;
        },
        error => {
          this.content = error.response.data.message;
        }
      );
      // this.board = this.$store.state.selection.selectedBoard;
    },
    getTeam() {
      this.team = this.$store.state.selection.selectedTeam;
    }
  },
  mounted() {
    this.getData();
  },
  watch: {
    $route: "getData"
  }
};
</script>
<style lang="scss" scoped>
</style>