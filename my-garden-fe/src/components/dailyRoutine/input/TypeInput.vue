<script setup>
defineProps({
  inputName: String
});

const emit = defineEmits(['changeType'])

</script>

<template>
  <p>{{ inputName }}</p>
  <select id="activity" @change="(e) => emit('changeType', e.target.value)">
    <option value="STUDY">공부</option>
    <option value="REST">휴식</option>
    <option value="ETC">기타</option>
    <option value="EXERCISE">운동</option>
    <option value="SLEEP">수면</option>
    <option value="MEAL">식사</option>
    <option value="GAME">게임</option>
  </select>
</template>

<style scoped>
p {
  text-align: left;
  font-size: 20px;
  margin-bottom: 10px;
}

label {
  font-size: 20px;
  margin-right: 10px;
  margin-bottom: 10px;
}

select {
  width: 100%;
  padding: 10px;
  font-size: 19px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 10px;
}
</style>
