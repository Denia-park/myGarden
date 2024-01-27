<script setup>

</script>

<template>
  <div class="align-center page-div">
    <nav aria-label="Page navigation example">
      <ul class="pagination">
        <li class="page-item">
          <a aria-label="Previous" class="page-link">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li class="page-item">
          <a aria-label="Previous" class="page-link">
            <span aria-hidden="true">&lt;</span>
          </a>
        </li>
        <li class="page-item">
          <a class="page-link"

          ></a>
        </li>
        <li class="page-item">
          <a aria-label="Next" class="page-link">
            <span aria-hidden="true">></span>
          </a>
        </li>
        <li class="page-item">
          <a aria-label="Next" class="page-link">
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
}

</style>
