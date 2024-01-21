<script setup>
import {ref, watch} from "vue";
import {getDailyRoutineApi} from "@/components/dailyRoutine/api/api.js";
import {store} from "@/scripts/store.js";
import ScheduleSection from "@/components/dailyRoutine/draw/ScheduleSection.vue";
import {extractTime, getTargetDateTimeRange, timeToMinutes} from "@/components/dailyRoutine/api/util.js";

const NOON_STRING = '12:00';
const noonMin = timeToMinutes(NOON_STRING);

const morningSchedule = ref([]);
const afternoonSchedule = ref([]);

function convertRoutineToTimeBlockArray(schedule) {
  const tempTimeBlockArray = [];

  schedule.forEach(block => {
    const startTime = extractTime(block.startDateTime);
    const endTime = extractTime(block.endDateTime);

    if (timeToMinutes(startTime) < noonMin) {
      const timeBlock = createTimeBlock(block, startTime, endTime, 'morning');

      morningSchedule.value.push(timeBlock);
      tempTimeBlockArray.push(timeBlock);
    }

    if (noonMin < timeToMinutes(endTime)) {
      const timeBlock = createTimeBlock(block, startTime, endTime, 'afternoon');

      afternoonSchedule.value.push(timeBlock);
      tempTimeBlockArray.push(timeBlock);
    }
  });

  return tempTimeBlockArray;
}

function createTimeBlock(block, startTime, endTime, partOfDay) {
  let startTimeMin = timeToMinutes(startTime);
  let endTimeMin = timeToMinutes(endTime);

  const scheduleDefaultData = {
    id: block.id,
    routineType: block.routineType,
    color: (store.state.colorMap)[block.routineType],
    routineDescription: block.routineDescription,
  };

  if (partOfDay === 'morning') {
    scheduleDefaultData.displayStartTime = startTime;
    scheduleDefaultData.displayEndTime = noonMin < endTimeMin ? NOON_STRING : endTime; // 오전에 시작해서 오후에 끝나는 경우를 고려함
    scheduleDefaultData.totalMinutes = (noonMin < endTimeMin ? noonMin : endTimeMin) - startTimeMin;
  } else if (partOfDay === 'afternoon') {
    scheduleDefaultData.displayStartTime = startTimeMin < noonMin ? NOON_STRING : startTime; // 오전에 시작해서 오후에 끝나는 경우를 고려함
    scheduleDefaultData.displayEndTime = endTime;
    scheduleDefaultData.totalMinutes = endTimeMin - (startTimeMin < noonMin ? noonMin : startTimeMin);
  }

  return scheduleDefaultData;
}

watch(() => store.getters.getViewDate, (newDate) => {
      const {targetStartDateTime, targetEndDateTime} = getTargetDateTimeRange(newDate);

      getDailyRoutineApi(targetStartDateTime, targetEndDateTime)
          .then(response => {
            store.commit('setTimeBlockArray', convertRoutineToTimeBlockArray(response.allDateTimeDataArray));
          })
    }, {immediate: true}
);
</script>

<template>
  <div id="schedule-container">
    <ScheduleSection :part-of-day="'오전'" :schedule-array="morningSchedule"/>
    <ScheduleSection :part-of-day="'오후'" :schedule-array="afternoonSchedule"/>
  </div>
</template>

<style scoped>
#schedule-container {
  display: flex; /* Arrange morning and afternoon sections side by side */
  justify-content: center; /* Center align the morning and afternoon sections */
  flex-wrap: wrap;
}
</style>
