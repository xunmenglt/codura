<template>
  <div style="width: auto;height: 400px" ref="getUserWeekInfo"></div>
</template>

<script>
import * as echarts from 'echarts'
import { getUserWeekInfo} from "@/api/uselog/userUseInfo";
import { formatSecondsToHours } from '@/utils/xunmeng'

export default {
  name: 'WeekUserUseStatusEcharts',
  data() {
    return {
      userInfoList: []
    }
  },
  mounted() {
    this.initCharts()
  },
  methods: {
    async initCharts() {
      const res = await getUserWeekInfo()
      if (res && res.code === 200 && Array.isArray(res.data)) {
        this.userInfoList = res.data
      }

      const chart = echarts.init(this.$refs.getUserWeekInfo)
      const usageTimeOption = {
        title: {
          text: '本周专注时间（每日统计）',
          left: 'left',
          textStyle: {
            fontSize: 18,
            fontWeight: 'bold',
            color: '#333'
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        legend: {
          data: ['活跃时间', '使用时间'],
          left: 'right'
        },
        xAxis: {
          type: 'category',
          data: this.userInfoList.map(info => {
            const date = new Date(info.createTime)
            return `${date.getMonth() + 1}-${date.getDate()}`
          }),
          axisLabel: {
            rotate: 45,
            textStyle: { color: '#333' }
          },
          axisLine: { lineStyle: { color: '#ccc' } }
        },
        yAxis: {
          type: 'value',
          axisLabel: { formatter: '{value} 小时' },
          axisLine: { lineStyle: { color: '#ccc' } }
        },
        series: [
          {
            data: this.userInfoList.map(info => formatSecondsToHours(info.editorActiveTime)),
            type: 'bar',
            itemStyle: { color: '#5C6BC0' },
            label: {
              show: true,
              position: 'top',
              color: '#333'
            },
            name: '活跃时间'
          },
          {
            data: this.userInfoList.map(info => formatSecondsToHours(info.editorUsageTime)),
            type: 'line',
            smooth: true,
            lineStyle: {
              color: '#FF6F61',
              width: 2
            },
            symbol: 'circle',
            symbolSize: 8,
            label: {
              show: true,
              position: 'top',
              color: '#FF6F61'
            },
            name: '使用时间'
          }
        ],
        grid: { left: '10%', right: '10%', bottom: '10%' }
      }

      chart.setOption(usageTimeOption)
    }
  }
}
</script>

<style scoped></style>
