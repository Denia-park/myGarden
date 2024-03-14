<script setup>

import {ref, watch} from "vue";
import {convertCategoryCodeToText} from "./util/util.js";

const props = defineProps({
  tableImportantContentPage: {
    type: Array,
    required: false,
  },
  tableContentPage: {
    type: Object,
    required: true,
  },
  categories: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(['goToBoardView',]);

/**
 * 페이지 번호 오프셋
 */
const pageNumberOffset = ref(0);

/**
 * 7일 이내 작성된 글인지 확인
 *
 * @param writtenAt 작성일시
 * @returns {boolean} 7일 이내 작성된 글이면 true, 아니면 false
 */
function isWrittenIn7days(writtenAt) {
  const diffTime = Math.abs(new Date() - new Date(writtenAt));
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

  return diffDays <= 7;
}

watch(() => props.tableContentPage.currentPage, () => {
  pageNumberOffset.value = (props.tableContentPage.currentPage - 1) * props.tableContentPage.pageSize;
});

</script>

<template>
  <table aria-describedby="contentsList" class="table table-hover text-align-center">
    <thead>
    <tr>
      <th scope="col" style="width: 10%;">번호</th>
      <th scope="col" style="width: 12%;">분류</th>
      <th scope="col" style="width: 45%;">제목</th>
      <th scope="col" style="width: 10%;">조회수</th>
      <th scope="col" style="width: 13%;">등록 일시</th>
      <th scope="col" style="width: 10%;">작성자</th>
    </tr>
    </thead>
    <tbody class="table-group-divider">
    <tr v-for="(importantNotice) in tableImportantContentPage" id="important-notice" :key="importantNotice.id">
      <th scope="row"></th>
      <td>{{ convertCategoryCodeToText(props.categories, importantNotice.category) }}</td>
      <td>
        <div class="table-title">
          <a class="title_link important" href="#" @click="() => emit('goToBoardView', importantNotice.id)">
            {{ importantNotice.title }}
          </a>
          <span v-if="isWrittenIn7days(importantNotice.writtenAt)" class="new-board">NEW</span>
        </div>
      </td>
      <td>{{ importantNotice.views }}</td>
      <td>{{ importantNotice.writtenAt.split(' ')[0] }}</td>
      <td>{{ importantNotice.writer }}</td>
    </tr>
    <tr v-for="(notice,index) in tableContentPage.content" :key="notice.id">
      <th scope="row">{{ props.tableContentPage.totalElements - (index + pageNumberOffset) }}</th>
      <td>{{ convertCategoryCodeToText(props.categories, notice.category) }}</td>
      <td>
        <div class="table-title">
          <a class="title_link" href="#" @click="() => emit('goToBoardView', notice.id)">
            {{ notice.title }}
          </a>
          <span v-if="isWrittenIn7days(notice.writtenAt)" class="new-board">NEW</span>
        </div>
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

#important-notice {
  --bs-table-bg: #c4deca;
}
</style>
