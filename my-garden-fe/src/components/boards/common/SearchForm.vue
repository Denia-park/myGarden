<script setup>

import {ref} from "vue";

const props = defineProps({
  categories: {
    type: Array,
    required: true,
  },
  startDate: {
    type: String,
    required: true,
  },
  endDate: {
    type: String,
    required: true,
  },
});

const emits = defineEmits(["search"]);

const startDate = ref(props.startDate);
const endDate = ref(props.endDate);
const category = ref("");
const searchText = ref("");
const pageSize = ref(10);
const sort = ref("writtenAt");
const order = ref("desc");

function search() {
  emits("search", {
    startDate: startDate.value,
    endDate: endDate.value,
    category: category.value,
    searchText: searchText.value,
    pageSize: pageSize.value,
    sort: sort.value,
    order: order.value,
  });
}
</script>

<template>
  <div class="filter-wrapper">
    <div class="filter-top-wrapper">
      <div class="reg-date">
        <span id="regDateText">
            등록일
        </span>
        <input id="regStartDate" v-model="startDate" class="filter-height text-align-center" name="regStartDate"
               type="date">
        -
        <input id="regEndDate" v-model="endDate" class="filter-height text-align-center" name="regEndDate" type="date">
      </div>
      <div class="search">
        <select id="category" v-model="category" class="filter-height" name="searchCategory">
          <option v-for="category in categories" :key="category.value" :value="category.value">
            {{ category.text }}
          </option>
        </select>
        <input id="searchBox" v-model="searchText" class="filter-height" name="searchText"
               placeholder="검색어를 입력해주세요. (제목 + 내용)"
               type="text"
               @keyup.enter="search">
        <button class="button filter-height" type="submit" @click="search">검색</button>
      </div>
    </div>
    <div class="filter-bot-wrapper">
      <div class="page-size-form ">
        <select id="pageSize" v-model="pageSize" class="filter-height" name="pageSize" @change="search">
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
        <select id="sort" v-model="sort" class="filter-height" name="sort" @change="search">
          <option value="writtenAt">등록일시</option>
          <option value="category">분류</option>
          <option value="title">제목</option>
          <option value="views">조회수</option>
        </select>
        <select id="order" v-model="order" class="filter-height" name="order" @change="search">
          <option value="desc">내림차순</option>
          <option value="asc">오름차순</option>
        </select>
      </div>
    </div>
  </div>
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
