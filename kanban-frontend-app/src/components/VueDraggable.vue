<template>
  <div>
    <draggable
      class="list-group"
      :list="column.tasks"
      group="people"
      @change="change($event, column)"
    >
      <!-- <transition-group> -->
      <div
        class="list-group-item"
        v-for="(element, index) in column.tasks"
        :key="element.id"
      >{{ element.description }} index: {{ index }} position: {{element.position}}</div>
      <!-- </transition-group> -->
    </draggable>

    <div
      slot="footer"
      class="btn-group list-group-item"
      role="group"
      aria-label="Basic example"
      key="footer"
    >
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
        <button class="button" @click="createTask" :disabled="!taskDescription">Create</button>
      </div>
    </div>
  </div>
</template>
<script>
import draggable from "vuedraggable";
import TaskService from "../services/TaskService";
import Operation from "../models/Operation";

export default {
  name: "column",
  display: "Column",
  order: 1,
  props: {
    column: Object
  },
  components: {
    draggable
  },
  computed: {
    currentColumn() {
      return this.$props.column;
    }
  },
  data() {
    return {
      taskDescription: "",
      list1: [
        { name: "John", id: 1 },
        { name: "Joao", id: 2 },
        { name: "Jean", id: 3 },
        { name: "Gerard", id: 4 }
      ],
      list2: [
        { name: "Juan", id: 5 },
        { name: "Edgard", id: 6 },
        { name: "Johnson", id: 7 }
      ]
    };
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
    add: function() {
      this.list.push({ name: "Juan" });
    },
    replace: function() {
      this.list = [{ name: "Edgard" }];
    },
    clone: function(el) {
      return {
        name: el.name + " cloned"
      };
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
      } else if (evt.removed) {
        console.log("removing...");
        // setTimeout(function() {
          // this.updateTask(evt.removed, column, Operation.REMOVE);
        // }, 10000);
      }
      // console.log("evt:");
      // console.log(evt);
      // window.console.log(evt);
    }
  }
};
</script>