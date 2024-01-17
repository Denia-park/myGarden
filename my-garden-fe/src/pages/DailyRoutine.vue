<script setup>
import PageTitle from "@/components/default/PageTitle.vue";
import DrawDailyRoutine from "@/components/dailyRoutine/draw/DrawDailyRoutine.vue";
import InputDailyRoutine from "@/components/dailyRoutine/input/InputDailyRoutine.vue";
import SelectDate from "@/components/dailyRoutine/popup/SelectDate.vue";
import {ref} from "vue";
import {getTodayDate} from "@/components/dailyRoutine/api/api.js";

const updateBlock = ref({});
const updateDate = ref(getTodayDate());

</script>
<template>
  <div id="header">
    <PageTitle :input-name="'하루 일과'"/>
    <SelectDate @update-date="date => updateDate = date"/>
  </div>
  <div id="wrapper">
    <InputDailyRoutine :update-block="updateBlock"/>
    <DrawDailyRoutine :update-date="updateDate" @update-block="(block) => updateBlock = block"/>
  </div>
</template>

<style scoped>
#header {
  max-width: 1216px;
  margin: auto;
  position: relative;
  text-align: center;
}

#wrapper {
  max-width: 1280px;
  margin: 0 auto;
  font-weight: normal;

  display: grid;
  grid-template-columns: 1fr 1fr;
  padding: 0 2rem;
}

</style>
