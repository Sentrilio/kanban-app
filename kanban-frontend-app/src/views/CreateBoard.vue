<template>
  <div>
    <form @submit.prevent="createBoard">
      <input type="text" v-model="boardName" />
      <button type="submit">Create Board</button>
    </form>
  </div>
</template>

<script>
import UserService from "../services/user.service";

export default {
  name: "CreateBoard",

  data() {
    return {
      boardName: "",
    };
  },
  mounted() {
  },
  methods: {
    createBoard() {
      UserService.createBoard(this.boardName,this.$store.state.selectedTeam.teamId)
      .then((response) => {
          console.log(response)
          this.$router.push("/");//should be prompted info about successful creation and ok to click
        },
        error => {
          console.log(error.response)
        }
      );
    }
  }
};
</script>
<style lang="scss" scoped>
</style>