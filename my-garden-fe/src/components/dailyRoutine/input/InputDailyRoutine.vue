<script setup>
import ContentTitle from "@/components/default/ContentTitle.vue";
import DateInput from "@/components/dailyRoutine/input/DateInput.vue";
import ContentInput from "@/components/dailyRoutine/input/ContentInput.vue";
import TypeInput from "@/components/dailyRoutine/input/TypeInput.vue";
import {ref} from "vue";
import axios from "axios";

const startDate = ref('');
const endDate = ref('');
const content = ref('');
const routineType = ref('STUDY');

function logData() {
  console.log("startDate : " + startDate.value)
  console.log("endDate : " + endDate.value)
  console.log("content : " + content.value)
  console.log("routineType : " + routineType.value)
}

function postDailyRoutine() {
  // logData();

  axios.post('/api/daily-routine', {
        startDateTime: startDate.value,
        endDateTime: endDate.value,
        routineType: routineType.value,
        routineDescription: content.value
      }
  ).then(() => {
        alert("등록되었습니다.");
        location.reload();
      }
  ).catch(error => {
        alert("등록에 실패하였습니다.");
        console.log(error);
      }
  );
}

function addLog(inputContent) {
  content.value = inputContent.value;

  validate();
  postDailyRoutine();
}

function validate() {
  //시작 날짜가 끝 날짜보다 늦을 수 없다.
  validateStartDateIsBeforeEndDate();

  //두 날짜는 1일이상 차이가 날 수 없다.
  validateDateDifferenceIsSmaller1();

  //content는 255자를 넘을 수 없다.
  validateContentLength();
}

function validateStartDateIsBeforeEndDate() {
  if (startDate.value > endDate.value) {
    alert("시작 날짜가 끝 날짜보다 늦을 수 없습니다.")
  }
}

function validateDateDifferenceIsSmaller1() {
  // Calculate the time difference in milliseconds
  const timeDifference = new Date(endDate.value) - new Date(startDate.value);

  // Calculate the number of days difference
  const daysDifference = timeDifference / (1000 * 3600 * 24);

  // Check if the difference is more than 2 days
  if (daysDifference >= 1) {
    alert("1일 이상 차이 날 수 없습니다.");
  }
}

function validateContentLength() {
  if (content.value.length > 255) {
    alert("content는 255자를 넘을 수 없습니다.")
  }
}

</script>

<template>
  <div class="data-container">
    <ContentTitle :input-name="'한 일 등록'"/>
    <DateInput :input-name="'시작'" @change-date="date => startDate = date"/>
    <DateInput :input-name="'끝'" @change-date="date => endDate = date"/>
    <TypeInput :input-name="'타입'" @change-type="type => routineType = type"/>
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
