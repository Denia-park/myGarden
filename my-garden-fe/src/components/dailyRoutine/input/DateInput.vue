<script setup>
defineProps({
  inputName: String
});

const emit = defineEmits(['changeDate'])

</script>

<template>
  <div class="input-group">
    <p>{{ inputName }}</p>
    <input type="datetime-local" @input="(e) => emit('changeDate', e.target.value)"/>
  </div>
</template>

<style scoped>

p {
  font-size: 20px;
  margin-bottom: 10px;
}

.input-group {
  margin-bottom: 10px;
}

input[type="datetime-local"] {
  width: 100%;
  padding: 10px;
  font-size: 19px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

</style>
