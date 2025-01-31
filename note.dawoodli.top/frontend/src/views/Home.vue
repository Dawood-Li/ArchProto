<template>
    <h2>Home</h2>
    <div>
        <div v-if="userInfo">
            <!-- 展示当前用户信息 -->
            <p>欢迎, {{ userInfo.Name }}</p>
            <p>角色: {{ userInfo.Role }}</p>
            <p>邮箱: {{ userInfo.Email }}</p>
            <p>上次登录时间: {{ userInfo.LastLoginAt }}</p>
        </div>
        <div v-else>
            <p>还未登录</p>
            <router-link to="/login">登录</router-link>
        </div>
    </div>
</template>

<!-- <script setup> -->
<script setup>
import { ref, onMounted } from 'vue';

const userInfo = ref(JSON.parse(localStorage.getItem('userInfo')));
console.log("userInfo", userInfo.value)

// onMounted(() => {
// })
</script>

<style scoped></style>
