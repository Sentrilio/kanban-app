<template>
  <div>
    <div v-for="task in tasks" :key="task.id">{{task}}</div>
  </div>
</template>

<script>
module.exports = {
  props: {
    tasks: Array
  },
  data: function() {
    return {
    };
  }
};
</script>
<style lang="scss" scoped>
</style>