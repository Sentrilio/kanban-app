<template>
  <div class="list-group-item task">{{task.description}}</div>
</template>
<script>
export default {
  props: {
    task: Object
  }
};
</script>
<style lang="css" scoped>
/* .task {
  background-color: white;
  margin: 5px;
} */
</style>