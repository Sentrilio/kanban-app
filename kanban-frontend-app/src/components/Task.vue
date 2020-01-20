<template>
  <div @click="logTask(task)" class="list-group-item btn task">
    <div class="task-description">{{task.description}}</div>
    <div class="task-icon">
      <font-awesome-icon class="icon" v-bind:class="taskClass" icon="circle" />
    </div>
    <!-- imp:{{task.importance}} -->
    <div @click="deleteTask(task)">
      <font-awesome-icon icon="minus" />
    </div>
  </div>
</template>
<script>
import TaskService from "../services/TaskService";
export default {
  computed: {
    taskClass: function() {
      if (this.task.importance === 1) {
        return "yellow";
      } else if (this.task.importance === 2) {
        return "orange";
      } else if (this.task.importance === 3) {
        return "red";
      }
      return "white";
    }
  },
  props: {
    task: Object
  },
  methods: {
    deleteTask(task) {
      TaskService.deleteTask(task.id)
        .then(response => {
          console.log(response);
        })
        .catch(err => {
          console.log(err.response.body);
        });
    },
    logTask(task) {
      console.log("clicked" + task.id);
      //to do - add window prompt with task settings
    }
  }
};
</script>
<style lang="css" scoped>
.task {
  display: flex;
  flex-direction: row;
  margin-bottom: 5px;
  margin-right: 5px;
  padding-right: 5px;
}
.icon {
  vertical-align: middle;

  /* align-content: center; */
}
div.task-icon {
  flex: 0 0 40px;
  vertical-align: middle;
  line-height: normal;
  /* align-content: center; */
}
div.task-description {
  flex: 0 0 143px;
  /* margin-bottom: 10px; */
  text-align: left;
}
div.list-group-item.btn.task{
  padding-right: 10px;
  margin-right: 0px !important;
}

/* .red.yellow.orange { */
/* } */
.red {
  color: red;
  /* align-content: right; */
}
.yellow {
  color: yellow;
}
.orange {
  color: orange;
}
.white {
  color: white;
}
</style>