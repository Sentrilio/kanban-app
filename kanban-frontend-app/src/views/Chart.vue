<template>
  <div>
      <a href="">MEGA CHART</a>
  </div>
</template>
<script>
import BoardService from "../services/BoardService";
export default {
  props: {
    boardId: Number
  },
  methods: {
    getBoard() {
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
    }
  },
  created() {
      this.getBoard();
  }
};
</script>
<style lang="css" scoped>
</style>