<script setup>
import {ref} from "vue";
import {getTodayDate} from "@/components/dailyRoutine/api/api.js";

const showModal = ref(false);
const selectedDate = ref(getTodayDate());

const emit = defineEmits(["update-date"]);

function openModal() {
  showModal.value = true;
}

function closeModal() {
  showModal.value = false;
}

function updateDate(event) {
  selectedDate.value = event.target.value;
  closeModal();
  emit("update-date", selectedDate.value);
}

function handleClickOutside(event) {
  const modalContent = document.querySelector('.modal-content');
  if (!modalContent.contains(event.target)) {
    closeModal();
  }
}
</script>
<template>
  <button class="date-button btn btn-success" @click="openModal">조회 날짜 선택</button>
  <h4 v-if="selectedDate" class="date-text">선택한 날짜 : {{ selectedDate }}</h4>

  <!-- 모달 바깥쪽 클릭 시 closeModal 호출 -->
  <div v-if="showModal" class="modal" @click="handleClickOutside">
    <!-- stop modifier는 이벤트 버블링을 막습니다 -->
    <div class="modal-content" @click.stop>
      <h4>조회 날짜 선택</h4>
      <input :value="selectedDate" type="date" @change="updateDate"/>
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
}

.date-text {
  position: absolute;
  top: 0;
  right: 0;
  margin-top: 42px;
  margin-right: 10px;
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

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 600px;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

input {
  width: 100%;
  font-size: 30px;
  height: 40px;
  margin-top: 10px;
  margin-bottom: 10px;
}

</style>
