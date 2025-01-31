<script setup>

</script>

<template>
<header>Header</header>

<nav>
  <router-link to="/home">Home</router-link>
  <router-link to="/login">Login</router-link>
  <router-link to="/register">Register</router-link>
  <router-link to="/note">Note</router-link>
  <router-link to="/chat">Chat</router-link>
</nav>

<router-view></router-view>
<footer>Footer</footer>
</template>

<style scoped>

</style>
