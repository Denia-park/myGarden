<script setup>
import {onMounted, ref} from "vue";

const props = defineProps({
  partOfDay: {
    type: String,
    required: true,
  },
  isEnoughHeightBlock: {
    type: Boolean,
    required: true,
  },
  timeText: {
    type: String,
    required: true,
  },
  tooltipText: {
    type: String,
    required: true,
  },
  timeBlockId: {
    type: Number,
    required: true,
  },
  hoverTimeBlockId: {
    type: Number,
    required: true,
  },
});

/**
 * tooltip의 ref
 */
const tooltipRef = ref(null);
const tooltipTextRef = ref(null);
const tooltipHeightOffset = ref(0);

/**
 * tooltip의 class를 업데이트한다.
 */
function updateTooltipClass() {
  return {
    'tooltip-text': true,
    'tooltip-left': props.partOfDay === '오전',
    'tooltip-right': props.partOfDay === '오후',
  };
}

/**
 * tooltip의 visibility를 업데이트한다.
 */
function updateVisible() {
  return {
    visibility: isTooltipVisible() ? 'visible' : 'hidden',
  };
}

/**
 * tooltip이 보여지는지 여부를 반환한다.
 */
function isTooltipVisible() {
  return props.timeBlockId === props.hoverTimeBlockId;
}

/**
 * tooltip의 위치를 계산한다.
 */
function calculateTooltipOffset() {
  if (!tooltipRef.value) return;

  const parentRect = tooltipRef.value.parentElement.getBoundingClientRect();
  const tooltipTextRect = tooltipTextRef.value.getBoundingClientRect();

  // tooltip 높이를 보정한다. (timeBlock이랑 높이가 맞지 않으면 보기가 좋지 않으므로)
  // (tooltip 높이의 절반) + (부모 영역과 tooltip 영역의 y 차이를 2로 나눈 값의 절대값)
  let tempTooltipHeightOffset = (tooltipTextRect.height / 2) + Math.abs((parentRect.y - tooltipTextRect.y) / 2);

  // tooltip 보정 높이가 너무 많아서, 화면의 상단을 벗어나는 경우 보정 높이를 줄인다.
  if (tempTooltipHeightOffset > parentRect.y) {
    tempTooltipHeightOffset -= 60;
  }

  //top을 음수로 지정해야 위로 올라가기 때문에, 음수로 변환한다.
  tooltipHeightOffset.value = -1 * tempTooltipHeightOffset;
}

onMounted(() => {
  calculateTooltipOffset();
});

</script>
<template>
  <div ref="tooltipRef" class="my-tooltip">
    <span ref="tooltipTextRef"
          :class="updateTooltipClass()"
          :style="updateVisible()">
      <span v-if="!isEnoughHeightBlock">
        {{ timeText }}
      </span>
      {{ tooltipText }}
    </span>
  </div>
</template>

<style scoped>

.my-tooltip {
  position: relative;
  display: block;
}

.my-tooltip .tooltip-text {
  width: 180px; /* 툴팁 영역의 넓이를 설정 */
  background-color: black;
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 5px 0;

  position: absolute; /* 절대 위치를 사용 */
  z-index: 1;
}

.my-tooltip .tooltip-text::after {
  content: " "; /* 정사각형 영역 사용 */
  position: absolute; /* 절대 위치 사용 */
  border-style: solid;
  border-width: 5px; /* 테두리 넓이를 5px 로 설정 */
}

.my-tooltip .tooltip-left {
  top: v-bind(tooltipHeightOffset+ 'px'); /* tooltip의 위치를 tooltipHeightOffset 만큼 보정 */
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
  top: v-bind(tooltipHeightOffset+ 'px'); /* tooltip의 위치를 tooltipHeightOffset 만큼 보정 */
  left: 105%;
}

.my-tooltip .tooltip-right::after {
  top: 50%;
  right: 100%;
  margin-top: -5px;
  border-color: transparent black transparent transparent;
}
</style>
