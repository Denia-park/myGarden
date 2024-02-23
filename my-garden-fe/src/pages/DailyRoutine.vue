<script setup>
import PageTitle from "@/components/default/PageTitle.vue";
import SelectDate from "@/components/dailyRoutine/popup/SelectDatePopUp.vue";
import LeftSide from "@/components/dailyRoutine/side/LeftSide.vue";
import RightSide from "@/components/dailyRoutine/side/RightSide.vue";
</script>
<template>
  <div id="header">
    <PageTitle :input-name="'하루 일과'"/>
    <SelectDate/>
  </div>
  <div id="wrapper">
    <LeftSide/>
    <RightSide/>
  </div>
</template>

<style scoped>
#header {
  max-width: 1280px;
  margin: auto;
  position: relative;
  text-align: center;
}

#wrapper {
  max-width: 1280px;
  margin: 0 auto;
  font-weight: normal;

  display: flex;
  flex-direction: row;
  justify-content: center;
  flex-wrap: wrap;
  padding: 0 2rem;
}

</style>
