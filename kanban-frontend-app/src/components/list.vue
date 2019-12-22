<template>
  <div class="wrapper">
    <!-- <form> -->
    <input type="text" v-model="bListName" />
    <!-- </form> -->
    <a>{{blist.position}}</a>
    <!-- <button class="btn" data-toggle="collapse" data-target="#demo">Create List</button>
    <div id="demo" class="collapse">
      <input type="text" placeholder="list name" />
    <button class="button">Create List</button>-->
    <!-- </div> -->
    <div class="container">
      <div v-for="task in tasks" :key="task.id">{{task}}</div>
    </div>
    <button class="btn" data-toggle="collapse" data-target="#listId">Create Task</button>
    <div id="listId" class="collapse">
      <input type="text" placeholder="task description" />
      <button class="button">Create</button>
    </div>
  </div>
</template>

<script>
module.exports = {
  props: {
    blist: Object,
    tasks: Array
  },
  data() {
    return {
      bListName: ""
    };
  },

  created() {
    this.bListName = this.$props.blist.name;
    console.log(this.bListName);
  }
};
</script>
<style lang="css" scoped>
.wrapper {
  padding: 20px;
  border-style: solid;
  /* padding-left: 20px; */
  margin-left: 20px;
}
input {
  border-style: none;
  /* border-top-style: hidden; */
  /* border-right-style: hidden; */
  /* border-left-style: hidden; */
  /* border-bottom-style: groove; */
  /* background-color: #eee; */
}
</style>