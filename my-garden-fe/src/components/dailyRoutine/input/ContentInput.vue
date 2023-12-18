<script setup>
import {ref} from "vue";

defineProps({
  inputName: String
});

const content = ref('');

const emit = defineEmits(['changeContent'])

function submit() {
  emit('changeContent', content);
}
</script>

<template>
  <div class="input-group">
    <p>{{ inputName }}</p>
    <textarea v-model="content" placeholder="일과를 입력하세요" @keyup.enter.ctrl="submit"/>

    <button class="btn btn-secondary" type="button" @click="submit">등록</button>
    <p>(※ Ctrl + Enter를 입력하셔도 등록이 됩니다.)</p>
  </div>
</template>

<style scoped>
p {
  text-align: center;
  font-size: 20px;
  margin-bottom: 10px;
}

textarea {
  width: 100%;
  padding: 10px;
  font-size: 1.2em;
  border: 1px solid #ccc;
  border-radius: 4px;
  height: 300px;

  margin-bottom: 10px;
}
</style>
