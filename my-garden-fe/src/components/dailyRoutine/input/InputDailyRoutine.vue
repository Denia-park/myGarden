<script setup>
import ContentTitle from "@/components/default/ContentTitle.vue";
import DateInput from "@/components/dailyRoutine/input/DateInput.vue";
import ContentInput from "@/components/dailyRoutine/input/ContentInput.vue";
import TypeInput from "@/components/dailyRoutine/input/TypeInput.vue";
import {onMounted, ref, watch} from "vue";
import {getTodayDate, getTodayDateTimeRange, postDailyRoutine} from "@/components/dailyRoutine/api/apiUtils.js";

const startDate = ref('');
const endDate = ref('');
const content = ref('');
const routineType = ref('STUDY');
const isUpdateMode = ref(false);

const props = defineProps({
  updateBlock: Object
});

function updateLastStartDateTime() {
  let todayLastStartDateTime = localStorage.getItem("todayLastStartDateTime");
  const todayDate = getTodayDateTimeRange().todayStartDateTime.split("T")[0];

  //localStorage에 데이터가 없거나, 데이터 확인 했는데 오늘 날짜가 아니면 초기화
  if (todayLastStartDateTime === null || todayLastStartDateTime.split("T")[0] !== todayDate) {
    todayLastStartDateTime = "" + todayDate + "T00:00:00";
  }

  startDate.value = todayLastStartDateTime;
}

onMounted(() => {
  updateLastStartDateTime();
});

function postRoutine() {
  // logData();

  validate();
  postDailyRoutine(startDate.value, endDate.value, routineType.value, content.value);

  localStorage.setItem("todayLastStartDateTime", endDate.value);
}

function logData() {
  console.log("startDate : " + startDate.value)
  console.log("endDate : " + endDate.value)
  console.log("content : " + content.value)
  console.log("routineType : " + routineType.value)
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

function updateRoutine() {
  // 업데이트 로직
  alert("아직 준비중입니다.");
  // alert("수정되었습니다.");
  //location.reload();
}

function cancelUpdate() {
  isUpdateMode.value = false;
  updateLastStartDateTime();
  endDate.value = '';
  routineType.value = 'STUDY'
  content.value = '';
}

function deleteRoutine() {
  // 삭제 로직
  alert("아직 준비중입니다.");
  // alert("삭제되었습니다.");
  //location.reload();
}

watch(() => props.updateBlock, (newVal) => {
      const updateStartDateTime = getTodayDate() + `T${newVal.displayStartTime}`;

      if (updateStartDateTime !== startDate.value) {
        startDate.value = updateStartDateTime;
        endDate.value = getTodayDate() + `T${newVal.displayEndTime}`;
        routineType.value = newVal.routineType;
        content.value = newVal.routineDescription;
        isUpdateMode.value = true;
      }
    }, {deep: true}
)

</script>

<template>
  <div class="data-container">
    <ContentTitle :input-name="'한 일 등록'"/>
    <DateInput :input-name="'시작'" :start-date-time="startDate" @change-date="date => startDate = date"/>
    <DateInput :end-date-time="endDate" :input-name="'끝'" @change-date="date => endDate = date"/>
    <TypeInput :input-name="'타입'" :routine-type="routineType" @change-type="type => routineType = type"/>
    <ContentInput :content="content" :input-name="'내용'" @submit="postRoutine"
                  @change-content="typingContent => content = typingContent"/>

    <div v-if="!isUpdateMode" class="submitBtnBox">
      <button class="btn btn-secondary" type="button" @click="postRoutine">등록</button>
      <p>(※ Ctrl + Enter를 입력하셔도 등록이 됩니다.)</p>
    </div>
    <div v-if="isUpdateMode" class="editBtnBox">
      <button class="btn btn-primary" type="button" @click="updateRoutine">수정</button>
      <button class="btn btn-secondary" type="button" @click="cancelUpdate">취소</button>
      <button class="btn btn-danger" type="button" @click="deleteRoutine">삭제</button>
      <p>(※ Ctrl + Enter를 입력하셔도 수정이 됩니다.)</p>
    </div>
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

.submitBtnBox button {
  width: 100%;
  margin-bottom: 10px;
}

.editBtnBox button {
  margin-left: 10px;
  margin-right: 10px;
  margin-bottom: 10px;
  width: 26%
}

</style>
