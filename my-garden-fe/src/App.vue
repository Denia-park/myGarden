<script setup>
import DefaultHeader from "@/components/default/DefaultHeader.vue";
import DefaultFooter from "@/components/default/DefaultFooter.vue";
import {store} from "@/scripts/store.js";

store.dispatch('initializeTokenFromSessionStorage');
</script>

<template>
  <DefaultHeader/>
  <RouterView/>
  <DefaultFooter/>
</template>

