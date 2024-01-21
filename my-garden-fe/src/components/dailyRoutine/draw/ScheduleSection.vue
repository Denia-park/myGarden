<script setup>
import {store} from "@/scripts/store.js";
import {timeToMinutes} from "@/components/dailyRoutine/api/util.js";

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

function buildTooltipMessage(block) {
  return block.routineDescription === "" ? block.routineType : block.routineDescription;
}

function buildTimeText(block) {
  return `${block.displayStartTime} ~ ${block.displayEndTime} :: ${block.routineType}`
}

function isEnoughHeightBlock(block) {
  const SHOW_TEXT_PIXELS = 30;

  return calculateDuration(block) >= SHOW_TEXT_PIXELS;
}

function isNotEnoughHeightBlock(block) {
  return !isEnoughHeightBlock(block);
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
           :style="blockStyle(block, partOfDay)"
           class="time-block my-tooltip"
           @click="updateBlock(block)">
        <div v-if="isEnoughHeightBlock(block)">
          {{ buildTimeText(block) }}
        </div>
        <span :class="[partOfDay === '오전' ? 'tooltip-left' : 'tooltip-right', 'tooltip-text']">
            <span v-if="isNotEnoughHeightBlock(block)">
              {{ buildTimeText(block) }}
            </span>
            {{ buildTooltipMessage(block) }}
          </span>
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
