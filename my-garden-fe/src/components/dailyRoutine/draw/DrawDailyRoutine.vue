<script setup>
import {computed, onMounted, ref} from "vue";

const schedule = ref([
  {
    startDateTime: '2023-12-17T00:10:00',
    endDateTime: '2023-12-17T06:30:00',
    routineType: '운동',
    routineDescription: '공백',
    color: '#b23f3f'
  },
  {
    startDateTime: '2023-12-17T07:00:00',
    endDateTime: '2023-12-17T15:00:00',
    routineType: '수면',
    routineDescription: '공백',
    color: '#a0a0a0'
  },
  {
    startDateTime: '2023-12-17T15:00:00',
    endDateTime: '2023-12-17T17:00:00',
    routineType: 'Meal',
    routineDescription: '공백',
    color: '#70db70'
  },
  {
    startDateTime: '2023-12-17T17:00:00',
    endDateTime: '2023-12-17T17:20:00',
    routineType: 'Study',
    routineDescription: '공백',
    color: '#ffdb4d'
  },
  {
    startDateTime: '2023-12-17T17:20:00',
    endDateTime: '2023-12-17T17:30:00',
    routineType: 'Break',
    routineDescription: '공백',
    color: '#4de4ff'
  },
  // ... add other blocks as necessary
]);

const splitSchedule = ref([]);

onMounted(() => {
  splitSchedule.value = processSchedule(schedule);
});

function calculateDuration(block) {
  const start = timeToMinutes(block.displayStartTime);
  const end = timeToMinutes(block.displayEndTime);
  return end - start;
}

function timeToMinutes(time) {
  const [hours, minutes] = time.split(':').map(Number);
  return hours * 60 + minutes;
}

function blockStyle(block, partOfDay) {
  const duration = calculateDuration(block);
  const totalDuration = partOfDay === 'morning' ? morningDuration.value : afternoonDuration.value;
  const heightRatio = (duration / totalDuration) * 100;
  return {
    backgroundColor: block.color,
    height: `${heightRatio}%`,
  };
}

function extractTime(dateTime) {
  const [date, time] = dateTime.split('T');
  return time.slice(0, 5);
}

function processSchedule(schedule) {
  const NOON_STRING = '12:00';
  const noon = timeToMinutes(NOON_STRING);

  return schedule.value.flatMap(block => {
    const startTime = extractTime(block.startDateTime);
    const endTime = extractTime(block.endDateTime);

    const start = timeToMinutes(startTime);
    const end = timeToMinutes(endTime);
    const splitBlocks = [];

    if (start < noon) {
      splitBlocks.push({
        routineType: block.routineType,
        color: block.color,
        displayStartTime: startTime,
        displayEndTime: end > noon ? NOON_STRING : endTime,
        partOfDay: 'morning',
      });
    }

    if (end > noon) {
      splitBlocks.push({
        routineType: block.routineType,
        color: block.color,
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

const morningDuration = computed(() => {
  return morningSchedule.value.reduce((total, block) => total + calculateDuration(block), 0);
});

const afternoonDuration = computed(() => {
  return afternoonSchedule.value.reduce((total, block) => total + calculateDuration(block), 0);
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
  flex: 1;
  display: flex;
  flex-direction: column;
  border: 1px solid #ccc;
  margin: 0 10px;
  width: 300px;
}

.time-block {
  width: 100%;
  margin: 2px 0;
  color: white;
  text-align: center;
  box-sizing: border-box;
}
</style>
