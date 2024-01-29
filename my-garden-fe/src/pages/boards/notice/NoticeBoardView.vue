<script setup>

</script>

<template>
  <div class="wrapper">
    <h1>공지사항</h1>

    <div class="detail_title_box">
      <span id="category">JAVA</span>
      <p id="title">제목이 출력됩니다.</p>
      <span id="writtenAt">2022.04.08 12:32</span>
      <span id="writer">작성자</span>
    </div>

    <hr id="title_box_bot_line"/>

    <div id="views"> 조회수:</div>

    <div class="detail_content_box">
      <div class="content">
        이것은 내용입니다.이것은 내용입니다.이것은 내용입니다.이것은 내용입니다.이것은 내용입니다.이것은 내용입니다.
        이것은 내용입니다.이것은 내용입니다.이것은 내용입니다.
        이것은 내용입니다.이것은 내용입니다.
      </div>
    </div>

    <hr id="reply_box_bot_line"/>

    <div class="detail_bot_button_box">
      <!--TODO: 목록으로 돌아갈때 쿼리파라미터 첨부해서 돌아가게 하기-->
      <button id="list_btn">목록</button>
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
  margin-bottom: 20px;
  margin-right: 20px;

  text-align: right;
}

.detail_content_box {
  border: 1px solid black;
  padding: 15px;
  margin-bottom: 30px;
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

</style>
