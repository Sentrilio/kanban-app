<template>
  <div id="app">
    <kanban-board :stages="stages" :blocks="blocks" @update-block="updateBlock">
      <div v-for="block in blocks" :slot="block.id" :key="block.id">
        <div>
          <strong>id:</strong>
          {{ block.id }}
        </div>
        <div>{{ block.title }}</div>
      </div>
    </kanban-board>
  </div>
</template>

<script>
import Vue from "vue";
import vueKanban from "vue-kanban";

Vue.use(vueKanban);

export default {
  name: "app",
  data() {
    return {
      stages: ["on-hold", "in-progress", "needs-review", "approved"],
      blocks: [
        {
          id: 0,
          status: "approved",
          title: "Buy coffe machine"
        },
        {
          id: 1,
          status: "in-progress",
          title: "Find better AirBnb options"
        }
      ],
      listPrimitive: null
    };
  },
  methods: {
    updateBlock(id, status) {
      console.log("id: " + id);
      console.log("status: " + status);
      //TODO: add code to update the block
    }
  }
};
</script>

<style lang="scss">
@import "../assets/kanban.scss";
</style>