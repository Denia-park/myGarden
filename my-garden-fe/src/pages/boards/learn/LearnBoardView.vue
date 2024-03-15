<script setup>
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import {
  deleteLearnBoardApi,
  deleteLearnBoardCommentApi,
  getLearnBoardCategoryApi,
  getLearnBoardCommentsApi,
  getLearnBoardViewApi,
  postLearnBoardCommentApi
} from "@/components/boards/learn/api/api.js";
import {router} from "@/scripts/router.js";
import BoardView from "@/components/boards/common/BoardView.vue";
import {isUserAccount} from "@/components/boards/common/util/util.js";

const route = useRoute()
const board = ref({});
const categories = ref([]);
const comments = ref([]);
const BOARD_TYPE = 'learn';

/**
 * 목록으로 이동
 */
function goToList() {
  router.push({
    name: "LearnBoardList",
    query: route.query
  });
}

/**
 * 수정 페이지로 이동
 */
function goToEdit() {
  router.push({
    name: "LearnBoardEdit",
    params: {boardId: route.params.boardId},
    query: route.query
  });
}

/**
 * 게시글 삭제
 */
function deleteBoard() {
  if (!confirm("정말 삭제하시겠습니까?")) {
    return;
  }

  deleteLearnBoardApi(route.params.boardId)
      .then(() => {
        alert("삭제되었습니다.");
        goToList();
      });
}

/**
 * 댓글 등록
 * @param comment
 */
function submitComment(comment) {
  postLearnBoardCommentApi(BOARD_TYPE, route.params.boardId, comment)
      .then(
          () => {
            getLearnBoardCommentsApi(BOARD_TYPE, route.params.boardId)
                .then(response => {
                  comments.value = response;
                });
          }
      );
}

/**
 * 댓글 삭제
 * @param commentId
 */
function deleteComment(commentId) {
  if (!confirm("정말 삭제하시겠습니까?")) {
    return;
  }

  deleteLearnBoardCommentApi(BOARD_TYPE, route.params.boardId, commentId)
      .then(() => {
        getLearnBoardCommentsApi(BOARD_TYPE, route.params.boardId)
            .then(response => {
              comments.value = response;
            });
      });
}

onMounted(() => {
  getLearnBoardCategoryApi(BOARD_TYPE)
      .then(response => {
        categories.value = response;
      });
  getLearnBoardViewApi(route.params.boardId)
      .then(response => {
        board.value = response;
      });
  getLearnBoardCommentsApi(BOARD_TYPE, route.params.boardId)
      .then(response => {
        comments.value = response;
      });
});
</script>

<template>
  <BoardView :board="board" :categories="categories" :comments="comments"
             :isAccessAccount="isUserAccount()" :title="'TIL'"
             @deleteBoard="deleteBoard" @deleteComment="deleteComment" @goToEdit="goToEdit" @goToList="goToList"
             @submitComment="submitComment"
  />
</template>

<style scoped>

</style>
