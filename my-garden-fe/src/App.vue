<script setup>
import {onMounted, ref} from 'vue';

const todo = ref('');
const startDate = ref('');
const endDate = ref('');
const logs = ref([]);

function addLog() {
  console.log(todo.value)
  console.log(startDate.value)
  console.log(endDate.value)
  if (todo.value.trim() && startDate.value && endDate.value) {
    logs.value.push({
      id: Date.now(),
      text: todo.value,
      start: startDate.value,
      end: endDate.value
    });
    todo.value = '';
    saveLogs();
  }
}

function loadLogs() {
  const savedLogs = localStorage.getItem('daily-logs');
  if (savedLogs) {
    console.log(JSON.parse(savedLogs));
    // logs.value = JSON.parse(savedLogs);
  }

}

function saveLogs() {
  localStorage.setItem('daily-logs', JSON.stringify(logs.value));
}

onMounted(() => {
  loadLogs();
});
</script>

<template>
  <h1>하루 일과 기록</h1>

  <div id="wrapper">
    <div class="container">
      <h1>기록</h1>
      <div class="input-group">
        <input v-model="startDate" type="datetime-local"/>
      </div>
      <div class="input-group">
        <input v-model="endDate" type="datetime-local"/>
      </div>
      <div class="input-group">
        <textarea v-model="todo" placeholder="일과를 입력하세요" @keyup.enter="addLog"/>
      </div>
      <ul>
        <li v-for="log in logs" :key="log.id">
          {{ log.start }} ~ {{ log.end }}: {{ log.text }}
        </li>
      </ul>
    </div>
    <div>
      <div class="container">
        <h1>
          여기는 그래프를 그립시다.
        </h1>
      </div>
    </div>
  </div>
</template>

<style scoped>

#wrapper {
  max-width: 1280px;
  margin: 0 auto;
  padding: 2rem;
  font-weight: normal;
}

#wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  padding: 0 2rem;
}

.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh; /* Full viewport height */
  text-align: center;
  max-width: 640px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  text-align: center;
  font-size: 60px;
  margin-bottom: 20px;
}

.input-group {
  margin-bottom: 10px;
}

input[type="datetime-local"] {
  width: 100%;
  padding: 10px;
  font-size: 1.2em;
  border: 1px solid #ccc;
  border-radius: 4px;
}

textarea {
  width: 100%;
  padding: 10px;
  font-size: 1.2em;
  border: 1px solid #ccc;
  border-radius: 4px;
  height: 300px;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  text-align: left;
  margin-top: 10px;
}
</style>
