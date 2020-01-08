<template>
  <div class="create-column">
    <button class="btn" data-toggle="collapse" data-target="#demo">Create another Column</button>
    <div id="demo" class="collapse">
      <input v-model="columnNameInput" type="text" placeholder="column name" />
      <br>
      <button class="button" @click="createColumn" :disabled="!columnNameInput">Create Column</button>
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
    boardId: Number
  },

  methods: {
    createColumn() {
      console.log(
        "Creating column" + this.columnNameInput + " id: " + this.boardId
      );
      UserService.createColumn(this.columnNameInput, this.boardId)
        .then(response => {
          console.log(response);
          this.$emit("refresh");
        })
        .catch(err => {
          console.log(err);
        });
    }
  }
};
</script>
<style lang="css" scoped>
.create-column {
  width: 100px;
  align-content: right;
  padding: 10px;
  outline-width: 1px !important;
  outline-color: black !important;
  width: 400px;
}

.btn {
  background-color: #888888;
}

.btn:hover {
  color: black;
}
</style>