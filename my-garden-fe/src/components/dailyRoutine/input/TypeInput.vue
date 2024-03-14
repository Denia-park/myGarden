<script setup>
import {ref, watch} from "vue";

const props = defineProps({
  inputName: String,
  routineType: String
});

const emit = defineEmits(['changeType'])

/**
 * 일과 타입
 */
const selectValue = ref('');

/**
 * 루틴 타입을 select value로 변경
 *
 * @param inputString 루틴 타입
 * @returns {*|string} select value
 */
function convertRoutineTypeToSelectValue(inputString) {
  switch (inputString) {
    case '공부':
      return 'STUDY';
    case '휴식':
      return 'REST';
    case '기타':
      return 'ETC';
    case '운동':
      return 'EXERCISE';
    case '수면':
      return 'SLEEP';
    case '식사':
      return 'MEAL';
    case '게임':
      return 'GAME';
    default: // 영어인 경우
      return inputString;
  }
}

/**
 * 일과 타입 변경 감지
 */
watch(() => props.routineType, (newValue) => {
      const type = convertRoutineTypeToSelectValue(newValue);

      selectValue.value = type;
      emit('changeType', type)
    }, {immediate: true}
);

</script>

<template>
  <div class="input-group">
    <p>{{ inputName }}</p>
    <select id="activity" v-model="selectValue" @change="(e) => emit('changeType', e.target.value)">
      <option value="STUDY">공부</option>
      <option value="REST">휴식</option>
      <option value="ETC">기타</option>
      <option value="EXERCISE">운동</option>
      <option value="SLEEP">수면</option>
      <option value="MEAL">식사</option>
      <option value="GAME">게임</option>
    </select>
  </div>
</template>

<style scoped>
p {
  font-size: 20px;
  margin-bottom: 10px;
}

select {
  height: 47px;

  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.input-group {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}
</style>
