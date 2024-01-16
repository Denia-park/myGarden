<script setup>
import {store} from "@/scripts/store.js";
import {router} from "@/scripts/router.js";

function logout() {
  store.commit('setAccount', 0);
  sessionStorage.removeItem('id');
  router.push('/login');
}
</script>

<template>
  <header>
    <div id="navbarHeader" class="collapse bg-dark">
      <div class="container">
        <div class="row">
          <div class="col-sm-8 col-md-7 py-4">
            <h4 class="text-white">Page</h4>
            <ul class="list-unstyled">
              <li>
                <router-link v-if="!$store.state.account.accessToken" class="text-white" to="/login">로그인</router-link>
                <a v-else class="text-white" href="#" @click="logout">로그아웃</a>
              </li>
              <li>
                <router-link class="text-white" to="/">하루 일과</router-link>
              </li>
              <li><a class="text-white" href="#">정리 게시판</a></li>
              <li><a class="text-white" href="#">하루 습관</a></li>
              <li><a class="text-white" href="#">명언 정리</a></li>
            </ul>
          </div>
          <div class="col-sm-4 offset-md-1 py-4">
            <h4 class="text-white">Contact</h4>
            <ul class="list-unstyled">
              <li><a class="text-white" href="https://github.com/Denia-park">Github</a></li>
              <li><a class="text-white" href="https://velog.io/@as9587">Blog</a></li>
              <li><a class="text-white" href="mailto: phg9587@naver.com">Email</a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="navbar navbar-dark bg-dark shadow-sm">
      <div class="container">
        <router-link class="navbar-brand d-flex align-items-center" to="/">
          <i class="fa-brands fa-pagelines"></i>
          <strong class="title">myGarden</strong>
        </router-link>
        <button aria-controls="navbarHeader" aria-expanded="false" aria-label="Toggle navigation" class="navbar-toggler"
                data-bs-target="#navbarHeader" data-bs-toggle="collapse" type="button">
          <span class="navbar-toggler-icon"></span>
        </button>
      </div>
    </div>
  </header>
</template>

<style scoped>
.title {
  margin-left: 5px;
}

a.text-white {
  text-decoration: none;
}
</style>

