<script setup>

import {onMounted, watch} from "vue";
import {Chart} from "chart.js/auto";
import {store} from "@/scripts/store.js";
import ChartDataLabels from 'chartjs-plugin-datalabels';

const colorMap = store.getters.getColors;
let allTotalMinutesSum = 0;

const options = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    title: {
      display: true,
      text: '일과 통계',
      font: {
        size: 25
      }
    },
    datalabels: {
      display: 'auto',
      color: 'black',
      font: {
        size: 20,
        weight: 'bold',
      },
      formatter: (value, context) => {
        const percent = Math.round(value / allTotalMinutesSum * 100);

        return `${percent}%`;
      }
    },
    legend: {
      display: true,
      position: 'right',
      labels: {
        font: {
          size: 20,
          weight: 'bold',
        }
      }
    },
    tooltip: {
      enabled: true,
      callbacks: {
        label: (context) => {
          const label = context.label;
          const value = context.formattedValue;

          const hour = Math.floor(value / 60);
          const minute = value % 60;

          return `${label} : ${hour}시간 ${minute}분`;
        }
      },
      titleFont: {
        size: 20
      },
      bodyFont: {
        size: 20
      },
    }
  }
};

const data = {
  labels: [],
  datasets: [{
    data: [],
    backgroundColor: []
  }]
};

const config = {
  type: 'pie',
  plugins: [ChartDataLabels],
  data: data,
  options: options
};

let myChart;

onMounted(() => {
  myChart = new Chart(
      document.getElementById('chartCanvas'),
      config
  );
});

function updateDataSet(statisticData) {
  Object.entries(statisticData)
      .map(([key, value]) => {
        return {
          routineType: key,
          routineColor: colorMap[key],
          routineTime: value,
        };
      })
      .sort((a, b) => {
        return b.routineTime - a.routineTime;
      })
      .forEach((timeBlock) => {
        myChart.data.labels.push(timeBlock.routineType);
        myChart.data.datasets[0].data.push(timeBlock.routineTime);
        myChart.data.datasets[0].backgroundColor.push(timeBlock.routineColor);
      });
}

function calculateStatisticDataFromTimeBlockArray(newVal, statisticData) {
  let tempSumTotalMinutes = 0;

  newVal.forEach((timeBlock) => {
    if (statisticData[timeBlock.routineType]) {
      statisticData[timeBlock.routineType] += timeBlock.totalMinutes;
    } else {
      statisticData[timeBlock.routineType] = timeBlock.totalMinutes;
    }

    tempSumTotalMinutes += timeBlock.totalMinutes;
  });

  return tempSumTotalMinutes;
}

watch(() => store.getters.getTimeBlockArray, (timeBlockArray) => {
  const statisticData = {};

  // 타입에 맞춰서 시간을 합산.
  allTotalMinutesSum = calculateStatisticDataFromTimeBlockArray(timeBlockArray, statisticData);

  //statisticData를 배열로 변환.
  updateDataSet(statisticData);

  myChart.update();
});

</script>

<template>
  <div class="chart-wrapper">
    <canvas id="chartCanvas"></canvas>
  </div>
</template>

<style scoped>
.chart-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;

  width: 557px;
  height: 370px;

  padding: 0 10px;
}

canvas {
  border: 1px solid black;
  width: 330px;
  height: 330px;
}

</style>
