
// axios 异常处理
function handleAxiosError(error) {
    if (error.response) {
        console.error('Error response:', error.response.data);
        alert(`Error: ${error.response.data.err || 'An error occurred'}`);
    } else if (error.request) {
        console.error('No response:', error.request);
        alert('Error: No response from the server');
    } else {
        console.error('Error message:', error.message);
        alert(`Error: ${error.message}`);
    }
}

// 接口 获取文档列表（未删除）
async function getAllDocs() {
    try {
        const response = await axios.get('/docs');
        return response.data.data;
    } catch (error) {
        handleAxiosError(error);
    }
}

// 接口 搜索文档
async function searchDocs(keywords) {
    try {
        const response = await axios.get(`/docs/search/${keywords}`);
        return response.data.data;
    } catch (error) {
        handleAxiosError(error);
    }
}

// 接口 获取指定文档内容
async function getDocById(id) {
    try {
        const response = await axios.get(`/docs/${id}`);
        return response.data.data;
    } catch (error) {
        if (error.response && error.response.status === 404) {
            alert('Error: Document not found');
            return { err: 'not found' };
        } else {
            handleAxiosError(error);
        }
    }
}

// 接口 创建文档
async function createDoc(title, content) {
    try {
        const response = await axios.post('/docs', { title, content });
        return response.data;
    } catch (error) {
        handleAxiosError(error);
    }
}

// 接口 更新文档
async function updateDoc(id, title, content) {
    try {
        const response = await axios.put(`/docs/${id}`, { title, content });
        return response.data;
    } catch (error) {
        handleAxiosError(error);
    }
}

// 接口 删除文档
async function deleteDoc(id) {
    try {
        const response = await axios.delete(`/docs/${id}`);
        return response.data;
    } catch (error) {
        handleAxiosError(error);
    }
}
