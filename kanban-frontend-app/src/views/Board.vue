<template>
  <div class="container" v-if="currentUser">
    <a>Board name: {{board.name}}</a>
    <br />
    <a>Team name: {{team.name}}</a>
    <br />
    <a>Tasks:</a>
    <ul>
      <li v-for="index in 5" :key="index">
        <list-component v-bind:tasks="tasks"></list-component>
      </li>
    </ul>
  </div>
</template>

<script>
import ListComponent from "../components/list.vue";
export default {
  name: "Board",
  components: {
    ListComponent
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  data() {
    return {
      tasks: ["A", "B", "C", "D"],
      board: {},
      team: {}
    };
  },
  methods: {
    getData() {
      this.getBoard();
      this.getTeam();
    },
    getBoard() {
      this.board = this.$store.state.selection.selectedBoard;
    },
    getTeam() {
      this.team = this.$store.state.selection.selectedTeam;
    }
  },
  mounted() {
    if (this.currentUser) {
      this.getData();
    }
  },
  watch: {
    $route: "getData"
  }
};
</script>
<style lang="scss" scoped>
</style>