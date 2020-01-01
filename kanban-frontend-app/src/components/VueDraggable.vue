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
import UserService from "../services/user.service";

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
      UserService.createTask(this.column.id, this.taskDescription)
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
    change: function(evt, column) {
      console.log(column.name);
      if (evt.added) {
        // console.log("column:")
        // console.log(column);
        this.$emit("boardUpdate");
      } else if (evt.moved) {
        console.log(column.tasks)
        // console.log("column:")
        // console.log(column);
        // this.$emit("taskUpdate",)
        this.$emit("boardUpdate");
      } else if (evt.removed) {
        // this.updatePositions(fromIndex,column.id)
        // console.log("removed");
      }
      console.log("evt:");
      console.log(evt);
      // window.console.log("evt: " + evt);
      // window.console.log(evt);
    },
    // updatePositions(fromIndex,columnId){
      // UserService.updatePositionsInColumn()
    // }
  }
};
</script>