<template>
  <div class="dashboard">
    <!-- 顶部统计信息区域 -->
    <el-row class="summary-row" gutter="20">
      <el-col :span="6">
        <el-card class="summary-card" shadow="hover">
          <div class="summary-title">总用户数</div>
          <div class="summary-value">{{ totalUsers }}人</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="summary-card" shadow="hover">
          <div class="summary-title">AI使用次数</div>
          <div class="summary-value">{{ totalAiUseTimes }} 次</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="summary-card" shadow="hover">
          <div class="summary-title">总使用时间</div>
          <div class="summary-value">{{ _autoFormatTime(totalUsageTime) }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="summary-card" shadow="hover">
          <div class="summary-title">Token消耗量</div>
          <div class="summary-value">{{ _autoFormatNumber(totalCodeHelpCount) }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表展示区域 -->
    <el-row class="charts-row" gutter="20">
      <el-col :span="12">
        <el-card class="chart-card" shadow="hover">
          <div slot="header">用户使用时间分布</div>
          <DayUsePlugInStatusInMonthEcharts/>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="hover">
          <div slot="header">各活动类型占比</div>
          <ActiveUserLeaderboardEcharts/>
        </el-card>
      </el-col>
    </el-row>

    <!-- 活跃用户前10名 -->
    <el-row class="active-users-row" gutter="20">
      <el-col :span="24">
        <el-card class="chart-card" shadow="hover">
          <div slot="header">用户开发排行榜</div>
          <!-- 悬浮置顶表格 -->
          <ActiveUserLeaderboardTable />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import DayUsePlugInStatusInMonthEcharts from '@/components/charts/dayUsePlugInStatusInMonthEcharts.vue'
import ActiveUserLeaderboardEcharts from '@/components/charts/activeUserLeaderboardEcharts.vue'
import ActiveUserLeaderboardTable from '@/components/charts/activeUserLeaderboardTable.vue'
import { autoFormatNumber,autoFormatTime }from '@/utils/xunmeng'

import {
  getTotalUsers,
  getTotalAiUseTimes,
  getTotalPlugInUseTime,
  getTotalCodeHelpCount
} from '@/api/chart/index.js'
export default {
  name: 'Dashboard',
  components:{
    DayUsePlugInStatusInMonthEcharts,
    ActiveUserLeaderboardEcharts,
    ActiveUserLeaderboardTable
  },
  data() {
    return {
      totalUsers: 0,
      totalAiUseTimes: 0,
      totalUsageTime: 0,
      totalCodeHelpCount:0,
      activeUsers: 0,
      monthlyActiveUsers: 0,
    };
  },
  computed: {
    topActiveUsers() {
      return this.userList.sort((a, b) => b.totalUsageTime - a.totalUsageTime).slice(0, 10);
    }
  },
  mounted() {
    this.initTotalUsers()
    this.initAiUseTimes()
    this.initTotalPlugInUseTime()
    this.initTotalCodeHelpCount()
  },
  methods: {
    _autoFormatNumber(data){
      return autoFormatNumber(data)
    },
    _autoFormatTime(data){
      return autoFormatTime(data)
    },
    async initTotalUsers(){
      let res = await getTotalUsers()
      if(res && res.code==200){
        this.totalUsers=res.data
      }
    },
    async initTotalPlugInUseTime(){
      let res = await getTotalPlugInUseTime()
      if(res && res.code==200){
        this.totalUsageTime=res.data
      }
    },
    async initAiUseTimes(){
      let res = await getTotalAiUseTimes()
      if(res && res.code==200){
        this.totalAiUseTimes=res.data
      }
    },
    async initTotalCodeHelpCount(){
      let res = await getTotalCodeHelpCount()
      if(res && res.code==200){
        this.totalCodeHelpCount=res.data
      }
    },
  }
};
</script>

<style scoped>
.dashboard {
  background-color: #fff;
  padding: 20px;
}

.summary-row {
  margin-bottom: 20px;
}

.summary-card {
  background: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
}

.summary-card:hover {
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
}

.summary-title {
  font-size: 16px;
  color: #333;
}

.summary-value {
  font-size: 24px;
  color: #5C6BC0;
  font-weight: bold;
}

.chart-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.chart-container {
  height: 400px;
}
</style>
