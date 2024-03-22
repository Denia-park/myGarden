<script setup>
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import {
  deleteNoticeBoardApi,
  getNoticeBoardCategoryApi,
  getNoticeBoardViewApi
} from "@/components/boards/notice/api/api.js";
import {router} from "@/scripts/router.js";
import BoardView from "@/components/boards/common/BoardView.vue";
import {isAdminAccount} from "@/components/boards/common/util/util.js";

const route = useRoute()
const board = ref({});
const categories = ref([]);

/**
 * 목록으로 이동
 */
function goToList() {
  router.push({
    name: "NoticeBoardList",
    query: route.query
  });
}

/**
 * 수정 페이지로 이동
 */
function goToEdit() {
  router.push({
    name: "NoticeBoardEdit",
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

  deleteNoticeBoardApi(route.params.boardId)
      .then(() => {
        goToList();
      });
}

onMounted(() => {
  getNoticeBoardCategoryApi('notice')
      .then(response => {
        categories.value = response;
      });
  getNoticeBoardViewApi(route.params.boardId)
      .then(response => {
        board.value = response;
      });
});
</script>

<template>
  <BoardView :board="board" :categories="categories" :isAccessAccount="isAdminAccount()"
             :title="'공지사항'"
             @deleteBoard="deleteBoard" @goToEdit="goToEdit" @goToList="goToList"
  />
</template>

<style scoped>

</style>
