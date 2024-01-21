<script setup>
import {store} from "@/scripts/store.js";
import {timeToMinutes} from "@/components/dailyRoutine/api/util.js";
import RoutineTooltip from "@/components/dailyRoutine/draw/RoutineTooltip.vue";
import {ref} from "vue";

const props = defineProps({
  partOfDay: {
    type: String,
    required: true,
  },
  scheduleArray: {
    type: Array,
    required: true,
  },
});

const tooltipHover = ref(false);

function blockStyle(block, partOfDay) {
  const duration = calculateDuration(block);
  const startTop = timeToMinutes(block.displayStartTime);
  const offset = getOffset(partOfDay);

  return {
    position: `absolute`,
    backgroundColor: block.color,
    top: `${startTop - 1 - offset}px`, // 1px for border
    height: `${duration}px`,
    border: `1px solid black`,
  };
}

function calculateDuration(block) {
  const start = timeToMinutes(block.displayStartTime);
  const end = timeToMinutes(block.displayEndTime);
  return end - start;
}

function getOffset(partOfDay) {
  const afternoonStrings = ['오후', 'PM', 'pm', 'afternoon'];

  // 720 === 1px per minute, 12 hours * 60 minutes
  return afternoonStrings.includes(partOfDay) ? 720 : 0; // 12:00 ~ 24:00
}

function buildTimeText(block) {
  return `${block.displayStartTime} ~ ${block.displayEndTime} :: ${block.routineType}`
}

function isEnoughHeightBlock(block) {
  const SHOW_TEXT_PIXELS = 30;

  return calculateDuration(block) >= SHOW_TEXT_PIXELS;
}

function updateBlock(block) {
  block.lastUpdated = new Date().toISOString();

  store.commit('setEditBlock', block);
}

</script>

<template>
  <div id="morning-schedule" class="schedule-section">
    <div class="schedule-label">{{ partOfDay }}</div>
    <div class="schedule-half">
      <div v-for="(block, index) in scheduleArray" :key="`${index}`"
           :style="blockStyle(block, partOfDay)" class="time-block"
           @click="updateBlock(block)"
           @mouseleave="tooltipHover = false" @mouseover="tooltipHover = true">

        <div v-if="isEnoughHeightBlock(block)">
          {{ buildTimeText(block) }}
        </div>
        <RoutineTooltip :hover="tooltipHover"
                        :is-enough-height-block="isEnoughHeightBlock(block)"
                        :part-of-day="partOfDay"
                        :time-text="buildTimeText(block)"
                        :tooltip-text="block.routineDescription === '' ? block.routineType : block.routineDescription"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>

.schedule-section {
  display: flex;
  flex-direction: column; /* Stack label and schedule vertically */
  align-items: center; /* Center align the label and schedule blocks */
  height: 750px;
}

.schedule-label {
  text-align: center;
  font-size: 1.2rem;
  font-weight: bold;
}

.schedule-half {
  position: relative;
  display: flex;
  flex-direction: column;
  border: 1px solid #ccc;
  margin: 0 10px;
  width: 250px;
  height: 720px;
}

/*time-block 내부 font 관련 설정*/
.time-block {
  width: 100%;
  color: black;
  font-size: 1.2rem;
  text-align: center;
}
</style>
