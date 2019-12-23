<template>
  <div class="wrapper">
    <div class="container">
      <input type="text" v-model="columnName" />
      <button>
        <font-awesome-icon icon="minus" />
      </button>
    </div>
    <a>{{column.position}}</a>
    <div>
      <div v-for="task in tasks" :key="task.id">{{task}}</div>
    </div>

    <button class="btn" data-toggle="collapse" :data-target="'#currentColumn'+currentColumn">
      <font-awesome-icon icon="plus" style="padding-right:5px;" />Create Task
    </button>
    <div :id="'currentColumn'+currentColumn" class="collapse">
      <input
        type="text"
        style="margin-top:5px;"
        v-model="taskDescription"
        placeholder="task description"
      />
      <button class="button" >Create</button>
    </div>
  </div>
</template>

<script>
import UserService from '../services/user.service';

export default {
  data() {
    return {
      columnName: "",
      taskDescription: "",
    };
  },
  props: {
    column: Object,
    tasks: Array
  },
  methods: {
    createTask(boardId,taskDescription) {
      UserService.createTask(boardId,taskDescription)
      .then(response=>{
        console.log(response.data);
      })
    }
  },
  computed: {
    currentColumn() {
      return this.$props.column.id;
    }
  },
  created() {
    this.columnName = this.$props.column.name;
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