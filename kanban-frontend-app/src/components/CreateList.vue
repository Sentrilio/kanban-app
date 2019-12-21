<template>
  <div class="my-container">
    <!-- <link rel="stylesheet" type="text/css" href="css/bootstrap.css" /> -->
    <!-- <link rel="stylesheet" type="text/css" href="css/style.css" /> -->
    <div class="myGrid">
      <!-- <input class="myinput1" type="text" value="lista 1" />
      <br> -->
      <button class="btn" data-toggle="collapse" data-target="#demo">Create List</button>
      <div id="demo" class="collapse">
        <input type="text" placeholder="list name" />
        <button class="button">Create List</button>
      </div>
    </div>
    <!-- <div>
      <b-button
        :class="visible ? null : 'collapsed'"
        :aria-expanded="visible ? 'true' : 'false'"
        aria-controls="collapse-4"
        @click="visible = !visible"
      >Toggle Collapse</b-button>
      <b-collapse id="collapse-4" v-model="visible" class="mt-2">
        <input type="text" />
        <button>Create</button>
      </b-collapse>
    </div>
    <div>
      <b-button v-b-toggle.collapse-1 variant="primary">Toggle Collapse</b-button>
      <b-collapse id="collapse-1" class="mt-2">
        <b-card>
          <p class="card-text">Collapse contents Here</p>
          <b-button v-b-toggle.collapse-1-inner size="sm">Toggle Inner Collapse</b-button>
          <b-collapse id="collapse-1-inner" class="mt-2">
            <b-card>Hello!</b-card>
          </b-collapse>
        </b-card>
      </b-collapse>
    </div>-->
    <!-- <div class="container">
      <div v-for="task in tasks" :key="task.id">{{task}}</div>
    </div> -->
  </div>
</template>

<script>
module.exports = {
  props: {
    tasks: Array
  },
  data() {
    return {
      visible: false
    };
  }
};
</script>
<style lang="css" scoped>
.my-container{
  align-content: right;
}
.myinput1 {
  border: none;
}
.myInput1:focus {
  border: 1px;
}
/* .collapseButton {
  background-color: green;
  width: 250px;
} */
.myGrid {
  padding: 5px;
  outline-style: solid;
  outline-width: 1px !important;
  outline-color: black !important;
  width: 100px;
}
.btn {
  background-color: #888888;
}
.button {
  /* background-color:#888888; */
  background-color: #4cd137;
  color: black;
}
.btn:hover {
  color: black;
}
</style>