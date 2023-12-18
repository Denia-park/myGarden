<script setup>
import ContentTitle from "@/components/default/ContentTitle.vue";
import DateInput from "@/components/dailyRoutine/input/DateInput.vue";
import ContentInput from "@/components/dailyRoutine/input/ContentInput.vue";
import {ref} from "vue";

const startDate = ref('');
const endDate = ref('');

function addLog(content) {

  console.log("startDate : " + startDate.value)
  console.log("endDate : " + endDate.value)
  console.log("content : " + content.value)

  // if (todo.value.trim() && startDate.value && endDate.value) {
  //   logs.value.push({
  //     id: Date.now(),
  //     text: todo.value,
  //     start: startDate.value,
  //     end: endDate.value
  //   });
  //   todo.value = '';
  //   saveLogs();
  // }
}
</script>

<template>
  <div class="data-container">
    <ContentTitle :input-name="'한 일 등록'"/>
    <DateInput :input-name="'시작'" @change-date="date => startDate = date"/>
    <DateInput :input-name="'끝'" @change-date="date => endDate = date"/>
    <ContentInput :input-name="'내용'" @submit="addLog" @change-content="typingContent => content = typingContent"/>

    <button class="btn btn-secondary" type="button" @click="postDailyRoutine">등록</button>
    <p>(※ Ctrl + Enter를 입력하셔도 등록이 됩니다.)</p>
  </div>
</template>

<style scoped>
.data-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 800px; /* Fulls viewport height */
  text-align: center;
  margin: 0 auto;
  padding: 20px;
  width: 100%;
}

</style>
