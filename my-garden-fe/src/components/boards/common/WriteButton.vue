<script setup>
import {router} from "@/scripts/router.js";

const props = defineProps({
  isAccessAccount: {
    type: Boolean,
    required: true
  },
  writePageName: {
    type: String,
    required: true
  },
  queryParameter: {
    type: Object,
    required: true
  }
});

/**
 * 페이지 이동
 *
 * @param pageName 페이지 이름
 */
function goToPage(pageName) {
  router.push({
    name: pageName,
    query: props.queryParameter
  });
}
</script>

<template>
  <button v-if="isAccessAccount" class="button filter-height align_left" type="button"
          @click="() => goToPage(writePageName)">
    등록
  </button>
</template>

<style scoped>

.filter-height {
  height: 35px;
}

.button {
  width: 75px;
  background: dodgerblue;
  color: white;
  border: none;
  border-radius: 5px;
}

.align_left {
  float: right;
  margin-right: 10px;
}
</style>
