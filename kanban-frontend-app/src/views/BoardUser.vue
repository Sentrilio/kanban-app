<template>
  <div class="container">
    <header class="jumbotron">
      <h3>{{content}}</h3>
    </header>
  </div>
</template>

<script>
import UserService from '../services/user.service';
import { log } from 'util';

export default {
  name: 'user',
  data() {
    return {
      content: ''
    };
  },
  mounted() {
    UserService.getUserBoard().then(
      response => {
        log(response)
        this.content = response.data;
      },
      error => {
        this.content = error.response.data.message;
      }
    );
  }
};
</script>