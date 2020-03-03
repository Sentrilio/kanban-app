<template>
  <div class="column">
    <div class="column-header">
      <div class="column-name">{{getColumnName}}</div>

      <input
        min="0"
        type="number"
        class="limit"
        @keypress="isNumber($event)"
        v-on:blur="handleBlur"
        v-on:keyup.enter="onEnter"
        :value="getColumnLimit"
      />
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
        :group="{name: 'task', put: false}"
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
    <div slot="footer" class="create-task" role="group" aria-label="Basic example" key="footer">
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
      selectedLimit: "",
      taskDescription: ""
    };
  },
  computed: {
    getColumnLimit() {
      return this.column.wipLimit;
    },
    getColumnName() {
      let columnName = this.column.name;
      if (columnName.length > 14) {
        return columnName.substring(0, 14) + "...";
      } else {
        return columnName;
      }
    },
    limitReached() {
      return ColumnService.isLimitReached(this.column);
    },
    currentColumn() {
      return this.$props.column;
    }
  },
  methods: {
    setColumnLimit(limit) {
      this.column.wipLimit = limit;
    },
    handleBlur(event) {
      event.srcElement.value = this.getColumnLimit;
    },
    onEnter(event) {
      let newLimit = Number.parseInt(event.srcElement.value);
      if (newLimit >= 0) {
        this.setColumnLimit(newLimit);
        this.setSelectedLimit(newLimit);
        event.target.blur();
      }
    },
    onSubmit(event) {
      console.log(event);
    },
    picked: function(item) {
      this.selected = item;
      console.log(item);
    },

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
    isNumber: function(evt) {
      evt = evt ? evt : window.event;
      var charCode = evt.which ? evt.which : evt.keyCode;
      if (
        charCode > 31 &&
        (charCode < 48 || charCode > 57) &&
        charCode !== 46
      ) {
        evt.preventDefault();
      } else {
        return true;
      }
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
          this.$emit("refresh");
          console.log(err);
        });
    },
    updateTask(event, column, operation) {
      let updateObject = {
        taskId: event.element.id,
        columnId: column.id,
        newIndex: event.newIndex,
        oldIndex: event.oldIndex,
        operation: operation
      };
      console.log(event);
      console.log(updateObject);

      if (updateObject.newIndex >= 0) {
        TaskService.updateTask(updateObject)
          .then(response => {
            console.log(response.status);
            // this.$emit("refresh");
          })
          .catch(err => {
            this.$emit("refresh");
            console.log("error:" + err);
          });
      } 
      else {
        this.$emit("refresh");
        console.log("new index < 0");
        console.log("proper new index: " + updateObject.newIndex);

        // let properIndex = this.column.tasks.findIndex(
          // x => x.id === event.element.id
        // );
        // updateObject.newIndex = properIndex;
        // if (updateObject.newIndex >= 0) {
        //   TaskService.updateTask(updateObject)
        //     .then(response => {
        //       console.log(response.status);
        //       this.$emit("refresh");
        //     })
        //     .catch(err => {
        //       this.$emit("refresh");
        //       console.log("error:" + err);
        //     });
        // } else {
        //   this.$emit("refresh");
        //   console.log("proper index is still < 0");
        // }
      }
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
  width: 200px;
  max-width: 133px;
}
#dropdownMenuButton {
  margin-right: 0 !important;
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
.limit {
  width: 45px;
  text-align: center;
  background-color: #ebebe0;
  border: none;
}
.limit:focus {
  background-color: white;
}
</style>