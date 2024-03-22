<script setup>
import PaginationForm from "@/components/boards/common/PaginationForm.vue";
import TableContents from "@/components/boards/common/TableContents.vue";

import {onMounted, ref, watch} from "vue";
import {getLearnBoardCategoryApi, getLearnBoardListApi} from "@/components/boards/learn/api/api.js";
import {getOneYearAgoDate, getTodayDate} from "@/components/dailyRoutine/api/util.js";
import SearchForm from "@/components/boards/common/SearchForm.vue";
import {router} from "@/scripts/router.js";
import {useRoute} from "vue-router";
import TotalElementCounter from "@/components/boards/common/TotalElementCounter.vue";
import WriteButton from "@/components/boards/common/WriteButton.vue";
import {isUserAccount} from "@/components/boards/common/util/util.js";

/**
 * TIL 게시판 구성에 필요한 변수
 */
const learnPage = ref({});
const learnTotalCount = ref(0);
const categories = ref([]);
const queryParameter = ref({
  startDate: getOneYearAgoDate(),
  endDate: getTodayDate(),
  category: "",
  searchText: "",
  currentPage: 1,
  pageSize: 10,
  sort: "writtenAt",
  order: "desc"
});

/**
 * 페이지 변경
 *
 * @param currentPage 변경할 페이지
 */
function pageChange(currentPage) {
  queryParameter.value.currentPage = currentPage;
  getLearnBoardList(queryParameter.value);
}

/**
 * TIL 게시판 목록 조회
 *
 * @param parameter 조회 조건
 */
function getLearnBoardList(parameter) {
  if (parameter) {
    queryParameter.value = parameter;
  }

  getLearnBoardListApi(parameter)
      .then(response => {
        learnPage.value = response;
      });
}

/**
 * TIL 게시판 카테고리 조회
 */
function getLearnBoardCategory() {
  getLearnBoardCategoryApi('learn')
      .then(response => {
        categories.value = response;
      });
}

/**
 * 게시판 상세보기 페이지로 이동
 *
 * @param boardId 게시글 번호
 */
function goToBoardView(boardId) {
  router.push({
    name: "LearnBoardView",
    params: {boardId: boardId},
    query: queryParameter.value
  });
}

watch(() => learnPage.value, () => {
  learnTotalCount.value = learnPage.value.totalElements;
});

onMounted(() => {
  if (Object.keys(useRoute().query).length !== 0) {
    queryParameter.value = useRoute().query;
  }

  getLearnBoardCategory();
  getLearnBoardList(queryParameter.value);
});
</script>

<template>
  <div class="wrapper">
    <h1>TIL</h1>

    <SearchForm :categories="categories" :query-parameter="queryParameter"
                @search="getLearnBoardList"/>

    <TotalElementCounter :total-element="learnTotalCount"/>

    <TableContents :categories="categories" :table-content-page="learnPage" @go-to-board-view="goToBoardView"/>

    <PaginationForm :page-info="learnPage" @page-change="pageChange"/>

    <WriteButton :is-access-account="isUserAccount()" :query-parameter="queryParameter"
                 :write-page-name="'LearnBoardWrite'"/>
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
