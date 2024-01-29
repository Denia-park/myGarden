<script setup>

const props = defineProps({
  pageInfo: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(["page-change"]);

</script>

<template>
  <div class="align-center page-div">
    <nav aria-label="Page navigation example">
      <ul class="pagination">
        <li class="page-item">
          <a aria-label="Previous" class="page-link" @click="() => emit('page-change', 1)">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li class="page-item">
          <a aria-label="Previous" class="page-link">
            <span aria-hidden="true">&lt;</span>
          </a>
        </li>
        <li v-for="page in pageInfo.totalPages" class="page-item">
          <a :id="page === pageInfo.currentPage ? 'currentPage' : ''" class="page-link"
             @click="() => emit('page-change', page)">
            {{ page }}
          </a>
        </li>
        <li class="page-item">
          <a aria-label="Next" class="page-link">
            <span aria-hidden="true">></span>
          </a>
        </li>
        <li class="page-item">
          <a aria-label="Next" class="page-link" @click="() => emit('page-change', props.pageInfo.totalPages)">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </div>
</template>

<style scoped>
.align-center {
  display: flex;
  justify-content: center;
}

.page-item {
  text-align: center;
}

.page-div a {
  color: black;
  cursor: pointer;
}

#currentPage {
  font-weight: bold;
  color: red;
}

</style>
