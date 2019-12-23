<template>
  <div class="wrapper">
    <div class="container">
      <input type="text" v-model="columnName" />
      <button>
        <font-awesome-icon icon="minus" />
      </button>
    </div>
    <a>{{currentColumn.position}}</a>
    <div>
      <div v-for="task in currentColumn.tasks" :key="task.id">{{task.description}}</div>
    </div>

    <button class="btn" data-toggle="collapse" :data-target="'#currentColumn'+currentColumn.id">
      <font-awesome-icon icon="plus" style="padding-right:5px;" />Create Task
    </button>
    <div :id="'currentColumn'+currentColumn.id" class="collapse">
      <input
        type="text"
        style="margin-top:5px;"
        v-model="taskDescription"
        placeholder="task description"
      />
      <button class="button"  @click="createTask"  :disabled="!taskDescription">Create</button>
    </div>
  </div>
</template>

<script>
import UserService from "../services/user.service";

export default {
  data() {
    return {
      taskDescription: "",
      columnName: "",
    };
  },
  props: {
    column: Object
  },
  computed: {
    currentColumn() {
      return this.$props.column;
    },
    currentColumnId() {
      return this.column.id;
    }
  },
  methods: {
    createTask() {
      UserService.createTask(this.column.id, this.taskDescription)
        .then(response => {
          console.log(response);
          this.$emit("refresh");
          this.taskDescription="";
        })
        .catch(err => {
          console.log(err);
        });
    }
  },

  created() {
    // this.column = this.$props.currentColumn;
    this.columnName = this.column.name;
  }
};
</script>
<style lang="css" scoped>
.container {
  display: flex;
}
.wrapper {
  padding: 20px;
  border-style: solid;
  /* padding-left: 20px; */
  margin-left: 20px;
}
input {
  border-style: none;
  /* border-top-style: hidden; */
  /* border-right-style: hidden; */
  /* border-left-style: hidden; */
  /* border-bottom-style: groove; */
  /* background-color: #eee; */
}
</style>