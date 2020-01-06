<template>
  <div class="column">
    <div class="column-name">
      <a>{{column.name}}</a>
    </div>
    <draggable
      class="list-group"
      :list="column.tasks"
      group="people"
      @change="change($event, column)"
    >
      <div v-for="task in column.tasks" :key="task.id">
        <task :task="task"></task>
      </div>
    </draggable>

    <div slot="footer" class="create-task" role="group" aria-label="Basic example" key="footer">
      <button class="btn" data-toggle="collapse" :data-target="'#currentColumn'+currentColumn.id">
        <font-awesome-icon icon="plus" style="padding-right:5px;" />Create Task
      </button>
      <div :id="'currentColumn'+currentColumn.id" class="collapse">
        <input type="text" v-model="taskDescription" placeholder="task description" />
        <button class="button" @click="createTask" :disabled="!taskDescription">Create</button>
      </div>
    </div>
  </div>
</template>
<script>
import draggable from "vuedraggable";
import TaskService from "../services/TaskService";
import Operation from "../models/Operation";
import Task from "../components/Task.vue";

export default {
  name: "column",
  display: "Column",
  order: 1,
  props: {
    column: Object
  },
  components: {
    Task,
    draggable
  },
  data() {
    return {
      taskDescription: ""
    };
  },
  computed: {
    currentColumn() {
      return this.$props.column;
    }
  },
  methods: {
  
    createTask() {
      TaskService.createTask(this.column.id, this.taskDescription)
        .then(response => {
          console.log(response);
          this.$emit("refresh");
          this.taskDescription = "";
        })
        .catch(err => {
          console.log(err);
        });
    },

    updateTask(event, column, operation) {
      console.log(event);
      let updateObject = {
        taskId: event.element.id,
        columnId: column.id,
        newIndex: event.newIndex,
        oldIndex: event.oldIndex,
        operation: operation
      };
      TaskService.updateTask(updateObject)
        .then(response => {
          console.log(response);
          this.$emit("refresh");
        })
        .catch(err => {
          console.log(err);
        });
    },
    change: function(evt, column) {
      console.log(column.name);
      if (evt.added) {
        console.log("adding...");
        this.updateTask(evt.added, column, Operation.ADD);
      } else if (evt.moved) {
        console.log("moving...");
        this.updateTask(evt.moved, column, Operation.MOVE);
      }
      // console.log(evt);
      // window.console.log(evt);
    }
  }
};
</script>

<style lang="css" scoped>
.column {
  outline-width: 1px;
  padding: 10px;
  margin: 10px;
  border-radius: 10px;
  background-color: #ebebe0;
  width: 300px;
}
.column-name {
  padding: 10px;
  margin: 10px;
  align-content: center;
}
.create-task {
  padding-top: 10px;
}
</style>