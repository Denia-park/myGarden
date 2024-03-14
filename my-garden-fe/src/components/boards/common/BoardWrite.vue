<script setup>

import {ref, watch} from "vue";

const props = defineProps({
  title: {
    type: String,
    required: false
  },
  boardId: {
    type: String,
    required: false
  },
  categories: {
    type: Array,
    required: true
  },
  isEditPage: {
    type: Boolean,
    required: true
  },
  boardRouteName: {
    type: String,
    required: true
  },
  isImportantCheck: {
    type: Boolean,
    required: false
  },
  content: {
    type: String,
    required: false
  }
});

const emit = defineEmits(["saveBoard", "goToBackPage"]);

/**
 * 게시글 내용
 */
const content = ref('');

/**
 * 이전 페이지로 이동
 */
function goToBackPage() {
  emit('goToBackPage', props.boardRouteName, props.boardId)
}

/**
 * 게시글 저장
 */
function save() {
  const category = document.getElementById("category").value;
  const title = document.getElementById("board_writer").value;

  const board = {
    category: category,
    title: title,
    content: content.value,
  };

  if (props.isImportantCheck) {
    board.isImportant = document.getElementById("isImportant")?.checked;
  }

  if (props.isEditPage) {
    board.id = props.boardId;
  }

  emit('saveBoard', board);
}

watch(() => props.content, () => {
  content.value = props.content;
});

</script>

<template>
  <div class="wrapper">
    <h1>{{ title }} 작성</h1>
    <div>
      <div class="caption">
        <span class="t_red">*</span> 표시는 필수입력 항목입니다.
      </div>
      <table aria-describedby="contentsWrite" class="post_table">
        <tbody id="tbody">
        <tr>
          <th scope="row">분류<span class="t_red">*</span></th>
          <td>
            <select id="category" class="tbox01" name="category">
              <option value="">분류 선택</option>
              <option v-for="category in categories" :key="category.code" :value="category.code">
                {{ category.text }}
              </option>
            </select>
          </td>
        </tr>
        <tr>
          <th scope="row">제목<span class="t_red">*</span></th>
          <td><input id="board_writer" class="tbox01" name="board_writer" placeholder="제목을 입력해주세요" value=""/></td>
        </tr>
        <tr>
          <th scope="row">내용<span class="t_red">*</span></th>
          <td>
            <v-md-editor v-model="content" height="400px"></v-md-editor>
          </td>
        </tr>
        <tr v-if="isImportantCheck">
          <th scope="row">알림글</th>
          <td>
            <input id="isImportant" name="isImportant" type="checkbox" value="Y"/>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div class="post_bot_button_box">
      <button v-if="isEditPage" id="edit_btn" @click="save">수정</button>
      <button v-else id="save_btn" @click="save">저장</button>
      <button id="cancel_btn" @click="goToBackPage"> 취소</button>
    </div>
  </div>
</template>

<style scoped>
.wrapper {
  max-width: 1280px;
  margin: 0 auto;

  display: flex;
  flex-direction: column;
  justify-content: center;
  flex-wrap: wrap;
  padding: 0 2rem;
}

.caption {
  font-size: 14px;
  font-weight: bold;
  margin: 10px;
}

.post_table {
  text-align: left;
  vertical-align: center;

  border-collapse: collapse;
  border-top: 1px solid #c9c9c9;

  width: 100%;
  height: auto;
}

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

input.tbox01 {
  width: 100%;
  height: 32px;
}

td select {
  width: 208px;
  height: 32px;
}

.t_red {
  color: #f55500
}

.post_bot_button_box button {
  border: none;
  height: 30px;
  width: 90px;
}

.post_bot_button_box {
  display: flex;
  justify-content: center;
}

.post_bot_button_box button#save_btn {
  background-color: mediumseagreen;
  color: whitesmoke;
  margin-right: 30px;
}

.post_bot_button_box button#cancel_btn {
  background-color: lightgray;
  color: black;
}

.post_bot_button_box button#edit_btn {
  background: dodgerblue;
  color: white;
  margin-right: 20px;
}

</style>
