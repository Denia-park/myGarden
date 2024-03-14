<script setup>
import ContentTitle from "@/components/default/ContentTitle.vue";
import DateInput from "@/components/dailyRoutine/input/DateInput.vue";
import ContentInput from "@/components/dailyRoutine/input/ContentInput.vue";
import TypeInput from "@/components/dailyRoutine/input/TypeInput.vue";
import {onMounted, ref, watch} from "vue";
import {deleteDailyRoutineApi, postDailyRoutineApi, updateDailyRoutineApi} from "@/components/dailyRoutine/api/api.js";
import {store} from "@/scripts/store.js";
import {getTodayDate} from "@/components/dailyRoutine/api/util.js";

/**
 * 하루 일과 입력에 필요한 내용
 */
const startDate = ref('');
const endDate = ref('');
const content = ref('');
const routineType = ref('STUDY');
const isUpdateMode = ref(false);

/**
 * 오늘 마지막 시작 시간을 가져온다.
 */
function getLastStartDateTime() {
  let todayLastStartDateTime = localStorage.getItem("todayLastStartDateTime");
  const todayDate = getTodayDate();

  //localStorage에 데이터가 없거나, 데이터 확인 했는데 오늘 날짜가 아니면 초기화
  if (todayLastStartDateTime === null || todayLastStartDateTime.split("T")[0] !== todayDate) {
    todayLastStartDateTime = "" + todayDate + "T00:00:00";
  }

  startDate.value = todayLastStartDateTime;
}

/**
 * 버튼을 누르거나, Ctrl + Enter를 눌렀을 때 실행되는 함수<br>
 * 일과 등록 유효성 검사 후, 일과 등록 또는 수정을 실행한다.
 */
function submit() {
  if (!validate()) {
    return;
  }

  if (isUpdateMode.value)
    updateRoutine();
  else
    postRoutine();
}

/**
 * 일과 등록 유효성 검사
 */
function validate() {
  //시작 날짜가 끝 날짜보다 늦을 수 없다.
  function validateStartDateIsBeforeEndDate() {
    if (startDate.value > endDate.value) {
      alert("시작 날짜가 끝 날짜보다 늦을 수 없습니다.")
      return false;
    }

    return true;
  }

  if (!validateStartDateIsBeforeEndDate()) {
    return false;
  }

  /**
   * 두 날짜의 차이가 1일 이상 나는지 검증한다.
   * @returns {boolean}
   */
  function validateDateDifferenceIsSmaller1() {
    // Calculate the time difference in milliseconds
    const timeDifference = new Date(endDate.value) - new Date(startDate.value);

    // Calculate the number of days difference
    const daysDifference = timeDifference / (1000 * 3600 * 24);

    // Check if the difference is more than 2 days
    if (daysDifference >= 1) {
      alert("1일 이상 차이 날 수 없습니다.");
      return false;
    }

    return true;
  }

  if (!validateDateDifferenceIsSmaller1()) {
    return false;
  }

  /**
   * content의 길이가 255자를 넘지 않는지 검증한다.
   */
  function validateContentLength() {
    if (content.value.length > 255) {
      alert("content는 255자를 넘을 수 없습니다.")
      return false;
    }

    return true;
  }

  return validateContentLength();
}

/**
 * 일과 등록
 */
function postRoutine() {
  if (!validate()) {
    return;
  }

  postDailyRoutineApi(startDate.value, endDate.value, routineType.value, content.value);

  localStorage.setItem("todayLastStartDateTime", endDate.value);
}

/**
 * 일과 수정
 */
function updateRoutine() {
  if (!updateValidate()) {
    return;
  }

  updateDailyRoutineApi(store.getters.getEditBlock.id, startDate.value, endDate.value, routineType.value, content.value);
}

/**
 * 일과 수정 유효성 검사
 */
function updateValidate() {
  if (!validate()) {
    return false;
  }

  /**
   * 수정 하려는 날짜가 오늘 날짜인지 검증한다.
   * @returns {boolean} 오늘 내의 날짜가 아니면 false, 아니면 true
   */
  function isNotTodayDate() {
    const todayDate = getTodayDate();
    const tempStartDate = startDate.value.split("T")[0];
    const tempEndDate = endDate.value.split("T")[0];

    return tempStartDate !== todayDate || tempEndDate !== todayDate;
  }

  if (isNotTodayDate()) {
    alert("오늘 내의 날짜만 수정이 가능합니다.");
    return false;
  }

  return true;
}

/**
 * 수정 취소
 */
function cancelUpdate() {
  isUpdateMode.value = false;
  getLastStartDateTime();
  endDate.value = '';
  routineType.value = 'STUDY'
  content.value = '';
}

/**
 * 일과 삭제
 */
function deleteRoutine() {
  deleteDailyRoutineApi(store.getters.getEditBlock.id);
}

onMounted(() => {
  getLastStartDateTime();
});

/**
 * 수정 모드로 변경
 */
watch(() => store.getters.getEditBlock, (newVal) => {
      const updateStartDateTime = newVal.startDate + 'T' + newVal.displayStartTime;

      if (updateStartDateTime !== startDate.value) {
        startDate.value = updateStartDateTime;
        endDate.value = newVal.endDate + 'T' + newVal.displayEndTime;
        routineType.value = newVal.routineType;
        content.value = newVal.routineDescription;
        isUpdateMode.value = true;
      }
    }, {deep: true}
)

</script>

<template>
  <div class="data-container">
    <ContentTitle :input-name="'일과 등록'"/>
    <div class="time-input-group">
      <DateInput :input-name="'시작 시간'" :start-date-time="startDate" @change-date="date => startDate = date"/>
      <DateInput :end-date-time="endDate" :input-name="'끝난 시간'" @change-date="date => endDate = date"/>
      <TypeInput :input-name="'타입'" :routine-type="routineType" @change-type="type => routineType = type"/>
    </div>

    <ContentInput :content="content" :input-name="'내용'" @submit="submit"
                  @change-content="typingContent => content = typingContent"/>

    <div v-if="!isUpdateMode" class="submitBtnBox">
      <button class="btn btn-primary" type="button" @click="postRoutine">등록</button>
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
  height: 380px;
  text-align: center;
  margin: 0 auto;
  padding: 0 10px 0 10px;
}

p {
  font-size: 15px;
  margin-bottom: 0;
}

.submitBtnBox button {
  width: 60%;
  margin-bottom: 3px;
}

.editBtnBox button {
  margin-left: 10px;
  margin-right: 10px;
  margin-bottom: 3px;
  width: 26%
}

.time-input-group {
  display: flex;
  flex-direction: row;
  margin-bottom: 10px;

  align-items: center;
}

</style>
