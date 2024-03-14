<script setup>

import {onMounted, watch} from "vue";
import {Chart} from "chart.js/auto";
import {store} from "@/scripts/store.js";
import ChartDataLabels from 'chartjs-plugin-datalabels';

/**
 * 하루 일과에서 사용하는 색상 맵
 */
const colorMap = store.getters.getColors;

/**
 * 현재 보고 있는 날짜
 */
let viewDate = store.getters.getViewDate;

/**
 * 모든 루틴의 총 시간 합
 */
let allTotalMinutesSum = 0;

/**
 * Chart.js의 옵션
 */
const options = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    title: {
      display: true,
      text: `일과 통계 [${viewDate}]`,
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

/**
 * Chart.js의 데이터
 */
const data = {
  labels: [],
  datasets: [{
    data: [],
    backgroundColor: []
  }]
};

/**
 * Chart.js의 설정
 */
const config = {
  type: 'pie',
  plugins: [ChartDataLabels],
  data: data,
  options: options
};

/**
 * Chart.js 객체
 */
let myChart;

/**
 * Chart.js의 DataSet을 업데이트
 */
function updateDataSetFrom(statisticData) {
  // 기존에 존재하는 데이터를 모두 삭제
  myChart.data.labels.length = 0;
  myChart.data.datasets[0].data.length = 0;
  myChart.data.datasets[0].backgroundColor.length = 0;

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

  // Chart의 Title을 업데이트
  myChart.options.plugins.title.text = `일과 통계 [${viewDate}]`;
}

/**
 * 시간 블록 배열에서 통계 데이터를 계산
 *
 * @param timeBlockArray 시간 블록 배열
 * @param statisticData 통계 데이터
 * @returns {number} 총 시간 합
 */
function calculateStatisticDataFromTimeBlockArray(timeBlockArray, statisticData) {
  let tempSumTotalMinutes = 0;

  timeBlockArray.forEach((timeBlock) => {
    if (statisticData[timeBlock.routineType]) {
      statisticData[timeBlock.routineType] += timeBlock.totalMinutes;
    } else {
      statisticData[timeBlock.routineType] = timeBlock.totalMinutes;
    }

    tempSumTotalMinutes += timeBlock.totalMinutes;
  });

  return tempSumTotalMinutes;
}

onMounted(() => {
  myChart = new Chart(
      document.getElementById('chartCanvas'),
      config
  );
});

/**
 * 시간 블록 배열이 업데이트되면, 통계 데이터를 계산하고, Chart.js의 DataSet을 업데이트한다.
 */
watch(() => store.getters.getTimeBlockArray, (timeBlockArray) => {
  const statisticData = {};

  // 타입에 맞춰서 시간을 합산.
  allTotalMinutesSum = calculateStatisticDataFromTimeBlockArray(timeBlockArray, statisticData);

  //statisticData를 pie Chart의 DataSet에 삽입
  updateDataSetFrom(statisticData);

  myChart.update();
});

watch(() => store.getters.getViewDate, (date) => {
  viewDate = date;
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
