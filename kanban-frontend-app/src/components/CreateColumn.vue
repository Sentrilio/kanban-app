<template>
  <div class="create-column">
    <div>
      <button class="btn" data-toggle="collapse" data-target="#demo">Dodaj kolumnę</button>
      <div id="demo" class="collapse">
        <input v-model="columnNameInput" type="text" placeholder="nazwa kolumny" />
        <div class="dropdown">
          limit
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
            <div v-for="index in 15" :key="index">
              <div class="dropdown-item btn" @click="setSelectedLimit(index)">{{index}}</div>
            </div>
          </div>
        </div>
        <br />
        <button class="button" @click="createColumn" :disabled="!columnNameInput">Dodaj</button>
      </div>
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
  computed: {},
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
input {
  margin-top: 10px;
}
div.dropdown {
  padding-top: 10px;
  padding-left: 5px;
}
.create-column {
  width: 100px;
  align-content: right;
  padding-left: 20px;
  margin-left: 10px;
  padding-right: 10px;
  outline-color: black;
  width: 200px;
  /* height: 100px; */
  outline-width: 1px;
  padding: 10px;
  border-radius: 10px;
  background-color: #ebebe0;
}

.btn {
  background-color: #888888;
}
div.btn {
  background-color: white;
}
div.btn:hover {
  background-color: #ebebe0;
}

.btn:hover {
  color: black;
}
</style>