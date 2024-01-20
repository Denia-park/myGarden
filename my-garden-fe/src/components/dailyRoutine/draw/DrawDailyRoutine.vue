<script setup>
import {computed, ref, watch} from "vue";
import {getDailyRoutineApi, getTargetDateTimeRange} from "@/components/dailyRoutine/api/api.js";

const props = defineProps({
  updateDate: String,
});

const SHOW_TEXT_PIXELS = 30;
const splitSchedule = ref([]);
const emit = defineEmits(['updateBlock'])

watch(() => props.updateDate, (newDate) => {
      const {targetStartDateTime, targetEndDateTime} = getTargetDateTimeRange(newDate);

      getDailyRoutineApi(targetStartDateTime, targetEndDateTime)
          .then(response => {
            splitSchedule.value = processSchedule(response.allDateTimeDataArray);
          })
    }, {immediate: true}
);

function processSchedule(schedule) {
  const NOON_STRING = '12:00';
  const noon = timeToMinutes(NOON_STRING);

  function createMorningBlock(block, startTime, end, endTime) {
    return {
      id: block.id,
      routineType: block.routineType,
      color: findMatchingColor(block.routineType),
      displayStartTime: startTime,
      displayEndTime: end > noon ? NOON_STRING : endTime,
      partOfDay: 'morning',
      routineDescription: block.routineDescription,
    };
  }

  function createAfternoonBlock(block, start, startTime, endTime) {
    return {
      id: block.id,
      routineType: block.routineType,
      color: findMatchingColor(block.routineType),
      displayStartTime: start < noon ? NOON_STRING : startTime,
      displayEndTime: endTime,
      partOfDay: 'afternoon',
      routineDescription: block.routineDescription,
    };
  }

  return schedule.flatMap(block => {
    const startTime = extractTime(block.startDateTime);
    const endTime = extractTime(block.endDateTime);

    const start = timeToMinutes(startTime);
    const end = timeToMinutes(endTime);
    const splitBlocks = [];

    if (start < noon) {
      splitBlocks.push(createMorningBlock(block, startTime, end, endTime));
    }

    if (end > noon) {
      splitBlocks.push(createAfternoonBlock(block, start, startTime, endTime));
    }

    return splitBlocks;
  });
}

function extractTime(dateTime) {
  const [date, time] = dateTime.split('T');
  return time.slice(0, 5);
}

function timeToMinutes(time) {
  const [hours, minutes] = time.split(':').map(Number);
  return hours * 60 + minutes;
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
  const blockTotalHeight = 720; // 1px per minute, 12 hours * 60 minutes

  return partOfDay === 'afternoon' ? blockTotalHeight : 0; // 12:00 ~ 24:00
}

function buildTooltipMessage(block) {
  return block.routineDescription === "" ? block.routineType : block.routineDescription;
}

function buildTimeText(block) {
  return `${block.displayStartTime} ~ ${block.displayEndTime} :: ${block.routineType}`
}

function isEnoughHeightBlock(block) {
  return calculateDuration(block) >= SHOW_TEXT_PIXELS;
}

function isNotEnoughHeightBlock(block) {
  return !isEnoughHeightBlock(block);
}

const morningSchedule = computed(() => {
  return splitSchedule.value.filter(block => block.partOfDay === 'morning');
});

const afternoonSchedule = computed(() => {
  return splitSchedule.value.filter(block => block.partOfDay === 'afternoon');
});

function updateBlock(block) {
  block.lastUpdated = new Date().toISOString();

  emit('updateBlock', block);
}
</script>

<template>
  <div id="schedule-container">
    <div id="morning-schedule" class="schedule-section">
      <div class="schedule-label">오전</div>
      <div class="schedule-half">
        <div v-for="(block, index) in morningSchedule" :key="`morning-${index}`"
             :style="blockStyle(block, 'morning')"
             class="time-block my-tooltip"
             @click="updateBlock(block)">
          <div v-if="isEnoughHeightBlock(block)">
            {{ buildTimeText(block) }}
          </div>
          <span class="tooltip-text tooltip-left">
            <span v-if="isNotEnoughHeightBlock(block)">
              {{ buildTimeText(block) }}
            </span>
            {{ buildTooltipMessage(block) }}
          </span>
        </div>
      </div>
    </div>

    <div id="afternoon-schedule" class="schedule-section">
      <div class="schedule-label">오후</div>
      <div class="schedule-half">
        <div v-for="(block, index) in afternoonSchedule" :key="`afternoon-${index}`"
             :style="blockStyle(block, 'afternoon')"
             class="time-block my-tooltip"
             @click="updateBlock(block)">
          <div v-if="isEnoughHeightBlock(block)">
            {{ buildTimeText(block) }}
          </div>
          <span class="tooltip-text tooltip-right">
            <span v-if="isNotEnoughHeightBlock(block)">
              {{ buildTimeText(block) }}
            </span>
            {{ buildTooltipMessage(block) }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
#schedule-container {
  display: flex; /* Arrange morning and afternoon sections side by side */
  justify-content: center; /* Center align the morning and afternoon sections */
  flex-wrap: wrap;
}

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

.my-tooltip {
  position: relative;
  display: block;
}

.my-tooltip .tooltip-text {
  visibility: hidden; /* 이벤트가 없으면 툴팁 영역을 숨김 */
  width: 180px; /* 툴팁 영역의 넓이를 설정 */
  background-color: black;
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 5px 0;

  position: absolute; /* 절대 위치를 사용 */
  z-index: 1;
}

.my-tooltip:hover .tooltip-text {
  visibility: visible; /* hover 이벤트 발생시 영역을 보여줌 */
}

.my-tooltip .tooltip-text::after {
  content: " "; /* 정사각형 영역 사용 */
  position: absolute; /* 절대 위치 사용 */
  border-style: solid;
  border-width: 5px; /* 테두리 넓이를 5px 로 설정 */
}

.my-tooltip .tooltip-left {
  top: -5px; /* 영역의 위치를 -5 만큼 위로 이동 */
  right: 105%; /* 왼쪽에 생성해야하므로 영역을 오른쪽에서 105% 이동 */
}

.my-tooltip .tooltip-left::after {
  top: 50%; /* 사각형 영역이 중앙에 오도록 위치 */
  left: 100%; /* 왼쪽에서 100% 위치에 오도록 위치 */
  margin-top: -5px;

  /* 사각형의 테두리에서 왼쪽만 노출 */
  border-color: transparent transparent transparent black;
}

.my-tooltip .tooltip-right {
  top: -5px;
  left: 105%;
}

.my-tooltip .tooltip-right::after {
  top: 50%;
  right: 100%;
  margin-top: -5px;
  border-color: transparent black transparent transparent;
}
</style>
