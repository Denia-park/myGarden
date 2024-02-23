<script setup>
import {ref} from "vue";
import {convertDateFormat} from "@/components/dailyRoutine/api/util.js";
import {store} from "@/scripts/store.js";
import {router} from "@/scripts/router.js";

const showModal = ref(false);
const inputDate = ref(new Date());

function openModal() {
  showModal.value = true;
}

function closeModal() {
  showModal.value = false;
}

function updateDate(date) {
  inputDate.value = date;
  closeModal();
  store.commit("setViewDate", convertDateFormat(date));
}

function goToStatistics() {
  router.push('/daily-routine/statistics');
}

function handleClickOutside(event) {
  const modalContent = document.querySelector('.modal-content');
  if (!modalContent.contains(event.target)) {
    closeModal();
  }
}
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
        <VDatePicker v-model="inputDate" class="date-picker" mode="date" style="width: 65%"
                     @update:modelValue="updateDate"/>
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
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 500px;

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
</style>
