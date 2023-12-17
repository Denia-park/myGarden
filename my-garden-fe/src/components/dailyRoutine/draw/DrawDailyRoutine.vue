<script setup>
import {computed, onMounted, ref} from "vue";
import axios from "axios";

const splitSchedule = ref([]);

onMounted(() => {
  fetchTodayDailyRoutine();
});

function getTodayDateTimeRange() {
  const currentDate = new Date();

  const year = currentDate.getFullYear();
  const month = String(currentDate.getMonth() + 1).padStart(2, '0');
  const day = String(currentDate.getDate()).padStart(2, '0');

  const todayStartDateTime = `${year}-${month}-${day}T00:00:00`;
  const todayEndDateTime = `${year}-${month}-${day}T23:59:59`;

  return {todayStartDateTime, todayEndDateTime};
}

function fetchTodayDailyRoutine() {
  const {todayStartDateTime, todayEndDateTime} = getTodayDateTimeRange();

  axios.get(`/api/daily-routine?startDateTime=${todayStartDateTime}&endDateTime=${todayEndDateTime}`)
      .then(({data}) => {
        splitSchedule.value = processSchedule(data.data);
      })
      .catch(error => {
        console.log(error);
      });
}

function calculateDuration(block) {
  const start = timeToMinutes(block.displayStartTime);
  const end = timeToMinutes(block.displayEndTime);
  return end - start;
}

function timeToMinutes(time) {
  const [hours, minutes] = time.split(':').map(Number);
  return hours * 60 + minutes;
}

function getOffset(partOfDay) {
  const blockTotalHeight = 720; // 1px per minute, 12 hours * 60 minutes

  return partOfDay === 'afternoon' ? blockTotalHeight : 0; // 12:00 ~ 24:00
}

function blockStyle(block, partOfDay) {
  const duration = calculateDuration(block);
  const startTop = timeToMinutes(block.displayStartTime);
  const offset = getOffset(partOfDay);

  return {
    position: `relative`,
    backgroundColor: block.color,
    top: `${startTop - offset}px`,
    height: `${duration}px`
  };
}

function extractTime(dateTime) {
  const [date, time] = dateTime.split('T');
  return time.slice(0, 5);
}

function findMatchingColor(type) {
  const colorMap = {
    '운동': '#b23f3f',
    '수면': '#a0a0a0',
    '식사': '#70db70',
    '공부': '#ffdb4d',
    '휴식': '#4de4ff',
    '게임': '#e76c0c',
    '기타': '#cd4dff',
  };

  return colorMap[type];
}

function processSchedule(schedule) {
  const NOON_STRING = '12:00';
  const noon = timeToMinutes(NOON_STRING);

  return schedule.flatMap(block => {
    const startTime = extractTime(block.startDateTime);
    const endTime = extractTime(block.endDateTime);

    const start = timeToMinutes(startTime);
    const end = timeToMinutes(endTime);
    const splitBlocks = [];

    if (start < noon) {
      splitBlocks.push({
        routineType: block.routineType,
        color: findMatchingColor(block.routineType),
        displayStartTime: startTime,
        displayEndTime: end > noon ? NOON_STRING : endTime,
        partOfDay: 'morning',
      });
    }

    if (end > noon) {
      splitBlocks.push({
        routineType: block.routineType,
        color: findMatchingColor(block.routineType),
        displayStartTime: start < noon ? NOON_STRING : startTime,
        displayEndTime: endTime,
        partOfDay: 'afternoon',
      });
    }

    return splitBlocks;
  });
}

const morningSchedule = computed(() => {
  return splitSchedule.value.filter(block => block.partOfDay === 'morning');
});

const afternoonSchedule = computed(() => {
  return splitSchedule.value.filter(block => block.partOfDay === 'afternoon');
});
</script>

<template>
  <div id="schedule-container">
    <div id="morning-schedule" class="schedule-section">
      <div class="schedule-label">오전</div>
      <div class="schedule-half">
        <div v-for="(block, index) in morningSchedule" :key="`morning-${index}`"
             :style="blockStyle(block, 'morning')"
             class="time-block">
          <div>{{ block.displayStartTime }} ~ {{ block.displayEndTime }} :: {{ block.routineType }}</div>
        </div>
      </div>
    </div>

    <div id="afternoon-schedule" class="schedule-section">
      <div class="schedule-label">오후</div>
      <div class="schedule-half">
        <div v-for="(block, index) in afternoonSchedule" :key="`afternoon-${index}`"
             :style="blockStyle(block, 'afternoon')"
             class="time-block">
          <div>{{ block.displayStartTime }} ~ {{ block.displayEndTime }} :: {{ block.routineType }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
#schedule-container {
  display: flex; /* Arrange morning and afternoon sections side by side */
}

.schedule-section {
  display: flex;
  flex-direction: column; /* Stack label and schedule vertically */
  align-items: center; /* Center align the label and schedule blocks */
  height: 900px;
}

.schedule-label {
  text-align: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 10px; /* Space between label and schedule blocks */
}

.schedule-half {
  display: flex;
  flex-direction: column;
  border: 1px solid #ccc;
  margin: 0 10px;
  width: 300px;
  height: 720px;
}

.time-block {
  width: 100%;
  margin: 2px 0;
  color: white;
  text-align: center;
  box-sizing: border-box;
}
</style>
