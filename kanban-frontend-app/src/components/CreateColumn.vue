<template>
  <div class="create-column">
    <button class="btn" data-toggle="collapse" data-target="#demo">Create another Column</button>
    <div id="demo" class="collapse">
      <input v-model="columnNameInput" type="text" placeholder="column name" />
      <div class="dropdown">
        <button
          class="btn btn-secondary dropdown-toggle"
          type="button"
          id="dropdownMenuButton"
          data-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false"
          :v-model="selectedLimit"
        >{{selectedLimit}}</button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
          <div v-for="index in 20" :key="index">
            <a class="dropdown-item" @click="setSelectedLimit(index)">{{index}}</a>
          </div>
        </div>
      </div>
      <br />
      <button class="button" @click="createColumn" :disabled="!columnNameInput">Create Column</button>
    </div>
  </div>
</template>

<script>
import ColumnService from "../services/ColumnService";
export default {
  data() {
    return {
      columnNameInput: "",
      selectedLimit: 5
    };
  },
  props: {
    boardId: Number
  },

  methods: {
    setSelectedLimit(limit) {
      this.selectedLimit = limit;
    },
    createColumn() {
      let data = {
        boardId: this.boardId,
        columnName: this.columnNameInput,
        wipLimit: this.selectedLimit
      };
      ColumnService.createColumn(data)
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