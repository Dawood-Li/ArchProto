import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import axios from 'axios';

axios.interceptors.request.use(config => {
    
    // 如果是调试模式
    // 将host修改为 http://localhost:8081/
    if (process.env.NODE_ENV === 'development') {
        config.baseURL = 'http://localhost:8081/';
    }

    // 登录注册请求直接直接放行
    if (config.url.includes('/api/Auth/')) {
        return config;
    }

    // 无token先登录
    const token = localStorage.getItem('token');
    if (!token) {
        router.push('/login');
        return Promise.reject(new Error('No token provided'));
    }
    
    // 有token携带token
    config.headers['Authorization'] = `Bearer ${token}`;
    return config;
}, error => {
    return Promise.reject(error);
});

axios.interceptors.response.use(
    response => response,
    error => {
        // 权限相关错误 重新登录
        if (error.response && error.response.status === 401) {
            alert(response.data.err);
            router.push('/login');
        }
        return Promise.reject(error);
    }
);

createApp(App).use(router).mount('#app')
