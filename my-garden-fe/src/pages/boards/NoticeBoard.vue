<script setup>
import SearchForm from "@/components/boards/common/SearchForm.vue";
import PaginationForm from "@/components/boards/common/PaginationForm.vue";
import TableContents from "@/components/boards/common/TableContents.vue";

import {ref, watch} from "vue";
import {getNoticeBoardListApi} from "@/components/boards/notice/api/api.js";
import {store} from "@/scripts/store.js";
import {getOneMonthAgoDate, getTodayDate} from "@/components/dailyRoutine/api/util.js";

const noticePage = ref({});
const noticeTotalCount = ref(0);
const categories = ref([]);
const startDate = ref(getTodayDate());
const endDate = ref(getOneMonthAgoDate());

function isAdminAccount() {
  return store.getters.getRoles.includes("ROLE_ADMIN");
}

getNoticeBoardListApi().then(response => {
  noticePage.value = response;
});

watch(() => noticePage.value, () => {
  noticeTotalCount.value = noticePage.value.content.length;
});
</script>

<template>
  <div class="wrapper">
    <h1>공지사항</h1>

    <!--TODO: 검색 조건 (날짜, 카테고리) 전달해야 함 -->
    <SearchForm :categories="categories" :end-date="endDate" :start-date="startDate"/>

    <div class="total-content-wrapper">
      총 <span> {{ noticeTotalCount }}</span>개의 글이 있습니다.
    </div>

    <TableContents :table-content-page="noticePage"/>

    <!--TODO: 페이지 정보 전달해야 함 -->
    <PaginationForm/>

    <button v-if="isAdminAccount()" class="button filter-height align_left" type="button">등록</button>
  </div>
</template>

<style scoped>
.wrapper {
  max-width: 1280px;
  margin: 0 auto;
  font-weight: normal;

  display: flex;
  flex-direction: column;
  justify-content: center;
  flex-wrap: wrap;
  padding: 0 2rem;
}

h1 {
  text-align: center;
  margin: 30px 0;
}

.total-content-wrapper {
  margin: 10px 0 10px 0;
}

.total-content-wrapper span {
  font-size: 15px;
  font-weight: bold;
}


.filter-height {
  height: 35px;
}

.button {
  width: 75px;
  background: dodgerblue;
  color: white;
  border: none;
  border-radius: 5px;
}

.align_left {
  float: right;
  margin-right: 10px;
}
</style>
