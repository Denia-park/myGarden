<script setup>
import {ref, watch} from "vue";

const props = defineProps({
  inputName: String,
  startDateTime: String,
  endDateTime: String
});

const emit = defineEmits(['changeDate'])

/**
 * 일과 시간
 */
const dateTime = ref('');

/**
 * 일과 시간 변경 감지
 */
watch(() => [props.startDateTime, props.endDateTime], (newValue) => {
  if (props.inputName === '시작 시간') {
    dateTime.value = newValue[0];
  } else if (props.inputName === '끝난 시간') {
    dateTime.value = newValue[1];
  }
});

</script>

<template>
  <div class="input-group">
    <p>{{ inputName }}</p>
    <input :value="dateTime" type="datetime-local" @input="(e) => emit('changeDate', e.target.value)"/>
  </div>
</template>

<style scoped>

p {
  font-size: 20px;
  margin-bottom: 10px;
}

input {
  width: 100%;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.input-group {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;

  width: 40%;
}

</style>
