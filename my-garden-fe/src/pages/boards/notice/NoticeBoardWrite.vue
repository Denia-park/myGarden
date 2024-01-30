<script setup>

import {router} from "@/scripts/router.js";
import {useRoute} from "vue-router";

const route = useRoute();

function goToPage(pageName) {
  router.push({
    name: pageName,
    query: route?.query
  });
}
</script>

<template>
  <div class="wrapper">
    <h1>공지사항 작성</h1>
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
              <option value="all">분류 선택</option>
              <option value="java">Java</option>
              <option value="javascript">JavaScript</option>
              <option value="database">DataBase</option>
            </select>
          </td>
        </tr>
        <tr>
          <th scope="row">제목<span class="t_red">*</span></th>
          <td><input id="board_writer" class="tbox01" name="board_writer" placeholder="제목을 입력해주세요" value=""/></td>
        </tr>
        <tr>
          <th scope="row">내용<span class="t_red">*</span></th>
          <td><textarea id="board_content" class="textarea01" name="board_content"></textarea></td>
        </tr>
        </tbody>
      </table>
    </div>

    <div class="post_bot_button_box">
      <button id="save_btn">저장</button>
      <button id="cancel_btn" @click="goToPage('NoticeBoardList')">취소</button>
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

/* textarea */
textarea.textarea01 {
  width: 100%;
  height: 300px;
  margin: 10px 0
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
  margin-top: 30px;

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

</style>
