<template>
  <div class="app-container home-dashboard">
    <div class="dashboard-header">
      <div class="header-content">
        <h1>体系协同多源识别算法分析优化软件</h1>
        <p class="subtitle">System Collaborative Multi-Source Identification Algorithm Analysis & Optimization</p>
      </div>
    </div>

    <div class="dashboard-body">

      <div class="top-section">
        <el-card shadow="hover" class="workflow-card">
          <template #header>
            <div class="card-header">
              <span><el-icon><Share /></el-icon> 工作流演示 (Workflow Demonstration)</span>
              <el-tag effect="dark" type="success">Running</el-tag>
            </div>
          </template>
          <div ref="workflowChartRef" class="chart-container workflow-chart"></div>
        </el-card>
      </div>

      <div class="bottom-section">
        <el-card shadow="hover" class="resource-card-container">
          <template #header>
            <div class="card-header">
              <span><el-icon><Monitor /></el-icon> 系统资源概览 (System Resources)</span>
              <el-button link type="primary" icon="Refresh">刷新</el-button>
            </div>
          </template>

          <el-row :gutter="20" class="resource-row">
            <el-col :span="6">
              <div class="monitor-item">
                <div class="monitor-title">CPU 负载</div>
                <div ref="cpuChartRef" class="monitor-chart"></div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="monitor-item">
                <div class="monitor-title">GPU 加速计算</div>
                <div ref="gpuChartRef" class="monitor-chart"></div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="monitor-item">
                <div class="monitor-title">内存使用率</div>
                <div ref="memChartRef" class="monitor-chart"></div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="monitor-item">
                <div class="monitor-title">网络吞吐量 (I/O)</div>
                <div ref="netChartRef" class="monitor-chart"></div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </div>

    </div>
  </div>
</template>

<script setup name="Index">
import { ref, onMounted, onBeforeUnmount } from 'vue';
import * as echarts from 'echarts';

// DOM 引用
const workflowChartRef = ref(null);
const cpuChartRef = ref(null);
const gpuChartRef = ref(null);
const memChartRef = ref(null);
const netChartRef = ref(null);

// ECharts 实例
let workflowChart = null;
let cpuChart = null;
let gpuChart = null;
let memChart = null;
let netChart = null;

// 定时器用于模拟数据跳动
let timer = null;

// 初始化工作流图表
const initWorkflowChart = () => {
  if (!workflowChartRef.value) return;
  workflowChart = echarts.init(workflowChartRef.value);

  // 模拟节点数据
  const nodes = [
    { name: '多源数据接入', x: 100, y: 300, category: 0, symbolSize: 50 },
    { name: '雷达信号', x: 300, y: 100, category: 1, symbolSize: 40 },
    { name: '红外图像', x: 300, y: 300, category: 1, symbolSize: 40 },
    { name: '无线电频谱', x: 300, y: 500, category: 1, symbolSize: 40 },
    { name: '数据预处理', x: 500, y: 300, category: 2, symbolSize: 50 },
    { name: '特征提取', x: 700, y: 300, category: 2, symbolSize: 50 },
    { name: '协同识别算法', x: 900, y: 300, category: 3, symbolSize: 60 },
    { name: '结果优化', x: 1100, y: 300, category: 3, symbolSize: 50 },
    { name: '最终输出', x: 1300, y: 300, category: 4, symbolSize: 50 },
  ];

  const links = [
    { source: '多源数据接入', target: '雷达信号' },
    { source: '多源数据接入', target: '红外图像' },
    { source: '多源数据接入', target: '无线电频谱' },
    { source: '雷达信号', target: '数据预处理' },
    { source: '红外图像', target: '数据预处理' },
    { source: '无线电频谱', target: '数据预处理' },
    { source: '数据预处理', target: '特征提取' },
    { source: '特征提取', target: '协同识别算法' },
    { source: '协同识别算法', target: '结果优化' },
    { source: '结果优化', target: '最终输出' },
  ];

  const option = {
    tooltip: {},
    animationDurationUpdate: 1500,
    animationEasingUpdate: 'quinticInOut',
    color: ['#83e0ff', '#29b6f6', '#4fc3f7', '#0288d1', '#01579b'],
    series: [
      {
        type: 'graph',
        layout: 'none',
        symbolSize: 50,
        roam: true,
        label: {
          show: true,
          position: 'bottom',
          formatter: '{b}',
          fontSize: 12,
          color: '#333'
        },
        edgeSymbol: ['circle', 'arrow'],
        edgeSymbolSize: [4, 10],
        edgeLabel: {
          fontSize: 20
        },
        data: nodes.map(node => ({
          ...node,
          itemStyle: {
            shadowBlur: 10,
            shadowColor: 'rgba(0, 0, 0, 0.3)'
          }
        })),
        links: links,
        lineStyle: {
          opacity: 0.9,
          width: 2,
          curveness: 0.1,
          color: '#aaa'
        },
        emphasis: {
          scale: true,
          focus: 'adjacency'
        }
      }
    ]
  };
  workflowChart.setOption(option);
};

// 通用仪表盘配置生成器
const getGaugeOption = (name, value, color) => {
  return {
    tooltip: { formatter: '{a} <br/>{b} : {c}%' },
    series: [
      {
        name: name,
        type: 'gauge',
        detail: {
          formatter: '{value}%',
          fontSize: 16,
          color: '#333',
          offsetCenter: [0, '70%']
        },
        progress: {
          show: true,
          itemStyle: { color: color }
        },
        axisLine: {
          lineStyle: { width: 10 }
        },
        axisTick: { show: false },
        splitLine: { length: 8, lineStyle: { width: 1, color: '#999' } },
        axisLabel: { distance: 15, color: '#999', fontSize: 10 },
        pointer: { width: 4 },
        data: [{ value: value, name: name }]
      }
    ]
  };
};

// 网络波形图配置生成器
const getLineOption = (data) => {
  return {
    tooltip: { trigger: 'axis' },
    grid: { top: 10, bottom: 20, left: 30, right: 10 },
    xAxis: { type: 'category', show: false, boundaryGap: false },
    yAxis: { type: 'value', splitLine: { show: false } },
    series: [{
      data: data,
      type: 'line',
      smooth: true,
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
          { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
        ])
      },
      itemStyle: { color: '#409eff' },
      showSymbol: false
    }]
  }
}

// 初始化资源监控图表
const initResourceCharts = () => {
  if (cpuChartRef.value) {
    cpuChart = echarts.init(cpuChartRef.value);
    cpuChart.setOption(getGaugeOption('CPU', 45, '#67C23A'));
  }
  if (gpuChartRef.value) {
    gpuChart = echarts.init(gpuChartRef.value);
    gpuChart.setOption(getGaugeOption('GPU', 28, '#E6A23C'));
  }
  if (memChartRef.value) {
    memChart = echarts.init(memChartRef.value);
    memChart.setOption(getGaugeOption('Memory', 60, '#F56C6C'));
  }
  if (netChartRef.value) {
    netChart = echarts.init(netChartRef.value);
    // 初始模拟数据
    netChart.setOption(getLineOption([10, 22, 15, 30, 25, 40, 35, 50, 45]));
  }
};

// 模拟动态数据
const startSimulation = () => {
  timer = setInterval(() => {
    // 随机更新仪表盘
    const cpuVal = +(Math.random() * 60 + 20).toFixed(0);
    const gpuVal = +(Math.random() * 50 + 10).toFixed(0);
    const memVal = +(Math.random() * 20 + 50).toFixed(0);

    cpuChart?.setOption({ series: [{ data: [{ value: cpuVal, name: 'CPU' }] }] });
    gpuChart?.setOption({ series: [{ data: [{ value: gpuVal, name: 'GPU' }] }] });
    memChart?.setOption({ series: [{ data: [{ value: memVal, name: 'Memory' }] }] });

    // 更新网络图表
    const netData = netChart?.getOption().series[0].data;
    if (netData) {
      netData.shift();
      netData.push(+(Math.random() * 50 + 10).toFixed(0));
      netChart.setOption({ series: [{ data: netData }] });
    }

  }, 2000);
};

// 窗口大小调整自适应
const handleResize = () => {
  workflowChart?.resize();
  cpuChart?.resize();
  gpuChart?.resize();
  memChart?.resize();
  netChart?.resize();
};

onMounted(() => {
  initWorkflowChart();
  initResourceCharts();
  startSimulation();
  window.addEventListener('resize', handleResize);
});

onBeforeUnmount(() => {
  if (timer) clearInterval(timer);
  window.removeEventListener('resize', handleResize);
  workflowChart?.dispose();
  cpuChart?.dispose();
  gpuChart?.dispose();
  memChart?.dispose();
  netChart?.dispose();
});
</script>

<style scoped lang="scss">
.home-dashboard {
  min-height: calc(100vh - 84px); /* 减去顶部导航栏的大致高度 */
  display: flex;
  flex-direction: column;
  background-color: #f0f2f5;
  padding: 20px;
}

.dashboard-header {
  text-align: center;
  margin-bottom: 20px;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);

  h1 {
    margin: 0;
    font-size: 28px;
    color: #303133;
    font-weight: 600;
    letter-spacing: 2px;
  }
  .subtitle {
    margin: 8px 0 0;
    font-size: 14px;
    color: #909399;
    text-transform: uppercase;
  }
}

.dashboard-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.top-section {
  flex: 2; // 占据 2/3 比例 (相对)
  min-height: 400px;

  .workflow-card {
    height: 100%;
    display: flex;
    flex-direction: column;

    :deep(.el-card__body) {
      flex: 1;
      padding: 0;
      position: relative;
    }
  }

  .chart-container {
    width: 100%;
    height: 100%;
    min-height: 400px;
  }
}

.bottom-section {
  flex: 1; // 占据 1/3 比例 (相对)
  min-height: 200px;

  .resource-card-container {
    height: 100%;
    :deep(.el-card__body) {
      height: calc(100% - 55px); // 减去header高度
    }
  }
}

.resource-row {
  height: 100%;
  display: flex;
  align-items: center;
}

.monitor-item {
  text-align: center;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  .monitor-title {
    font-size: 14px;
    color: #606266;
    margin-bottom: 10px;
    font-weight: bold;
  }

  .monitor-chart {
    width: 100%;
    height: 140px;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;

  .el-icon {
    margin-right: 5px;
    vertical-align: middle;
  }
}
</style>
