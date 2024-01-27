<script setup>
import SearchForm from "@/components/boards/common/SearchForm.vue";
import PaginationForm from "@/components/boards/common/PaginationForm.vue";
import TableContents from "@/components/boards/common/TableContents.vue";
</script>

<template>
  <div class="wrapper">
    <h1>공지사항</h1>

    <!--TODO: 검색 조건 (날짜, 카테고리) 전달해야 함 -->
    <SearchForm/>

    <!--TODO: 전체 몇개의 글인지 표시-->
    <div id="total_content_num">
      <p>총 100개의 글이 있습니다.</p>
    </div>

    <!--TODO: 테이블 컨텐츠 전달해야 함 -->
    <TableContents/>

    <!--TODO: 페이지 정보 전달해야 함 -->
    <PaginationForm/>

    <!-- TODO: 내가 admin인 경우에만 `등록` 버튼 활성화 -->
    <button class="button filter-height align_left" onclick="location.href='post'" type="button">등록</button>
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

#total_content_num {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
}

.filter-height {
  height: 35px;
}

.button {
  width: 75px;
  background: black;
  color: white;
  border: none;
  border-radius: 5px;
}

.align_left {
  float: right;
  margin-right: 10px;
}
</style>
