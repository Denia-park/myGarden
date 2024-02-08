<script setup>
import PaginationForm from "@/components/boards/common/PaginationForm.vue";
import TableContents from "@/components/boards/common/TableContents.vue";

import {onMounted, ref, watch} from "vue";
import {getNoticeBoardCategoryApi, getNoticeBoardListApi} from "@/components/boards/notice/api/api.js";
import {getOneMonthAgoDate, getTodayDate} from "@/components/dailyRoutine/api/util.js";
import SearchForm from "@/components/boards/common/SearchForm.vue";
import {router} from "@/scripts/router.js";
import {useRoute} from "vue-router";
import TotalElementCounter from "@/components/boards/common/TotalElementCounter.vue";
import WriteButton from "@/components/boards/common/WriteButton.vue";
import {isAdminAccount} from "@/components/boards/common/util/util.js";

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

    <TotalElementCounter :total-element="noticeTotalCount"/>

    <TableContents :categories="categories" :table-content-page="noticePage" @go-to-board-view="goToBoardView"/>

    <PaginationForm :page-info="noticePage" @page-change="pageChange"/>

    <WriteButton :is-access-account="isAdminAccount()" :query-parameter="queryParameter"
                 :write-page-name="'NoticeBoardWrite'"/>
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
  margin: 20px 0;
}

</style>
