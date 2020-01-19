<template>
  <div class="column">
    <div class="column-name">
      <a>
        {{column.name}}
        limit: {{column.wipLimit}}
      </a>
    </div>
    <div v-if="limitReached">
      <draggable
        class="list-group"
        :list="column.tasks"
        :group="{name: 'task',put: false}"
        @change="change($event, column)"
      >
        <div v-for="task in column.tasks" :key="task.id">
          <task :task="task"></task>
        </div>
      </draggable>
    </div>
    <div v-else>
      <draggable
        class="list-group"
        :list="column.tasks"
        :group="{name:'task'}"
        @change="change($event, column)"
      >
        <div v-for="task in column.tasks" :key="task.id">
          <task :task="task"></task>
        </div>
      </draggable>
    </div>
    <!-- v-if="!limitReached" -->
    <div slot="footer" class="create-task" role="group" aria-label="Basic example" key="footer">
      <!-- <button class="btn" data-toggle="collapse" :data-target="'#currentColumn'+currentColumn.id"> -->
      <button
        class="btn"
        :disabled="limitReached"
        data-toggle="collapse"
        :data-target="'#currentColumn'+currentColumn.id"
      >
        <font-awesome-icon icon="plus" style="padding-right:5px;" />Create Task
      </button>
      <div :id="'currentColumn'+currentColumn.id" class="collapse">
        <input
          type="text"
          class="task-input"
          v-model="taskDescription"
          placeholder="task description"
        />

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
import ColumnService from "../services/ColumnService";

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
    limitReached() {
      return ColumnService.isLimitReached(this.column);
    },
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
        })
        .catch(err => {
          console.log(err);
        });
    },
    change: function(evt, column) {
      if (evt.added) {
        this.updateTask(evt.added, column, Operation.ADD);
      } else if (evt.moved) {
        this.updateTask(evt.moved, column, Operation.MOVE);
      }
    }
  }
};
</script>

<style lang="css" scoped>
.column {
  margin-left: 10px;
  outline-width: 1px;
  padding: 10px;
  border-radius: 10px;
  background-color: #ebebe0;
  /* background-color: red; */

  width: 250px;
}
.column-name {
  margin: 10px;
  align-content: center;
}
.task-input {
  margin-top: 8px;
}
</style>