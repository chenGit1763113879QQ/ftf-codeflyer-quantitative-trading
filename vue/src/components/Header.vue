<template>
  <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex">
    <div style="width: 400px; padding-left: 30px; font-weight: bold; color: dodgerblue">CodeFlyer Quantitative Trading
      System
    </div>
    <div style="flex: 1"></div>
    <div style="width: 130px">
      <el-dropdown>
        <span class="el-dropdown-link">
          <el-avatar :size="30" :src="user.avatar" style="position: relative; top: 10px"></el-avatar>
             &nbsp {{ user.nickName }}
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/login')">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: ['user'],
  data() {
    return {}
  },
  created() {
  }
}
</script>

<style scoped>

</style>
