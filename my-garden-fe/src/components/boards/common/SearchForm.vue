<script setup>

</script>

<template>
  <form action="" class="filter-wrapper" method="get">
    <div class="filter-top-wrapper">
      <div class="reg-date">
        <span id="regDateText">
            등록일
        </span>
        <!-- TODO: 등록일도 부모한테서 받아와야 함, 기본적으로 오늘로부터 한달내 조건 -->
        <input id="regStartDate" class="filter-height text-align-center" name="regStartDate" type="date">
        -
        <input id="regEndDate" class="filter-height text-align-center" name="regEndDate" type="date">
      </div>
      <div class="search">
        <!-- TODO: 카테고리는 부모한테서 받아와야 함 -->
        <select id="category" class="filter-height" name="searchCategory">
          <option value="all">전체 분류</option>
          <option value="title">제목</option>
          <option value="content">내용</option>
          <option value="titleContent">제목 + 내용</option>
        </select>
        <input id="searchBox" class="filter-height" name="searchText" placeholder="검색어를 입력해주세요. (제목 + 내용)"
               type="text">
        <button class="button filter-height" type="submit">검색
        </button>
      </div>
    </div>
    <div class="filter-bot-wrapper">
      <div class="page-size-form ">
        <select id="pageSize" class="filter-height" name="pageSize">
          <option value="10">10</option>
          <option value="20">20</option>
          <option value="30">30</option>
          <option value="40">40</option>
          <option value="50">50</option>
        </select>
        개씩 보기
      </div>
      <div class="page-sort-form">
        정렬
        <select id="sort" class="filter-height" name="sort">
          <option value="category">분류</option>
          <option value="title">제목</option>
          <option value="views">조회수</option>
        </select>
        <select id="order" class="filter-height" name="order">
          <option value="desc">내림차순</option>
          <option value="asc">오름차순</option>
        </select>
      </div>
    </div>
  </form>
</template>

<style scoped>
.filter-wrapper {
  height: 110px;
  margin: 0 10px;

  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.filter-top-wrapper {
  padding: 10px 10px;

  border: 1px solid;

  display: flex;
  justify-content: space-between;
}

.filter-bot-wrapper {
  padding: 10px 10px;

  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.filter-height {
  height: 35px;
}

.search {
  margin-left: 30px;
}

.button {
  width: 75px;
  background: dodgerblue;
  color: white;
  border: none;
  border-radius: 5px;
}

.text-align-center {
  text-align: center;
}

.search select {
  width: 150px;
  text-align: center;
}

.page-size-form select {
  width: 60px;
  text-align: center;
}

.page-sort-form {
  width: 260px;

  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}

.page-sort-form select {
  width: 100px;
  text-align: center;
}

#searchBox {
  margin: 0px 10px;
  width: 300px;
}

#regDateText {
  margin-right: 20px;
}

</style>
