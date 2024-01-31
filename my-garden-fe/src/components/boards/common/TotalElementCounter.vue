<script setup>
const props = defineProps({
  totalElement: {
    type: Number,
    required: true
  }
});
</script>

<template>
  <div class="total-content-wrapper">
    총 <span> {{ totalElement }}</span>개의 글이 있습니다.
  </div>
</template>

<style scoped>
.total-content-wrapper {
  margin: 10px 0 10px 0;
}

.total-content-wrapper span {
  font-size: 15px;
  font-weight: bold;
}


</style>
