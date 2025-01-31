<template>
    <div class="note-view">
        <h1>笔记详情</h1>
        <div v-if="note">
            <h2>{{ note.Title }}</h2>
            <p>笔记ID: {{ note.NoteID }}</p>
            <p>数据ID: {{ note.DataID }}</p>
            <p>用户: {{ note.UserName }} (ID: {{ note.UserID }})</p>
            <p>创建时间: {{ note.CreatedAt }}</p>
            <p>更新时间: {{ note.UpdatedAt }}</p>
            <div class="content">
                <h3>内容:</h3>
                <pre>{{ note.Content }}</pre>
            </div>
        </div>
        <div v-else>
            加载中...
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const note = ref(null);

onMounted(async () => {
    try {
        const response = await axios.post(`/api/Notes/GetDetail/${route.params.dataId}`);
        if (response.status === 200) {
            note.value = response.data.data;
        }
    } catch (error) {
        alert("获取笔记详情时出错: " + error.message);
        console.error("获取笔记详情时出错: ", error);
    }
});
</script>

<style scoped>
.content {
    margin-top: 20px;
    padding: 15px;
    border: 1px solid #ddd;
    border-radius: 4px;
    white-space: pre-wrap;
    word-wrap: break-word;
}

pre {
    white-space: pre-wrap;
    font-family: inherit;
}
</style>