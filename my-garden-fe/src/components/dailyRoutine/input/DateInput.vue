<script setup>
import {ref, watch} from "vue";

const props = defineProps({
  inputName: String,
  startDateTime: String,
  endDateTime: String
});

const emit = defineEmits(['changeDate'])
const dateTime = ref('');

watch(() => [props.startDateTime, props.endDateTime], (newValue) => {
  if (props.inputName === '시작') {
    dateTime.value = newValue[0];
  } else if (props.inputName === '끝') {
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

.input-group {
  margin-bottom: 10px;
}

input[type="datetime-local"] {
  width: 100%;
  padding: 10px;
  font-size: 19px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

</style>
