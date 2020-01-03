<template>
  <div>
    <a>{{member.user.username}}</a>
  </div>
</template>

<script>
export default {
  name: "TeamMember",
  props: {
    member: Object
  }
};
</script>
<style lang="css" scoped>
</style>