<template>
  <div class="list-group-item btn task" @click="logTask(task)">
    {{task.description}} imp:{{task.importance}}
    <!-- <font-awesome-icon icon="minus" /> -->
  </div>
</template>
<script>
export default {
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
  text-align: left;
  margin-bottom: 10px;
}
</style>