<script setup>
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import {
  deleteLearnBoardApi,
  getLearnBoardCategoryApi,
  getLearnBoardViewApi
} from "@/components/boards/learn/api/api.js";
import {router} from "@/scripts/router.js";
import BoardView from "@/components/boards/common/BoardView.vue";
import {isUserAccount} from "@/components/boards/common/util/util.js";

const route = useRoute()
const board = ref({});
const categories = ref([]);

function goToList() {
  router.push({
    name: "LearnBoardList",
    query: route.query
  });
}

function goToEdit() {
  router.push({
    name: "LearnBoardEdit",
    params: {boardId: route.params.boardId},
    query: route.query
  });
}

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

onMounted(() => {
  getLearnBoardCategoryApi('learn')
      .then(response => {
        categories.value = response;
      });
  getLearnBoardViewApi(route.params.boardId)
      .then(response => {
        board.value = response;
      });
});
</script>

<template>
  <BoardView :board="board" :categories="categories" :isAccessAccount="isUserAccount()"
             :title="'TIL'"
             @deleteBoard="deleteBoard" @goToEdit="goToEdit" @goToList="goToList"
  />
</template>

<style scoped>

</style>
