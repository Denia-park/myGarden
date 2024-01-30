<script setup>
import PaginationForm from "@/components/boards/common/PaginationForm.vue";
import TableContents from "@/components/boards/common/TableContents.vue";

import {onMounted, ref, watch} from "vue";
import {getNoticeBoardCategoryApi, getNoticeBoardListApi} from "@/components/boards/notice/api/api.js";
import {store} from "@/scripts/store.js";
import {getOneMonthAgoDate, getTodayDate} from "@/components/dailyRoutine/api/util.js";
import SearchForm from "@/components/boards/common/SearchForm.vue";
import {router} from "@/scripts/router.js";
import {useRoute} from "vue-router";

const noticePage = ref({});
const noticeTotalCount = ref(0);
const categories = ref([]);
const queryParameter = ref({
  startDate: getOneMonthAgoDate(),
  endDate: getTodayDate(),
  category: "",
  searchText: "",
  currentPage: 1,
  pageSize: 10,
  sort: "writtenAt",
  order: "desc"
});

function isAdminAccount() {
  return store.getters.getRoles.includes("ROLE_ADMIN");
}

function pageChange(currentPage) {
  queryParameter.value.currentPage = currentPage;
  getNoticeBoardList(queryParameter.value);
}

function getNoticeBoardList(parameter) {
  if (parameter) {
    queryParameter.value = parameter;
  }

  getNoticeBoardListApi(parameter)
      .then(response => {
        noticePage.value = response;
      });
}

function getNoticeBoardCategory() {
  getNoticeBoardCategoryApi('notice')
      .then(response => {
        categories.value = response;
      });
}

function goToBoardView(boardId) {
  router.push({
    name: "NoticeBoardView",
    params: {boardId: boardId},
    query: queryParameter.value
  });
}

function goToPage(pageName) {
  router.push({
    name: pageName,
    query: queryParameter.value
  });
}

watch(() => noticePage.value, () => {
  noticeTotalCount.value = noticePage.value.totalElements;
});

onMounted(() => {
  if (Object.keys(useRoute().query).length !== 0) {
    queryParameter.value = useRoute().query;
  }

  getNoticeBoardCategory();
  getNoticeBoardList(queryParameter.value);
});
</script>

<template>
  <div class="wrapper">
    <h1>공지사항</h1>

    <SearchForm :categories="categories" :query-parameter="queryParameter"
                @search="getNoticeBoardList"/>

    <div class="total-content-wrapper">
      총 <span> {{ noticeTotalCount }}</span>개의 글이 있습니다.
    </div>

    <TableContents :categories="categories" :table-content-page="noticePage" @go-to-board-view="goToBoardView"/>

    <PaginationForm :page-info="noticePage" @page-change="pageChange"/>

    <button v-if="isAdminAccount()" class="button filter-height align_left" type="button"
            @click="() => goToPage('NoticeBoardWrite')">
      등록
    </button>
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
