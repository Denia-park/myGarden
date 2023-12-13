<script setup>
import {computed, onMounted, ref} from "vue";

const schedule = ref([
  {startTime: '00:10', endTime: '06:30', activity: '운동', color: '#b23f3f'},
  {startTime: '07:00', endTime: '15:00', activity: 'Sleep', color: '#a0a0a0'},
  {startTime: '15:00', endTime: '17:00', activity: 'Meal', color: '#70db70'},
  {startTime: '17:00', endTime: '17:20', activity: 'Study', color: '#ffdb4d'},
  {startTime: '17:20', endTime: '17:30', activity: 'Break', color: '#4de4ff'},
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

function processSchedule(schedule) {
  const noon = timeToMinutes('12:00');

  return schedule.value.flatMap(block => {
    const start = timeToMinutes(block.startTime);
    const end = timeToMinutes(block.endTime);
    const splitBlocks = [];

    if (start < noon) {
      splitBlocks.push({
        activity: block.activity,
        color: block.color,
        displayStartTime: block.startTime,
        displayEndTime: end > noon ? '12:00' : block.endTime,
        partOfDay: 'morning',
      });
    }

    if (end > noon) {
      splitBlocks.push({
        activity: block.activity,
        color: block.color,
        displayStartTime: start < noon ? '12:00' : block.startTime,
        displayEndTime: block.endTime,
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
          <div>{{ block.displayStartTime }} ~ {{ block.displayEndTime }} :: {{ block.activity }}</div>
        </div>
      </div>
    </div>

    <div id="afternoon-schedule" class="schedule-section">
      <div class="schedule-label">오후</div>
      <div class="schedule-half">
        <div v-for="(block, index) in afternoonSchedule" :key="`afternoon-${index}`"
             :style="blockStyle(block, 'afternoon')"
             class="time-block">
          <div>{{ block.displayStartTime }} ~ {{ block.displayEndTime }} :: {{ block.activity }}</div>
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
