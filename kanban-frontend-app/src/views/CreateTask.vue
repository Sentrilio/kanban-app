<template>
  <div class="container" v-if="currentUser">
    <form @submit.prevent="createBoard">
      <input type="text" placeholder="description" v-model="description" />
      <input type="text" placeholder="content" v-model="content" />
      <button :disabled="!selectedTeam || !boardName" type="submit">Create task</button>
    </form>
  </div>
</template>

<script>
</script>
<style lang="scss" scoped>
</style>