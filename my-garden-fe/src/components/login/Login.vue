<script setup>

</script>

<template>
  <div class="form-sign-in w-100 m-auto">
    <form>
      <h1 class="h3 mb-3 fw-normal">Please sign in</h1>

      <div class="form-floating">
        <input id="floatingInput" class="form-control" placeholder="name@example.com" type="email">
        <label for="floatingInput">Email address</label>
      </div>
      <div class="form-floating">
        <input id="floatingPassword" class="form-control" placeholder="Password" type="password">
        <label for="floatingPassword">Password</label>
      </div>

      <div class="form-check text-start my-3">
        <input id="flexCheckDefault" class="form-check-input" type="checkbox" value="remember-me">
        <label class="form-check-label" for="flexCheckDefault">
          Remember me
        </label>
      </div>
      <button class="btn btn-primary w-100 py-2" type="submit">Sign in</button>
      <p class="mt-5 mb-3 text-body-secondary">&copy; 2017–2023</p>
    </form>
  </div>
</template>

<style scoped>
.form-sign-in {
  max-width: 330px;
  padding: 1rem;
}

.form-sign-in .form-floating:focus-within {
  z-index: 2;
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
</style>
