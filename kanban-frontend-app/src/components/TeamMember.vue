<template>
  <div class="member">
    <a>{{member.user.username}}</a>
    <a>{{member.role}}</a>
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
.member {
  display: grid;

  margin-top: 10px;
  text-align: left;
  border-top: 1px solid;
  /* grid-template-columns: repeat(3, 1000fr); */
  grid-template-columns: 700px 350px 
  

}
</style>