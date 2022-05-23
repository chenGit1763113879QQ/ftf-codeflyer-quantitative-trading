<template>
  <div style="padding: 10px">

    <!--    功能区域-->
<!--    <div style="margin: 10px 0;float: left;margin-left: 10px">-->
<!--      <el-button type="primary" @click="add">添加</el-button>-->
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
<!--    </div>-->

<!--    &lt;!&ndash;    搜索区域&ndash;&gt;-->
<!--    <div style="margin: 10px 0; float: left;width: 800px;margin-left: 20px">-->
<!--      <el-input v-model="search" placeholder="请输入代码或名称" style="width: 20%" clearable></el-input>-->
<!--      <el-button type="primary" style="margin-left: 5px" @click="load">模糊查询</el-button>-->
<!--    </div>-->
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
      <el-table-column width="150px"
          prop="tradePrice"
          label="交易价格(一手)">
      </el-table-column>
      <el-table-column width="200px"
          prop="tradeTime"
          label="计划时间">
      </el-table-column>
      <el-table-column width="100px"
          prop="amount"
          label="数量(手)">
      </el-table-column>

<!--      <el-table-column label="操作" width="200">-->
<!--        <template #default="scope">-->
<!--          <el-popconfirm title="确定操作？" @confirm="handleDelete(scope.row.id)">-->
<!--            <template #reference>-->
<!--              <el-button style="margin-left: 8px" size="mini" type="danger">上下线</el-button>-->
<!--            </template>-->
<!--          </el-popconfirm>-->
<!--        </template>-->
<!--      </el-table-column>-->
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
      pageSize: 20,
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
    load() {
      this.loading = true
      request.get("/stock/trade-decision/list", {
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
    },

    add() {
      this.dialogVisible = true
      this.form = {}
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
