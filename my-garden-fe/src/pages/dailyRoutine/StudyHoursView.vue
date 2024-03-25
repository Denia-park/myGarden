<script setup>
import PageTitle from "@/components/default/PageTitle.vue";
import {useRoute} from "vue-router";
import {urlSafeBase64ToEmail} from "@/components/util/util.js";
import {getTodayDate} from "@/components/dailyRoutine/api/util.js";
import {CalendarHeatmap} from "vue3-calendar-heatmap";
import {onMounted, ref} from "vue";
import {getStudyHoursExceptTodayApiWithoutLogin} from "@/components/dailyRoutine/api/api.js";

const route = useRoute();
const studyHours = ref([]);

/**
 * URL로부터 받은 멤버 이메일 (URL Safe Base64 인코딩) -> 공부 시간 조회시에 사용
 */
const urlSafeBase64MemberEmail = route.params.urlSafeBase64MemberEmail;
const memberEmail = urlSafeBase64ToEmail(urlSafeBase64MemberEmail);

/**
 * 공부 시간 조회
 */
function getStudyHours() {
  getStudyHoursExceptTodayApiWithoutLogin(urlSafeBase64MemberEmail)
      .then(data => {
        studyHours.value = data;
      })
      .catch(error => {
        console.error(error);
      });
}

onMounted(() => {
  getStudyHours();
});
</script>
<template>
  <div id="header">
    <PageTitle :input-name="'공부 시간 잔디 조회'"/>
    <h4>[조회 Email: <span>{{ memberEmail }}</span>]</h4>
  </div>
  <div id="wrapper">
    <h4>공부 시간 잔디</h4>
    <CalendarHeatmap :end-date="getTodayDate()" :values="studyHours" tooltip-unit="Hours"/>
  </div>
</template>

<style scoped>
#header {
  max-width: 1280px;
  margin: auto;
  position: relative;
  text-align: center;
}

#header span {
  font-weight: bold;
  background: yellow;
}

#wrapper {
  max-width: 1280px;
  margin: 60px auto;
  font-weight: normal;

  text-align: center;
}

</style>
