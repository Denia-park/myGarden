<script setup>
defineProps({
  inputName: String
});
</script>

<template>
  <h1>{{ inputName }}</h1>
</template>

<style scoped>

h1 {
  text-align: center;
  font-size: 60px;
  margin-top: 10px;
  margin-bottom: 20px;
}

</style>
