<!-- Login.vue -->
<template>

    <h2>用户登录</h2>
    
    <router-link to="/">回到主页</router-link>
    <br>

    <label for="email">邮箱：</label>
    <br>
    <input id="email" v-model="form.Email" type="email" placeholder="请输入邮箱" required />
    <br>
    
    <label for="password">密码：</label>
    <br>
    <input id="password" v-model="form.Password" type="password" placeholder="请输入密码" required />
    <br>
    
    <button @click="handleLogin">登录</button>
    <router-link to="/register">注册</router-link>
    <br>

</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const form = ref({
    Email: '',
    Password: ''
})

const handleLogin = async () => {
    try {
        const { data } = await axios.post('/api/Auth/Login', form.value)

        if (data.err) {
            alert(data.err)
            return
        }

        // 存储用户信息
        localStorage.setItem('token', data.token)
        localStorage.setItem('userInfo', JSON.stringify(data.userInfo))

        alert('登录成功！')
        console.log("data:", data)

        // 导航逻辑
        router.go(-1) // 尝试返回上一页
    } catch (error) {
        alert(error.response?.data?.err || '登录失败，请检查网络连接')
    }
}
</script>

<style scoped>

</style>
