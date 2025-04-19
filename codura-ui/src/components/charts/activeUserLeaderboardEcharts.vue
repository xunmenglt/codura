<template>
  <div style="width: 100%; height: 400px;" ref="plugInUsagePercentage"></div>
</template>

<script>
import * as echarts from 'echarts';
import { getPlugInUsagePercentage } from '@/api/chart';

export default {
  name: 'activeUserLeaderboardEcharts',
  data() {
    return {
      // option配置
      userInfo: {}, // 存储从接口获取的数据
    };
  },
  mounted() {
    this.initCharts();
  },
  methods: {
    async initCharts() {
      // 获取数据
      const res = await getPlugInUsagePercentage();
      if (res && res.code === 200) {
        this.userInfo = res.data;
      }

      const seriesData = [
        this.userInfo.codeCompletionTokens,
        this.userInfo.codeCompletionQaTokens,
        this.userInfo.testCaseWritingTokens,
        this.userInfo.variableTypeDeclarationTokens,
        this.userInfo.codeExplanationTokens,
        this.userInfo.dcoumentionWritingTokens,
        this.userInfo.codeRefactoringTokens,
        this.userInfo.qucikCodeInsertionTokens,
      ];

      const plugInUsagePercentageChart = echarts.init(this.$refs.plugInUsagePercentage);
      const plugInUsagePercentageOption = {
        title: {
          text: '模块统计（欢迎程度）',
          left: 'center', 
          textStyle: {
            fontSize: 18,
            fontWeight: 'bold',
            color: '#333',
          },
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)',
        },
        legend: {
          type: 'plain', // 改为 'plain'，简化展示
          orient: 'vertical',
          left: 'left', // 右对齐图例
          top: 'middle', // 垂直居中图例
          data: [
            '代码补全', '代码问答', '测试编写', '变量声明', '代码解释', 
            '文档编写', '代码重构', '代码键入',
          ],
          textStyle: {
            color: '#333',
          },
        },
        series: [
          {
            type: 'pie',
            radius: '55%',
            center: ['60%', '50%'], // 将饼图居中
            name: '模块使用情况',
            data: [
              { value: seriesData[0], name: '代码补全' },
              { value: seriesData[1], name: '代码问答' },
              { value: seriesData[2], name: '测试编写' },
              { value: seriesData[3], name: '变量声明' },
              { value: seriesData[4], name: '代码解释' },
              { value: seriesData[5], name: '文档编写' },
              { value: seriesData[6], name: '代码重构' },
              { value: seriesData[7], name: '代码键入' },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
          },
        ],
      };

      plugInUsagePercentageChart.setOption(plugInUsagePercentageOption);
    },
  },
};
</script>

<style scoped></style>
