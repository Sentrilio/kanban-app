<template>
  <div class="my-container">
    <div class="myGrid">
      <button class="btn" data-toggle="collapse" data-target="#demo">Create another List</button>
      <div id="demo" class="collapse">
        <input v-model="listNameInput" type="text" placeholder="list name" />
        <button class="button" @click="createList" :disabled="!listNameInput">Create List</button>
      </div>
      <a>Board id : {{boardId}}</a>
    </div>
  </div>
</template>

<script>
import UserService from "../services/user.service";
export default {
  props: {
    tasks: Array,
    boardId: Number
  },
  methods: {
    createList() {
      console.log("Creating list");
      UserService.createList(this.listNameInput, this.boardId)
        .then(response => {
          console.log(response);
          this.$emit('getData')
        })
        .catch(err => {
          console.log(err);
        });
    }
  },
  data() {
    return {
      visible: false,
      listNameInput: ""
    };
  }
};
</script>
<style lang="css" scoped>
.my-container {
  width: 100px;
  align-content: right;
}
.myinput1 {
  border: none;
}
.myInput1:focus {
  border: 1px;
}
/* .collapseButton {
  background-color: green;
  width: 250px;
} */
.myGrid {
  padding: 5px;
  outline-style: solid;
  outline-width: 1px !important;
  outline-color: black !important;
  width: 300px;
}
.btn {
  background-color: #888888;
}
/* .button { */
/* background-color: #4cd137; */
/* color: black; */
/* } */
.btn:hover {
  color: black;
}
</style>