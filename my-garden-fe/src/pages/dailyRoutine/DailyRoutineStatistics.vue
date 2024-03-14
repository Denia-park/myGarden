<script setup>
import StatisticTable from "@/components/dailyRoutine/statistics/StatisticTable.vue";
import SelectDateWithCalendar from "@/components/dailyRoutine/statistics/SelectDateWithCalendar.vue";

import {ref, watch} from "vue";
import {getTargetDateTimeRange, getTodayDate} from "@/components/dailyRoutine/api/util.js";
import {getDailyRoutineApi} from "@/components/dailyRoutine/api/api.js";

/**
 * 통계를 보여줄 기준 시작 날짜와 끝 날짜
 */
const inputDate = ref({
  start: getTodayDate(),
  end: getTodayDate()
});

/**
 * 통계 데이터
 */
const statisticData = ref({});

/**
 * 통계 데이터 로딩 여부
 */
const isLoaded = ref(false);

/**
 * 밀리세컨드를 시간으로 변환하기 위한 상수
 */
const MILLS_TO_HOURS = 3600000;

/**
 * 조회 날짜 변경
 *
 * @param date 변경할 날짜
 */
function updateSelectDate(date) {
  inputDate.value.start = date.start;
  inputDate.value.end = date.end;
}

/**
 * 활동 데이터를 통계 데이터로 변환
 *
 * @param activities 활동 데이터
 * @returns {[]} 통계 데이터
 */
function calculateActivities(activities) {
  const results = activities.reduce((acc, curr) => {
    return calculateHourPerType(acc, curr);
  }, {});

  // Calculate percentage of each type for each day and format hours
  calculatePercentagePerType(results);

  return convertObjectToArray(results);
}

/**
 * 활동 타입별 시간 계산
 *
 * @param acc 누적된 결과
 * @param curr 현재 활동
 * @returns {*} 계산된 결과
 */
function calculateHourPerType(acc, curr) {
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

/**
 * 활동 타입별 비율 계산
 *
 * @param results 통계 데이터
 */
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

/**
 * 객체를 배열로 변환
 *
 * @param object 객체
 * @returns {[]} 배열
 */
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

/**
 * 날짜가 변경될 때마다 통계 데이터를 새로 가져온다.
 */
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
