<script setup>
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import {
  deleteNoticeBoardApi,
  getNoticeBoardCategoryApi,
  getNoticeBoardViewApi
} from "@/components/boards/notice/api/api.js";
import {router} from "@/scripts/router.js";
import {store} from "@/scripts/store.js";
import BoardView from "@/components/boards/common/BoardView.vue";

const route = useRoute()
const board = ref({});
const categories = ref([]);

function goToList() {
  router.push({
    name: "NoticeBoardList",
    query: route.query
  });
}

function isAdminAccount() {
  return store.getters.getRoles.includes("ROLE_ADMIN");
}

function goToEdit() {
  router.push({
    name: "NoticeBoardEdit",
    params: {boardId: route.params.boardId},
    query: route.query
  });
}

function deleteBoard() {
  if (!confirm("정말 삭제하시겠습니까?")) {
    return;
  }

  deleteNoticeBoardApi(route.params.boardId)
      .then(() => {
        alert("삭제되었습니다.");
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
  <BoardView :board="board" :categories="categories" :isAdminAccount="isAdminAccount()"
             :title="'공지사항'"
             @deleteBoard="deleteBoard" @goToEdit="goToEdit" @goToList="goToList"
  />
</template>

<style scoped>

</style>
