<template>
  <div class="list-group-item btn task" @click="logTask(task)">
    <div class="task-description">{{task.description}}</div>
    <div v-if="task.importance>0" class="task-icon">
      <font-awesome-icon class="icon" v-bind:class="taskClass" icon="circle" />
    </div>
    <!-- imp:{{task.importance}} -->
    <!-- <font-awesome-icon icon="minus" /> -->
  </div>
</template>
<script>
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
}
.icon {
  vertical-align: middle;

  /* align-content: center; */
}
div.task-icon {
  vertical-align: middle;
  line-height: normal;
  /* align-content: center; */
}
div.task-description {
  flex: 0 0 170px;
  /* margin-bottom: 10px; */
  text-align: left;
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
</style>