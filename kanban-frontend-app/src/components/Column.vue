<template>
  <div class="column">
    <div class="column-header">
      <div class="column-name">
        {{column.name}}
        <!-- limit: {{column.wipLimit}} -->
      </div>
      <div class="dropdown">
        <button
          class="btn btn-secondary dropdown-toggle"
          type="button"
          id="dropdownMenuButton"
          data-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false"
          :v-model="column.wipLimit"
        >{{column.wipLimit}}</button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
          <div class="drp-items" v-for="index in 15" :key="index">
            <div class="dropdown-item btn" @click="setSelectedLimit(index)">{{index}}</div>
          </div>
        </div>
      </div>
      <div class="btn minus-icon" @click="deleteColumn(column)">
        <div>
          <font-awesome-icon icon="minus" />
        </div>
      </div>
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
        <font-awesome-icon icon="plus" style="padding-right:5px;" />Dodaj zadanie
      </button>
      <div :id="'currentColumn'+currentColumn.id" class="collapse">
        <input type="text" class="task-input" v-model="taskDescription" placeholder="opis zadania" />

        <button class="button" @click="createTask" :disabled="!taskDescription">Dodaj</button>
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
    deleteColumn(column) {
      ColumnService.deleteColumn(column.id)
        .then(response => {
          this.$emit("refresh");
          console.log(response);
        })
        .catch(err => {
          console.log(err.response);
        });
    },
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
    setSelectedLimit(limit) {
      let data = {
        columnId: this.column.id,
        limit: limit
      };
      ColumnService.updateColumnLimit(data)
        .then(response => {
          console.log(response);
          this.$emit("refresh");
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
div.column-name {
  padding-left: 10px;
  width: 300px;
}

.column {
  margin-left: 10px;
  outline-width: 1px;
  padding: 10px;
  border-radius: 10px;
  background-color: #ebebe0;
  /* background-color: red; */

  width: 250px;
}
div.minus-icon {
  padding-right: 0px;
  /* padding-right: 10px; */
  /* align-content: center; */
}

div.column-header {
  /* flex: 0 0 100%;  */
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.task-input {
  margin-top: 8px;
}
</style>