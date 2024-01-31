<script setup>
import {convertCategoryCodeToText} from "@/components/boards/common/util/util.js";

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  board: {
    type: Object,
    required: true
  },
  categories: {
    type: Array,
    required: true
  },
  isAccessAccount: {
    type: Boolean,
    required: true
  }
});

const emit = defineEmits(["goToList", "goToEdit", "deleteBoard"]);

</script>

<template>
  <div class="wrapper">
    <h1>{{ title }}</h1>

    <div class="detail_title_box">
      <span id="category">{{ convertCategoryCodeToText(categories, board.category) }}</span>
      <p id="title">{{ board.title }}</p>
      <span id="writtenAt"> {{ board.writtenAt }} </span>
      <span id="writer"> {{ board.writer }} </span>
    </div>

    <hr id="title_box_bot_line"/>

    <div id="views"> 조회수: {{ board.views }}</div>

    <div class="detail_content_box">
      <v-md-editor :model-value="board.content" mode="preview"></v-md-editor>
    </div>

    <hr id="reply_box_bot_line"/>

    <div class="detail_bot_button_box">
      <button id="list_btn" @click="() => emit('goToList')">목록</button>
      <button v-if="isAccessAccount" id="edit_btn" @click="() => emit('goToEdit')">수정</button>
      <button v-if="isAccessAccount" id="delete_btn" @click="() => emit('deleteBoard')">삭제</button>
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
