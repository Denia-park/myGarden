<script setup>

import {computed} from "vue";

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
  hover: {
    type: Boolean,
    required: true,
  }
});

//부모에서 hover 이벤트 발생시 영역을 보여줌
const tooltipStyle = computed(() => {
  return {
    //hover 이벤트가 없으면 툴팁 영역을 숨김
    visibility: props.hover ? 'visible' : 'hidden',
  }
});

</script>
<template>
  <div class="my-tooltip">
    <span :class="['tooltip-text', partOfDay === '오전' ? 'tooltip-left' : 'tooltip-right']" :style="tooltipStyle">
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
