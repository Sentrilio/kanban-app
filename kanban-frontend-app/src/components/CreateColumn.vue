<template>
  <div class="my-container">
    <div class="myGrid">
      <button class="btn" data-toggle="collapse" data-target="#demo">Create another Column</button>
      <div id="demo" class="collapse">
        <input v-model="columnNameInput" type="text" placeholder="column name" />
        <button class="button" @click="createColumn" :disabled="!columnNameInput">Create Column</button>
      </div>
      <a>Board id : {{boardId}}</a>
    </div>
  </div>
</template>

<script>
import UserService from "../services/user.service";
export default {
  data() {
    return {
      columnNameInput: ""
    };
  },
  props: {
    boardId: Number,
  },

  methods: {
    createColumn() {
      console.log(
        "Creating column" + this.columnNameInput + " id: " + this.boardId
      );
      UserService.createColumn(this.columnNameInput, this.boardId)
        .then(response => {
          console.log(response);
          this.$emit('refresh')
        })
        .catch(err => {
          console.log(err);
        });
    },
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

.btn:hover {
  color: black;
}
</style>