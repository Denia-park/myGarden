<script setup>
import DefaultHeader from "@/components/default/DefaultHeader.vue";
import DefaultFooter from "@/components/default/DefaultFooter.vue";
import {useRoute} from "vue-router";
import {watch} from "vue";
import {checkApi} from "@/components/login/api/api.js";

const route = useRoute();

watch(() => route.path, () => {
      checkApi();
    }
);

</script>

<template>
  <DefaultHeader/>
  <RouterView/>
  <DefaultFooter/>
</template>

