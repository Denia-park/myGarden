<script setup>
import {router} from "@/scripts/router.js";
import {ref} from "vue";
import {signupApi} from "@/components/auth/signup/api/api.js";

/**
 * 회원가입 폼
 */
const form = ref({
  email: '',
  password: '',
  passwordCheck: '',
  emailValid: false,
  passwordValid: false,
  passwordCheckValid: false,
})

/**
 * 로그인 페이지로 이동
 */
function goToLogin() {
  router.push('/login');
}

/**
 * 회원가입 유효성 검사
 */
function validateSignup() {
  if (!form.value.emailValid) {
    alert('이메일를 확인해주세요.');
    return false;
  } else if (!form.value.passwordValid) {
    alert('비밀번호를 확인해주세요.');
    return false;
  } else if (!form.value.passwordCheckValid) {
    alert('비밀번호 확인에 제대로 입력하셨는지 확인해주세요.');
    return false;
  }

  return true;
}

/**
 * 회원가입
 */
function signup() {
  const {email, password} = form.value;

  if (!validateSignup()) {
    return;
  }

  signupApi(email, password)
      .then(responseMsg => {
        alert(responseMsg);

        if (responseMsg === '회원가입에 성공했습니다.') {
          router.push('/login');
        }
      })
}

/**
 * 이메일 유효성 검사
 */
function validateEmail() {
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
  form.value.emailValid = emailRegex.test(form.value.email);
}

/**
 * 비밀번호 유효성 검사
 */
function validatePassword() {
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@!%*#?&])[A-Za-z\d$@!%*#?&]{8,20}$/;
  form.value.passwordValid = passwordRegex.test(form.value.password);
}

/**
 * 비밀번호 확인 유효성 검사
 */
function validatePasswordCheck() {
  form.value.passwordCheckValid = form.value.password === form.value.passwordCheck;
}

</script>

<template>
  <div id="modalSignin" class="modal modal-sheet position-static d-block p-4 py-md-5" role="dialog"
       tabindex="-1">
    <div class="modal-dialog" role="document">
      <div class="modal-content rounded-4 shadow">
        <div class="modal-header p-5 pb-4 border-bottom-0">
          <h1 class="fw-bold mb-0 fs-2">회원가입</h1>
          <button aria-label="Close" class="btn-close" data-bs-dismiss="modal" type="button"
                  @click="goToLogin"></button>
        </div>

        <div class="modal-body p-5 pt-0">
          <div class="form-floating mb-3">
            <input v-model="form.email" class="form-control rounded-3" placeholder="name@example.com"
                   type="email" @input="validateEmail">
            <label>이메일 주소</label>
            <div v-if="!form.emailValid" class="text-danger validationText">
              유효한 이메일 형식을 입력해주세요.
            </div>
          </div>

          <div class="form-floating mb-3">
            <input v-model="form.password" class="form-control rounded-3" placeholder="Password"
                   type="password" @input="validatePassword">
            <label>비밀번호</label>
            <div v-if="!form.passwordValid" class="text-danger validationText">
              영문자, 숫자, 특수문자($, @, !, %, *, #, ?, &)가 각각 1개 이상 포함된 8~20자의 비밀번호여야 합니다.
            </div>
          </div>
          <div class="form-floating mb-3">
            <input v-model="form.passwordCheck" class="form-control rounded-3" placeholder="Password Check"
                   type="password" @input="validatePasswordCheck">
            <label>비밀번호 확인</label>
            <div v-if="!form.passwordCheckValid" class="text-danger validationText">
              위에서 패스워드로 입력하신 값이랑 일치하지 않습니다.
            </div>
          </div>
          <button class="w-100 mb-2 btn btn-lg rounded-3 btn-primary" type="submit" @click="signup">회원가입</button>
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

.validationText {
  font-size: 1rem;
}
</style>
