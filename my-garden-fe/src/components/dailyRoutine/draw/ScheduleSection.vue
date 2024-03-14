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

/**
 * hover 중인 시간 블록 id
 */
const hoverTimeBlockId = ref(0);

/**
 * 오전, 오후에 해당 하는 문자열 모음
 */
const morningStrings = ['오전', 'AM', 'am', 'morning'];
const afternoonStrings = ['오후', 'PM', 'pm', 'afternoon'];

/**
 * 시간 블록 스타일
 *
 * @param block 시간 블록
 * @param partOfDay 오전/오후
 * @returns {{position: string, backgroundColor: *, top: string, height: string, border: string}}
 */
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

/**
 * 시간 블록의 높이 계산 (분 단위, 1분당 1px)
 *
 * @param block 시간 블록
 * @returns {number} 시간 블록의 높이
 */
function calculateDuration(block) {
  const start = timeToMinutes(block.displayStartTime);
  const end = timeToMinutes(block.displayEndTime);
  return end - start;
}

/**
 * 시간 블록의 offset 계산
 *
 * @param partOfDay 오전/오후
 * @returns {number} offset
 */
function getOffset(partOfDay) {
  // 720 === 1px per minute, 12 hours * 60 minutes
  return afternoonStrings.includes(partOfDay) ? 720 : 0; // 12:00 ~ 24:00
}

/**
 * 시간 블록 텍스트 생성
 *
 * @param block 시간 블록
 * @returns {string} 시간 블록 텍스트
 */
function buildTimeText(block) {
  return `${block.displayStartTime} ~ ${block.displayEndTime} :: ${block.routineType}`
}

/**
 * 시간 블록이 충분한 높이를 가지고 있는지 확인<p>
 * (충분한 높이를 가지고 있지 않으면 툴팁을 표시하지 않는다.)
 *
 * @param block 시간 블록
 * @returns {boolean} 충분한 높이를 가지고 있는지 여부
 */
function isEnoughHeightBlock(block) {
  const SHOW_TEXT_PIXELS = 30;

  return calculateDuration(block) >= SHOW_TEXT_PIXELS;
}

/**
 * 시간 블록 업데이트
 *
 * @param block 시간 블록
 */
function updateBlock(block) {
  block.lastUpdated = new Date().toISOString();

  store.commit('setEditBlock', block);
}

/**
 * 영어로 변환
 *
 * @param partOfDay 오전/오후
 * @returns {string} 영어로 변환된 오전/오후
 */
function convertEng(partOfDay) {
  return morningStrings.includes(partOfDay) ? 'morning' : 'afternoon';
}

</script>

<template>
  <div id="morning-schedule" class="schedule-section">
    <div class="schedule-label">{{ partOfDay }}</div>
    <div class="schedule-half">
      <div v-for="block in scheduleArray" :key="`${convertEng(partOfDay)}-${block.id}`"
           :style="blockStyle(block, partOfDay)" class="time-block"
           @click="updateBlock(block)"
           @mouseleave="hoverTimeBlockId = 0" @mouseover="hoverTimeBlockId = block.id">

        <div v-if="isEnoughHeightBlock(block)">
          {{ buildTimeText(block) }}
        </div>
        <RoutineTooltip :hover-time-block-id="hoverTimeBlockId"
                        :is-enough-height-block="isEnoughHeightBlock(block)"
                        :part-of-day="partOfDay"
                        :time-block-id="block.id"
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
