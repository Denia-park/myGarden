<script setup>

import {ref, watch} from "vue";

const props = defineProps({
  inputName: String,
  content: String
});

const content = ref(props.content);

const emit = defineEmits(['changeContent', 'submit'])

watch(() => props.content, (newValue) => {
  content.value = newValue;
});

</script>

<template>
  <div class="input-group">
    <p>{{ inputName }}</p>
    <textarea v-model="content" placeholder="일과를 입력하세요"
              @input="(e) => emit('changeContent', e.target.value)"
              @keyup.enter.ctrl="() => emit('submit', '')"/>
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
