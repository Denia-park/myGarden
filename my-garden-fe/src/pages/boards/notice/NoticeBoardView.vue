<script setup>
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import {getNoticeBoardCategoryApi, getNoticeBoardViewApi} from "@/components/boards/notice/api/api.js";
import {convertCategoryCodeToText} from "@/components/boards/common/util/util.js";
import {router} from "@/scripts/router.js";
import {store} from "@/scripts/store.js";

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

onMounted(() => {
  getNoticeBoardCategoryApi()
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
  <div class="wrapper">
    <h1>공지사항</h1>

    <div class="detail_title_box">
      <span id="category">{{ convertCategoryCodeToText(categories, board.category) }}</span>
      <p id="title">{{ board.title }}</p>
      <span id="writtenAt"> {{ board.writtenAt }} </span>
      <span id="writer"> {{ board.writer }} </span>
    </div>

    <hr id="title_box_bot_line"/>

    <div id="views"> 조회수: {{ board.views }}</div>

    <div class="detail_content_box">
      <div class="content">
        {{ board.content }}
      </div>
    </div>

    <hr id="reply_box_bot_line"/>

    <div class="detail_bot_button_box">
      <button id="list_btn" @click="goToList">목록</button>
      <button v-if="isAdminAccount" id="edit_btn" @click="goToEdit">수정</button>
      <button v-if="isAdminAccount" id="delete_btn" @click="goToEdit">삭제</button>
    </div>
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

.detail_title_box {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  font-size: 25px;
}

#category {
  font-size: 20px;
  font-weight: bold;

  margin-right: 20px;
}

#title {
  width: 800px;
  margin-bottom: 0;
}

#writtenAt {
  font-size: 15px;
  font-weight: bold;
  margin-right: 20px;
}

#writer {
  font-size: 15px;
  font-weight: bold;
}

#views {
  font-size: 15px;
  font-weight: bold;
  margin-bottom: 30px;
  margin-right: 20px;

  text-align: right;
}

.detail_content_box {
  border: 1px solid black;
  padding: 15px;
  margin-bottom: 20px;

  min-height: 400px;
}

.detail_bot_button_box {
  display: flex;
  justify-content: center;
}

#list_btn {
  width: 150px;
  background: mediumseagreen;
  color: white;
  border: none;
  border-radius: 5px;
}

#edit_btn {
  width: 150px;
  background: dodgerblue;
  color: white;
  border: none;
  border-radius: 5px;
  margin-left: 20px;
}

#delete_btn {
  width: 150px;
  background: red;
  color: white;
  border: none;
  border-radius: 5px;
  margin-left: 20px;
}

</style>
