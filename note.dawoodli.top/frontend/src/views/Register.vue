<!-- Register.vue -->
<template>
    <h2>用户注册</h2>
    <router-link to="/">回到主页</router-link>
    <br>

    <label for="email">邮箱：</label>
    <br>
    <input id="email" v-model="form.Email" type="email" placeholder="请输入邮箱" required />
    <br>

    <label for="name">用户名：</label>
    <br>
    <input id="name" v-model="form.Name" type="text" placeholder="请输入用户名" required />
    <br>
    
    <label for="password">密码：</label>
    <br>
    <input id="password" v-model="form.Password" type="password" placeholder="请输入密码" required />
    <br>

    <label for="confirmPassword">确认密码：</label>
    <br>
    <input id="confirmPassword" v-model="form.ConfirmPassword" type="password" placeholder="请再次输入密码" required />
    <br>
    
    <button @click="handleRegister">注册</button>
    <router-link to="/login">登录</router-link>
    <br>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const form = ref({
    Email: '',
    Password: '',
    ConfirmPassword: '',
    Name: '',
})

const handleRegister = async () => {
    try {
        if (form.value.Password !== form.value.ConfirmPassword) {
            alert('两次输入的密码不一致')
            return
        }

        const { data } = await axios.post('/api/Auth/Register', {
            Email: form.value.Email,
            Password: form.value.Password,
            Name: form.value.Name,
        })

        if (data.err) {
            alert(data.err)
            return
        }

        // 存储用户信息
        localStorage.setItem('token', data.token)
        localStorage.setItem('userInfo', JSON.stringify(data.userInfo))

        alert('注册成功')
        router.push('/home')
    } catch (error) {
        alert(error.response?.data?.err || '注册失败，请检查网络连接')
    }
}
</script>

<style scoped>

</style>