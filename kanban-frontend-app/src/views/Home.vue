<template>
  <div class="container">
    <header class="jumbotron">
      <h3>{{content}}</h3>
    </header>
  </div>
</template>

<script>
import UserService from "../services/UserService";

export default {
  name: "home",
  data() {
    return {
      content: ""
    };
  },

  mounted() {
    UserService.getPublicContent().then(
      response => {
        this.content = response.data;
      },
      error => {
        this.content = error.response.data.message;
      }
    );
  }
};
</script>