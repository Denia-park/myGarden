<script setup>
import {router} from "@/scripts/router.js";

function goToLogin() {
  router.push('/login');
}

</script>

<template>
  <div id="modalSignin" class="modal modal-sheet position-static d-block p-4 py-md-5" role="dialog"
       tabindex="-1">
    <div class="modal-dialog" role="document">
      <div class="modal-content rounded-4 shadow">
        <div class="modal-header p-5 pb-4 border-bottom-0">
          <h1 class="fw-bold mb-0 fs-2">Sign up for free</h1>
          <button aria-label="Close" class="btn-close" data-bs-dismiss="modal" type="button"
                  @click="goToLogin"></button>
        </div>

        <div class="modal-body p-5 pt-0">
          <form class="">
            <div class="form-floating mb-3">
              <input id="floatingInput" class="form-control rounded-3" placeholder="name@example.com" type="email">
              <label for="floatingInput">Email address</label>
            </div>
            <div class="form-floating mb-3">
              <input id="floatingPassword" class="form-control rounded-3" placeholder="Password" type="password">
              <label for="floatingPassword">Password</label>
            </div>
            <button class="w-100 mb-2 btn btn-lg rounded-3 btn-primary" type="submit">Sign up</button>
            <small class="text-body-secondary">By clicking Sign up, you agree to the terms of use.</small>
            <hr class="my-4">
            <h2 class="fs-5 fw-bold mb-3">Or use a third-party</h2>
            <button class="w-100 py-2 mb-2 btn btn-outline-warning rounded-3" type="submit">
              <svg class="bi me-1" height="16" width="16">
                <use xlink:href="#kakao"/>
                <i class="fa-regular fa-comment"></i>
              </svg>
              Sign up with Kakao
            </button>
            <button class="w-100 py-2 mb-2 btn btn-outline-success rounded-3" type="submit">
              <svg class="bi me-1" height="16" width="16">
                <i class="fa-regular fa-n"></i>
              </svg>
              Sign up with Naver
            </button>
            <button class="w-100 py-2 mb-2 btn btn-outline-primary rounded-3" type="submit">
              <svg class="bi me-1" height="16" width="16">
                <i class="fa-brands fa-google"></i>
              </svg>
              Sign up with Google
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.fa-brands, .fa-regular {
  margin-right: 6px;
}
</style>
