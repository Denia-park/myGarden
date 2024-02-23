<script setup>
import StatisticTable from "@/components/dailyRoutine/statistics/StatisticTable.vue";
import SelectDateWithCalendar from "@/components/dailyRoutine/statistics/SelectDateWithCalendar.vue";

import {ref, watch} from "vue";
import {getTargetDateTimeRange, getTodayDate} from "@/components/dailyRoutine/api/util.js";
import {getDailyRoutineApi} from "@/components/dailyRoutine/api/api.js";

const inputDate = ref({
  start: getTodayDate(),
  end: getTodayDate()
});
const statisticData = ref({});
const MILLS_TO_HOURS = 3600000;
const isLoaded = ref(false);

function updateSelectDate(date) {
  inputDate.value.start = date.start;
  inputDate.value.end = date.end;
}

function calculateActivities(activities) {
  const results = activities.reduce((acc, curr) => {
    return calculateHourPerType(curr, acc);
  }, {});

  // Calculate percentage of each type for each day and format hours
  calculatePercentagePerType(results);

  return convertObjectToArray(results);
}

function calculateHourPerType(curr, acc) {
  const date = curr.startDateTime.split('T')[0];
  const startTime = new Date(curr.startDateTime).getTime();
  const endTime = new Date(curr.endDateTime).getTime();
  const durationHours = (endTime - startTime) / MILLS_TO_HOURS; // Convert milliseconds to hours

  if (!acc[date]) {
    acc[date] = {totalHours: 0, types: {}};
  }

  if (!acc[date].types[curr.routineType]) {
    acc[date].types[curr.routineType] = 0;
  }

  acc[date].types[curr.routineType] += durationHours;
  acc[date].totalHours += durationHours;

  return acc;
}

function calculatePercentagePerType(results) {
  for (const date in results) {
    results[date].totalHours = parseFloat(results[date].totalHours.toFixed(2)); // Format totalHours to one decimal place

    for (const type in results[date].types) {
      const hoursFormatted = parseFloat(results[date].types[type].toFixed(2));
      const percentage = Math.min((results[date].types[type] / results[date].totalHours) * 100, 100);
      results[date].types[type] = {hours: hoursFormatted, percentage: percentage.toFixed(2) + '%'};
    }
  }
}

function convertObjectToArray(object) {
  const arrayOfObjects = [];

  // 주어진 데이터를 날짜와 내용을 가지는 객체의 배열로 변환
  for (const date in object) {
    if (object.hasOwnProperty(date)) {
      const content = object[date];
      const obj = {date, content};
      arrayOfObjects.push(obj);
    }
  }

  return arrayOfObjects;
}

watch(() => inputDate.value, () => {
  const {targetStartDateTime} = getTargetDateTimeRange(inputDate.value.start);
  const {targetEndDateTime} = getTargetDateTimeRange(inputDate.value.end);

  getDailyRoutineApi(targetStartDateTime, targetEndDateTime)
      .then(data => {
        statisticData.value = calculateActivities(data.allDateTimeDataArray);
        isLoaded.value = true;
      })
}, {deep: true, immediate: true});
</script>

<template>
  <div class="wrapper">
    <div class="header">
      <h1>하루 일과 통계</h1>
      <button class="btn btn-info" @click="() => $router.push('/daily-routine')">일과 등록<br> 돌아가기</button>
    </div>
    <div class="content">
      <div class="upper-content">
        <div class="upper-content-left">
          <SelectDateWithCalendar @update-select-date="updateSelectDate"/>
        </div>
        <div class="upper-content-right">
          <StatisticTable v-if="isLoaded" :add-table-class="['border-primary']"
                          :end-date="inputDate.end" :is-total="true" :routine-data="statisticData"
                          :start-date="inputDate.start" :tr-color="'table-primary'"/>
        </div>
      </div>
      <div class="lower-content">
        <div v-for="eachDay in statisticData" :key="eachDay.date" class="single-content">
          <StatisticTable :add-table-class="['border-secondary']"
                          :end-date="inputDate.end" :is-total="false" :routine-data="eachDay"
                          :start-date="inputDate.start" :tr-color="'table-secondary'"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.wrapper {
  max-width: 1280px;
  margin: 0 auto;
  font-weight: normal;

  display: flex;
  flex-direction: column;
  justify-content: center;
  flex-wrap: wrap;
  padding: 0 2rem;
}

.header {
  position: relative;
}

.header button {
  position: absolute;
  top: 20px;
  right: 10px;
}

h1 {
  text-align: center;
  margin: 20px 0;
}

.content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 10px;
  height: 760px;
}

.upper-content {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 360px;

  margin-bottom: 10px;
}

.upper-content-left {
  width: 90%;
  height: 100%;

  display: flex;
  flex-direction: column;
  align-items: center;
}

.upper-content-right {
  width: 100%;

  height: 100%;
  overflow: auto;
}

.lower-content {
  max-width: 1280px;
  min-width: 1280px;

  display: flex;
  flex-direction: row;

  height: 400px;
  overflow: auto;
}

.lower-content div {
  min-width: 250px;
  margin-right: 3px;
}

</style>
