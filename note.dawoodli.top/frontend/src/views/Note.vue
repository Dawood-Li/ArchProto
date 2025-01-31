<template>
    <div class="note-list">
        <h1>公开笔记列表</h1>
        <table>
            <thead>
                <tr>
                    <th>标题</th>
                    <th>创建时间</th>
                    <th>更新时间</th>
                </tr>
            </thead>
            <tbody>
                <tr 
                    v-for="note in notes" 
                    :key="note.NoteID"
                    @click="viewNote(note.DataID)"
                    class="clickable-row"
                >
                    <td>{{ note.Title }}</td>
                    <td>{{ note.CreatedAt }}</td>
                    <td>{{ note.UpdatedAt }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const notes = ref([]);

const viewNote = (dataId) => {
    router.push(`/noteView/${dataId}`);
};

onMounted(async () => {
    try {
        const resp = await axios.post('/api/Notes/GetPublicList');
        if (resp.status === 200) {
            notes.value = resp.data.data;
        }
    } catch (error) {
        alert("获取笔记列表时出错: " + error.message);
        console.error("获取笔记列表时出错: ", error);
    }
});
</script>

<style scoped>
table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
}

th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
}

th {
    background-color: #f2f2f2;
}

.clickable-row {
    cursor: pointer;
}

.clickable-row:hover {
    background-color: #f5f5f5;
}
</style>