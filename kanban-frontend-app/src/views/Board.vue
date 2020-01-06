<template>
  <div class="board">
    <div v-for="column in columns" :key="column.id">
      <column-draggable @refresh="refresh" @boardUpdate="boardUpdate" :column="column"></column-draggable>
    </div>
    <create-column @refresh="refresh" v-bind:boardId="board.id"></create-column>
  </div>
</template>

<script>
import CreateColumn from "../components/CreateColumn.vue";
import ColumnDraggable from "../components/ColumnDraggable.vue";
import BoardService from "../services/BoardService";
import TeamService from "../services/TeamService";

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

.board {
  /* display: inline-block; */
  display: flex;
  justify-content: space-between;
  background-color: grey;
  max-height: 2000px;
  height: 92vh;
}


</style>