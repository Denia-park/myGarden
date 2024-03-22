<script setup>
import {onMounted, ref} from "vue";
import {convertDateFormat, getTodayDate} from "@/components/dailyRoutine/api/util.js";
import {store} from "@/scripts/store.js";
import {router} from "@/scripts/router.js";
import {CalendarHeatmap} from "vue3-calendar-heatmap";
import 'vue3-calendar-heatmap/dist/style.css';
import {getStudyHoursExceptTodayApi} from "@/components/dailyRoutine/api/api.js";

/**
 * 모달을 보여줄지 여부
 */
const showModal = ref(false);
/**
 * 공부 시간을 저장한 배열
 */
const studyHours = ref([]);

/**
 * 조회 날짜
 */
const inputDate = ref(new Date());

/**
 * 오늘 공부 시간 추가
 */
function addTodayStudyHour() {
  studyHours.value.push({
    date: getTodayDate(),
    count: Math.floor(store.getters.getStudyHoursToday),
  });
}

/**
 * 오늘을 제외한 공부 시간 조회
 */
function getStudyHours() {
  let studyHoursArrExceptToday = store.getters.getStudyHoursArrExceptToday;
  if (studyHoursArrExceptToday.length === 0) {
    getStudyHoursExceptTodayApi()
        .then(data => {
          store.commit("setStudyHoursArrExceptToday", data);
          studyHours.value = data;
          addTodayStudyHour();
        })
  } else {
    studyHours.value = studyHoursArrExceptToday;
    addTodayStudyHour();
  }
}

/**
 * 모달 열기
 */
function openModal() {
  showModal.value = true;
}

/**
 * 모달 닫기
 */
function closeModal() {
  showModal.value = false;
}

/**
 * 조회 날짜 변경
 *
 * @param date 변경할 날짜
 */
function updateDate(date) {
  inputDate.value = date;
  closeModal();
  store.commit("setViewDate", convertDateFormat(date));
}

/**
 * 일과 통계 페이지로 이동
 */
function goToStatistics() {
  router.push('/daily-routine/statistics');
}

/**
 * 모달 바깥쪽 클릭 시 모달 닫기
 *
 * @param event 클릭 이벤트
 */
function handleClickOutside(event) {
  const modalContent = document.querySelector('.modal-content');
  if (!modalContent.contains(event.target)) {
    closeModal();
  }
}

onMounted(() => {
  getStudyHours();
});
</script>
<template>
  <div class="date-wrapper">
    <button class="statistics-button btn btn-info" @click="goToStatistics">
      일과 통계
      <br/>
      모아 보기
    </button>
    <button class="date-button btn btn-success" @click="openModal">
      조회 날짜
      <br/>
      {{ convertDateFormat(inputDate) }}
    </button>
  </div>

  <!-- 모달 바깥쪽 클릭 시 closeModal 호출 -->
  <div v-if="showModal" class="modal" @click="handleClickOutside">
    <!-- stop modifier는 이벤트 버블링을 막습니다 -->
    <div class="modal-content" @click.stop>
      <h4>조회 날짜 선택</h4>
      <button class="btn btn-success today-button" @click="updateDate(new Date())">오늘</button>
      <div>
        <VDatePicker v-model="inputDate" class="date-picker" mode="date" style="width: 35%"
                     @update:modelValue="updateDate"/>
      </div>
      <div class="heatmapBox">
        <h4>공부 시간</h4>
        <CalendarHeatmap :end-date="getTodayDate()" :values="studyHours" tooltip-unit="Hours"/>
      </div>
      <span class="close" @click="closeModal">close</span>
    </div>
  </div>
</template>

<style scoped>
.date-button {
  position: absolute;
  top: 0;
  right: 0;
  margin-top: 3px;
  margin-right: 10px;

  width: 150px;
}

.statistics-button {
  position: absolute;
  top: 0;
  right: 0;
  margin-top: 3px;
  margin-right: 170px;

  width: 150px;
}

@media (max-width: 750px) {
  .date-wrapper {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
  }

  .date-button {
    position: relative;

    width: 150px;
    margin-top: 3px;
    margin-right: 10px;
    margin-bottom: 10px;
  }

  .statistics-button {
    position: relative;

    width: 150px;
    margin-top: 3px;
    margin-right: 10px;
    margin-bottom: 10px;
  }
}

.modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgb(0, 0, 0);
  background-color: rgba(0, 0, 0, 0.4);
}

.modal h4 {
  font-size: 31px;
  margin-bottom: 20px;
}

.modal-content {
  background-color: #fefefe;
  margin: 6% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 1200px;
  height: 750px;

  position: relative;
}

.today-button {
  top: 20px;
  right: 20px;
  position: absolute;
}

.close {
  color: #aaa;
  float: right;
  font-size: 31px;
  font-weight: bold;

  margin-top: 10px;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

.heatmapBox {
  margin: 30px 20px;
}
</style>
