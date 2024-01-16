<script setup>

import {ref} from "vue";
import {loginApi} from "@/components/auth/login/api/api.js";
import {router} from "@/scripts/router.js";


const form = ref({
  email: '',
  password: '',
})

function submit() {
  const {email, password} = form.value;

  loginApi(email, password)
      .then(response => {
        if (response === null) {
          return;
        }

        router.push('/daily-routine');
      })
}

function goToSignup() {
  router.push('/signup');
}

</script>

<template>
  <div id="modalSignin" class="modal modal-sheet position-static d-block p-4 py-md-5" role="dialog"
       tabindex="-1">
    <div class="modal-dialog" role="document">
      <div class="modal-content rounded-4 shadow">
        <div class="modal-header p-5 pb-4 border-bottom-0">
          <h1 class="fw-bold mb-0 fs-2">Please login</h1>
        </div>

        <div class="modal-body p-5 pt-0">
          <div class="form-floating">
            <input id="floatingInput" v-model="form.email" class="form-control" placeholder="name@example.com"
                   type="email">
            <label for="floatingInput">Email address</label>
          </div>
          <div class="form-floating">
            <input id="floatingPassword" v-model="form.password" class="form-control" placeholder="Password"
                   type="password">
            <label for="floatingPassword">Password</label>
          </div>

          <div class="form-check text-start my-3">
            <!--            <input id="flexCheckDefault" class="form-check-input" type="checkbox" value="remember-me">-->
            <!--            <label class="form-check-label" for="flexCheckDefault">-->
            <!--              Remember me-->
            <!--            </label>-->
          </div>
          <button class="btn btn-primary w-100 py-2" @click="submit">Login</button>
          <button class="btn btn-success w-100 py-2" @click="goToSignup">Sign up</button>
        </div>
      </div>
    </div>
  </div>

</template>

<style scoped>
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
