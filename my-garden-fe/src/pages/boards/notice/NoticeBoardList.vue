<script setup>
import PaginationForm from "@/components/boards/common/PaginationForm.vue";
import TableContents from "@/components/boards/common/TableContents.vue";

import {onMounted, ref, watch} from "vue";
import {
  getNoticeBoardCategoryApi,
  getNoticeBoardListApi,
  getNoticeImportantBoardListApi
} from "@/components/boards/notice/api/api.js";
import {getOneYearAgoDate, getTodayDate} from "@/components/dailyRoutine/api/util.js";
import SearchForm from "@/components/boards/common/SearchForm.vue";
import {router} from "@/scripts/router.js";
import {useRoute} from "vue-router";
import TotalElementCounter from "@/components/boards/common/TotalElementCounter.vue";
import WriteButton from "@/components/boards/common/WriteButton.vue";
import {isAdminAccount} from "@/components/boards/common/util/util.js";

/**
 * 공지사항 게시판 구성에 필요한 변수
 */
const noticePage = ref({});
const noticeImportantPage = ref([]);
const noticeTotalCount = ref(0);
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
  getNoticeBoardList(queryParameter.value);
}

/**
 * 공지사항 중요 게시글 목록 조회
 */
function getNoticeImportantBoardList() {
  getNoticeImportantBoardListApi()
      .then(response => {
        noticeImportantPage.value = response;
      });
}

/**
 * 공지사항 목록 조회
 *
 * @param parameter 조회 조건
 */
function getNoticeBoardList(parameter) {
  if (parameter) {
    queryParameter.value = parameter;
  }

  getNoticeBoardListApi(parameter)
      .then(response => {
        noticePage.value = response;
      });
}

/**
 * 공지사항 카테고리 조회
 */
function getNoticeBoardCategory() {
  getNoticeBoardCategoryApi('notice')
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
  getNoticeImportantBoardList();
  getNoticeBoardList(queryParameter.value);
});
</script>

<template>
  <div class="wrapper">
    <h1>공지사항</h1>

    <SearchForm :categories="categories" :query-parameter="queryParameter"
                @search="getNoticeBoardList"/>

    <TotalElementCounter :total-element="noticeTotalCount"/>

    <TableContents :categories="categories" :table-content-page="noticePage"
                   :table-important-content-page="noticeImportantPage" @go-to-board-view="goToBoardView"/>

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
