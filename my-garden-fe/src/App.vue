<script setup>
import DefaultHeader from "@/components/default/DefaultHeader.vue";
import DefaultFooter from "@/components/default/DefaultFooter.vue";
import {store} from "@/scripts/store.js";
import setup from "@/scripts/axios-interceptors.js";

store.dispatch('initializeTokenFromSessionStorage');
setup();
</script>

<template>
  <DefaultHeader/>
  <RouterView/>
  <DefaultFooter/>
</template>

