<script setup>
import DefaultHeader from "@/components/default/DefaultHeader.vue";
import DefaultFooter from "@/components/default/DefaultFooter.vue";
import {store} from "@/scripts/store.js";

const id = sessionStorage.getItem('id');

if (id) {
  store.commit('setAccount', id);
}

</script>

<template>
  <DefaultHeader/>
  <RouterView/>
  <DefaultFooter/>
</template>

