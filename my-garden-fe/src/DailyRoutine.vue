<script setup>
import PageTitle from "@/PageTitle.vue";
import DrawDailyRoutine from "@/DrawDailyRoutine.vue";
import InputDailyRoutine from "@/InputDailyRoutine.vue";
</script>
<template>
  <PageTitle :input-name="'하루 일과 기록'"/>
  <div id="wrapper">
    <InputDailyRoutine/>
    <DrawDailyRoutine/>
  </div>
</template>

<style scoped>

#wrapper {
  max-width: 1280px;
  margin: 0 auto;
  font-weight: normal;

  display: grid;
  grid-template-columns: 1fr 1fr;
  padding: 0 2rem;
}

</style>
