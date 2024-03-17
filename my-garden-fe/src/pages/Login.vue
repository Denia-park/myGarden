<script setup>

import {onMounted, ref} from "vue";
import {loginApi} from "@/components/auth/login/api/api.js";
import {router} from "@/scripts/router.js";
import {store} from "@/scripts/store.js";

/**
 * 로그인 폼
 */
const form = ref({
  email: '',
  password: '',
})

/**
 * 아이디 및 패스워드 저장 여부
 */
const rememberMe = ref(false);

/**
 * 아이디 및 패스워드 저장 여부 설정
 *
 * @param email 이메일
 * @param password 패스워드
 */
function setRememberMe(email, password) {
  if (!rememberMe.value) {
    localStorage.removeItem("rememberMe");
    localStorage.removeItem("userAccount");
    return;
  }

  const userAccount = {
    email: email,
    password: password
  };

  localStorage.setItem("rememberMe", "true");
  localStorage.setItem("userAccount", JSON.stringify(userAccount));
}

/**
 * 로그인
 */
function submit() {
  const {email, password} = form.value;

  loginApi(email, password)
      .then(response => {
        if (response === null) {
          alert("로그인에 실패했습니다.");

          return;
        }
        alert("로그인에 성공했습니다.");

        setRememberMe(email, password);
        store.dispatch('initializeAuthenticationRoles', store);

        router.push('/daily-routine');
      })
}

/**
 * 회원가입 페이지로 이동
 */
function goToSignup() {
  router.push('/signup');
}

/**
 * 폼 초기화
 */
function initializeForm() {
  const savedRememberMe = localStorage.getItem("rememberMe");
  const userAccount = localStorage.getItem("userAccount");

  if (savedRememberMe === "true" && userAccount !== null) {
    rememberMe.value = true;
    const userAccountJson = JSON.parse(userAccount);

    form.value.email = userAccountJson.email;
    form.value.password = userAccountJson.password;
  }
}

onMounted(() => {
  initializeForm();
});

</script>

<template>
  <div id="modalSignin" class="modal modal-sheet position-static d-block p-4 py-md-5" role="dialog"
       tabindex="-1">
    <div class="modal-dialog" role="document">
      <div class="modal-content rounded-4 shadow">
        <div class="modal-header p-5 pb-4 border-bottom-0">
          <h1 class="fw-bold mb-0 fs-2">로그인</h1>
        </div>

        <div class="modal-body p-5 pt-0">
          <div class="form-floating">
            <input id="floatingInput" v-model="form.email" class="form-control" placeholder="name@example.com"
                   type="email">
            <label for="floatingInput">이메일 주소</label>
          </div>
          <div class="form-floating">
            <input id="floatingPassword" v-model="form.password" class="form-control" placeholder="Password"
                   type="password">
            <label for="floatingPassword">비밀번호</label>
          </div>

          <div class="form-check text-start my-3">
            <input id="flexCheckDefault" v-model="rememberMe" class="form-check-input" type="checkbox">
            <label class="form-check-label" for="flexCheckDefault">
              로그인 정보 저장
            </label>
          </div>
          <button class="btn btn-primary w-100 py-2" @click="submit">로그인</button>
          <button class="btn btn-success w-100 py-2" @click="goToSignup">회원가입</button>
        </div>
      </div>
    </div>
  </div>

</template>

<style scoped>
.modal-header h1 {
  width: 100%;
  text-align: center;
}

.form-sign-in input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}

.form-sign-in input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

button {
  margin-bottom: 1rem;
}
</style>
