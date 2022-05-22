<template>
  <div style="padding: 10px">

    <!--    功能区域-->
    <div style="margin: 10px 0;float: left;margin-left: 10px">
      <el-button type="primary" @click="add">添加</el-button>
<!--      <el-upload-->
<!--        action="http://localhost:9090/user/import"-->
<!--        :on-success="handleUploadSuccess"-->
<!--        :show-file-list=false-->
<!--        :limit="1"-->
<!--        accept='.xlsx'-->
<!--        style="display: inline-block; margin: 0 10px"-->
<!--      >-->
<!--        <el-button type="primary">导入</el-button>-->
<!--      </el-upload>-->
<!--      <el-button type="primary" @click="exportUser">导出</el-button>-->
    </div>

    <!--    搜索区域-->
    <div style="margin: 10px 0; float: left;width: 800px;margin-left: 20px">
      <el-input v-model="search" placeholder="请输入代码或名称" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">模糊查询</el-button>
    </div>
    <el-table
        :data="tableData"
        border
        stripe
        style="width: 80%">
      <el-table-column width="100px"
          prop="id"
          label="ID"
          sortable
      >
      </el-table-column>
      <el-table-column width="100px"
          prop="stockCode"
          label="代码">
      </el-table-column>
      <el-table-column width="130px"
          prop="stockName"
          label="名称">
      </el-table-column>
      <el-table-column width="100px"
          prop="price"
          label="现价(分)">
      </el-table-column>
      <el-table-column width="200px"
          prop="createTime"
          label="添加时间">
      </el-table-column>
      <el-table-column width="100px"
          prop="status"
          label="上下线状态">
      </el-table-column>

      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-popconfirm title="确定操作？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button style="margin-left: 8px" size="mini" type="danger">上下线</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="基金/股票添加" v-model="dialogVisible" width="30%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="基金/股票代码">
          <el-input v-model="form.stockCode" style="width: 80%"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>


import request from "@/utils/request";

export default {
  name: 'Home',
  components: {},
  data() {
    return {
      loading: true,
      form: {},
      dialogVisible: false,
      bookVis: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      bookList: [],
      roles: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    handleChange(row) {
      request.put("/user/changeRole", row).then(res => {
        if (res.code === '0') {
          this.$message.success("更新成功")
          if (res.data) {
            this.$router.push("/login")
          }
        }
      })
    },
    showBooks(books) {
      this.bookList = books
      this.bookVis = true
    },
    load() {
      this.loading = true
      request.get("/stock/list", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.loading = false
        this.tableData = res.data.records
        this.total = res.data.total
      })

      request.get("/role/all").then(res => {
        this.roles = res.data
      })
    },
    handleUploadSuccess(res) {
      if (res.code === "0") {
        this.$message.success("导入成功")
        this.load()
      }
    },
    exportUser() {
      location.href = "http://" + window.server.filesUploadUrl + ":9090/user/export";
    },
    add() {
      this.dialogVisible = true
      this.form = {}
    },
    save() {
        request.post("/stock/add?stockCode="+this.form.stockCode).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }

          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleDelete(id) {
      console.log(id)
      request.post("/stock/status?id=" + id).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "操作成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()  // 删除之后重新加载表格的数据
      })
    },
    handleSizeChange(pageSize) {   // 改变当前每页的个数触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>
