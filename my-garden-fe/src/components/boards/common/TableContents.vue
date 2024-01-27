<script setup>

import {ref, watch} from "vue";

const props = defineProps({
  tableContentPage: {
    type: Object,
    required: true,
  },
});

const curBoardNumStart = ref(0);

watch(() => props.tableContentPage, () => {
  const curPageElement = props.tableContentPage.content.length;
  const curPageBoardNumOffset = (props.tableContentPage.currentPage - 1) * props.tableContentPage.pageSize;
  curBoardNumStart.value = curPageBoardNumOffset + curPageElement;
});

function isWrittenIn7days(writtenAt) {
  const diffTime = Math.abs(new Date() - new Date(writtenAt));
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

  return diffDays <= 7;
}

</script>

<template>
  <table aria-describedby="contentsList" class="table table-hover text-align-center">
    <thead>
    <tr>
      <th scope="col" style="width: 10%;">번호</th>
      <th scope="col" style="width: 12%;">카테고리</th>
      <th scope="col" style="width: 45%;">제목</th>
      <th scope="col" style="width: 10%;">조회수</th>
      <th scope="col" style="width: 13%;">등록 일시</th>
      <th scope="col" style="width: 10%;">작성자</th>
    </tr>
    </thead>
    <tbody class="table-group-divider">
    <tr v-for="(notice,index) in tableContentPage.content" :key="notice.id">
      <th scope="row">{{ curBoardNumStart - index }}</th>
      <td>{{ notice.category }}</td>
      <td class="table-title">
        <!--TODO: 제목 클릭시 상세 페이지로 이동, Query Parameter 모두 가지고 이동-->
        <a class="title_link" href="#">
          {{ notice.title }}
        </a>
        <span v-if="isWrittenIn7days(notice.writtenAt)" class="new-board">NEW</span>
      </td>
      <td>{{ notice.views }}</td>
      <td>{{ notice.writtenAt.split(' ')[0] }}</td>
      <td>{{ notice.writer }}</td>
    </tr>
    </tbody>
  </table>
</template>

<style scoped>
.table-title {
  display: flex;
  justify-content: center;
  align-items: center;
}

.new-board {
  color: red;
  font-size: 10px;
  font-weight: bold;
  margin-left: 5px;
  text-decoration: red underline;
}

.text-align-center {
  text-align: center;
}

.title_link {
  color: black;
  text-decoration: none;
}
</style>
