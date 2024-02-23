<script setup>
import {ref} from "vue";
import {convertDateFormat, getTodayDate} from "@/components/dailyRoutine/api/util.js";

const emit = defineEmits(['updateSelectDate']);

const inputDate = ref({
  start: getTodayDate(),
  end: getTodayDate()
});
const inputCalendarDate = ref({
  start: new Date(inputDate.value.start),
  end: new Date(inputDate.value.end)
});

function updateDate(date) {
  inputDate.value.start = convertDateFormat(date.start);
  inputDate.value.end = convertDateFormat(date.end);
}
</script>

<template>
  <VDatePicker v-model.range="inputCalendarDate" mode="date" style="width: 70%"
               @update:modelValue="updateDate"/>
  <div class="date-select">
    <input v-model="inputDate.start" class="form-control" disabled style="width: 35%" type="text"/>
    <span>~</span>
    <input v-model="inputDate.end" class="form-control" disabled style="width: 35%" type="text"/>

    <button class="btn btn-success" @click="() => emit('updateSelectDate', inputDate)">조회</button>
  </div>
</template>

<style scoped>

h1 {
  text-align: center;
  margin: 20px 0;
}

.date-select {
  margin-top: 10px;

  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  width: 70%;
}

.date-select input {
  text-align: center;
}

</style>
