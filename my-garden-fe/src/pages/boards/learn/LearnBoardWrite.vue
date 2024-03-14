<script setup>

import {router} from "@/scripts/router.js";
import {useRoute} from "vue-router";
import {getLearnBoardCategoryApi, getLearnBoardViewApi} from "@/components/boards/learn/api/api.js";
import {onMounted, ref} from "vue";
import {postBoardApi} from "@/components/boards/common/api/api.js";
import BoardWrite from "@/components/boards/common/BoardWrite.vue";

const route = useRoute();
const categories = ref([]);
const boardId = route.params?.boardId;
const isImportantCheck = false;
const content = ref("");

/**
 * 현재 페이지가 수정 페이지인지 확인
 *
 * @returns {boolean} 수정 페이지 여부
 */
function isEditPage() {
  return boardId !== undefined;
}

/**
 * 이전 페이지로 이동
 *
 * @param pageName 이동할 페이지 이름
 * @param boardId 게시글 ID
 */
function goToBackPage(pageName, boardId) {
  if (isEditPage()) {
    router.push({
      name: pageName + 'View',
      params: {boardId: boardId},
      query: route.query
    });
  } else {
    router.push({
      name: pageName + 'List',
      query: route.query
    });
  }
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
 * 게시글 저장 전에 유효성 검사
 *
 * @param board 저장할 게시글
 */
function validate(board) {
  if (board.category === "") {
    alert("분류를 선택해주세요.");
    return true;
  }

  if (board.title === "") {
    alert("제목을 입력해주세요.");
    return true;
  }
  if (board.title.length > 100) {
    alert("제목은 100자 이내로 입력해주세요.");
    return true;
  }

  if (board.content === "") {
    alert("내용을 입력해주세요.");
    return true;
  }

  if (board.content.length > 4000) {
    alert("내용은 4000자 이내로 입력해주세요.");
    return true;
  }

  return false;
}

/**
 * 게시글 저장
 *
 * @param board 저장할 게시글 정보
 */
function saveBoard(board) {
  if (validate(board)) {
    return;
  }

  postBoardApi('learn', board, boardId);
}

/**
 * 응답 데이터로 입력 폼 채우기
 *
 * @param response 응답 데이터
 */
function fillInputFromResponse(response) {
  document.getElementById("category").value = response.category;
  document.getElementById("board_writer").value = response.title;
  content.value = response.content;
  
  if (isImportantCheck) {
    document.getElementById("isImportant").checked = response.isImportant;
  }
}

onMounted(() => {
  getLearnBoardCategory();
  if (isEditPage()) {
    getLearnBoardViewApi(boardId)
        .then(response => {
          fillInputFromResponse(response);
        });
  }
});
</script>

<template>
  <BoardWrite :board-id="boardId" :board-route-name="'LearnBoard'" :categories="categories" :content="content"
              :is-edit-page="isEditPage()" :is-important-check="isImportantCheck" :title="'TIL'"
              @goToBackPage="goToBackPage" @saveBoard="saveBoard"/>
</template>

<style scoped>

.post_table tbody th, .post_table tbody td {
  font-size: 15px;
  font-weight: bold;

  padding: 10px 15px;
  color: #333;
  background: #ececec;
  border-bottom: 1px solid #c9c9c9;
}

.post_table tbody th {
  width: 20%;
  background: #d5d3d3;
}

.post_table tbody tr {
  height: 50px;
}

td select {
  width: 208px;
  height: 32px;
}

.post_bot_button_box button {
  border: none;
  height: 30px;
  width: 90px;
}

</style>
