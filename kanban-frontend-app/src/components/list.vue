<template>
  <div class="wrapper">
    <div class="container">
      <input type="text" v-model="bListName" />
      <button>
        <font-awesome-icon icon="minus" />
      </button>
    </div>
    <a>{{blist.position}}</a>
    <div>
      <div v-for="task in tasks" :key="task.id">{{task}}</div>
    </div>

    <button class="btn" data-toggle="collapse" :data-target="'#currentList'+currentblist">
      <font-awesome-icon icon="plus" style="padding-right:5px;" />Create Task
    </button>
    <div :id="'currentList'+currentblist" class="collapse">
      <input type="text" style="margin-top:5px;" placeholder="task description" />
      <button class="button">Create</button>
    </div>
  </div>
</template>

<script>
module.exports = {
  computed: {
    currentblist() {
      return this.$props.blist.listId;
    }
  },
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
  }
};
</script>
<style lang="css" scoped>
.container {
  display: flex;
}
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