<template>
  <div style="width: auto;height: 400px" ref="dayUsePlugInStatusInMonth">
  </div>
</template>

<script>
import * as echarts from 'echarts';

import { getDayUsePlugInStatusInMonth } from '@/api/chart'
import { formatSecondsToHours } from '@/utils/xunmeng'

export default {
  name: 'dayUsePlugInStatusInMonthEcharts',
  data() {
    return {
      // option配置
      userInfoList: [], // 存储从接口获取的数据
    }
  },
  mounted() {
    this.initCharts()
  },
  methods: {
    async initCharts() {
      // 获取数据
      const res = await getDayUsePlugInStatusInMonth()
      if (res && res.code == 200) {
        this.userInfoList = res.data;
      }

      // 计算展示前10条数据的百分比
      const totalData = this.userInfoList.length;
      const initialEnd = totalData > 0 ? (10 / totalData) * 100 : 10; // 如果没有数据，显示10%的比例

      const usageTimeChart = echarts.init(this.$refs.dayUsePlugInStatusInMonth);
      const usageTimeOption = {
        title: {
          text: '平台用户专注代码时间（月度汇总）',
          left: 'left',
          textStyle: {
            fontSize: 18,
            fontWeight: 'bold',
            color: '#333'
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['活跃时间', '使用时间'],
          left: 'right',
        },
        xAxis: {
          type: 'category',
          data: this.userInfoList.map(info => info.createTime.split('-')[1]+"-"+info.createTime.split('-')[2]),          
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
            type: 'bar',  //柱状图
            itemStyle: {
              color: '#5C6BC0'
            },
            label: {
              show: true,
              position: 'top',
              color: '#333'
            },
            name:'活跃时间'
          },
          {
            data: this.userInfoList.map(info => formatSecondsToHours(info.editorUsageTime)),
            type: 'line',  // 折线图
            smooth: true,  // 平滑折线
            lineStyle: {
              color: '#FF6F61',  // 折线颜色
              width: 2
            },
            symbol: 'circle',  // 折线的节点
            symbolSize: 8,  // 节点大小
            label: {
              show: true,
              position: 'top',
              color: '#FF6F61'
            },
            name:'使用时间'
          }
        ],
        grid: { left: '10%', right: '10%', bottom: '10%' },
        dataZoom: [
          {
            type: 'slider', // 滑动条
            show: true,
            start: 0, // 起始点
            end: initialEnd, // 默认显示前10条数据
            xAxisIndex: [0],
            handleSize: '16%', // 滑块的宽度
            height: '8px', // 设置滑动条的高度为8px
            handleStyle: {
              color: '#5C6BC0' // 滑动条滑块的颜色
            },
            fillerColor: 'rgba(182, 199, 255, 0.4)'  // 滑动条的填充色
          },
          {
            type: 'inside', // 内部滑动
            xAxisIndex: [0],
            start: 0,
            end: initialEnd
          }
        ]
      };
      usageTimeChart.setOption(usageTimeOption);
    }
  }
}
</script>

<style scoped></style>
