<script setup>
defineProps({
  inputName: String
});
</script>

<template>
  <h2>{{ inputName }}</h2>
</template>

<style scoped>
h2 {
  text-align: center;
  font-size: 40px;
  margin-bottom: 20px;
}
</style>
