<script setup>
import {store} from "@/scripts/store.js";
import {router} from "@/scripts/router.js";
import {computed} from "vue";
import {logoutApi} from "@/components/auth/logout/api/api.js";

function useRoute(route) {
  closeHeader();

  switch (route) {
    case 'login':
      router.push('/login');
      break;
    case 'dailyRoutine':
      router.push('/daily-routine');
      break;
    case 'noticeBoard':
      router.push('/boards/notice');
      break;
    case 'learnBoard':
      router.push('/boards/learn');
      break;
    case 'logout':
      logout();
      break;
    default:
      alert('잘못된 접근입니다.');
      break;
  }
}

function logout() {
  logoutApi()
      .then((msg) => {
        if (msg === null) {
          alert('로그아웃에 실패했습니다.');
          return;
        }

        alert('로그아웃 되었습니다.');
        goToHome();
      });
}

function goToHome() {
  closeHeader();
  router.push(homeUrl.value);
}

const homeUrl = computed(() => {
  return store.getters.getAccessToken ? '/daily-routine' : '/login';
});

const closeHeader = () => {
  const navbarHeader = document.getElementById('navbarHeader');

  navbarHeader.classList.remove('show');
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
                <a v-if="!store.state.account.accessToken" class="text-white" href="#" @click="useRoute('login')">
                  로그인
                </a>
                <a v-else class="text-white" href="#" @click="useRoute('logout')">
                  로그아웃
                </a>
              </li>
              <li>
                <a class="text-white" href="#" @click="useRoute('noticeBoard')">공지사항</a>
              </li>
              <li>
                <a class="text-white" href="#" @click="useRoute('dailyRoutine')">하루 일과</a>
              </li>
              <li>
                <a class="text-white" href="#" @click="useRoute('learnBoard')">TIL</a>
              </li>
            </ul>
          </div>
          <div class="col-sm-4 offset-md-1 py-4">
            <h4 class="text-white">Contact</h4>
            <ul class="list-unstyled">
              <li><a class="text-white" href="https://github.com/Denia-park" target='_blank'>Github</a></li>
              <li><a class="text-white" href="https://velog.io/@as9587" target='_blank'>Blog</a></li>
              <li><a class="text-white" href="mailto: phg9587@naver.com" target='_blank'>Email</a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="navbar navbar-dark bg-dark shadow-sm">
      <div class="container">
        <a class="navbar-brand d-flex align-items-center" href="#" @click="goToHome">
          <i class="fa-brands fa-pagelines"></i>
          <strong class="title">myGarden</strong>
        </a>
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

