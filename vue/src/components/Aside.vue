<template>
  <el-row class="tac">
    <el-col :span="12">
      <el-menu
          style="width: 200px;min-height: calc(100vh - 50px)"
          default-active="stock-pool"
          class="el-menu-vertical-demo"
          router
      >
        <el-menu-item index="stock-pool"><span>基金/股票池</span></el-menu-item>
        <el-menu-item index="2"><span>今日计划</span></el-menu-item>
        <el-menu-item index="3"><span>执行列表</span></el-menu-item>
        <el-menu-item index="4"><span>执行结果</span></el-menu-item>
      </el-menu>
    </el-col>
  </el-row>
</template>

<script>

export default {
  name: "Aside",
  data() {
    return {
      user: {}
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)

    // 请求服务端，确认当前登录用户的 合法信息
    // request.get("/user/" + this.user.id).then(res => {
    //   if (res.code === '0') {
    //     this.user = res.data
    //   }
    // })
  }
}
</script>

<style scoped>

</style>
