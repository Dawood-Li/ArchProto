document.addEventListener('DOMContentLoaded', async () => {
    await loadDocs();
    document.getElementById('searchButton').addEventListener('click', searchDocsHandler);
    document.getElementById('resetButton').addEventListener('click', loadDocs);
    document.getElementById('newDocButton').addEventListener('click', newDocHandler);
    document.getElementById('saveButton').addEventListener('click', saveDocHandler);
    document.getElementById('docList').addEventListener('click', async (e) => {
        const target = e.target;
        if (target.classList.contains('doc-title') && !target.closest('.doc-delete')) {
            const docId = target.parentElement.querySelector('.doc-id').textContent;
            await loadDocContentHandler(docId);
        }
    });
});

async function loadDocs() {
    const docs = await getAllDocs();
    renderDocs(docs);
}

function renderDocs(docs) {
    const docList = document.getElementById('docList');
    docList.innerHTML = ''; // 清空现有的文档列表
    docs.forEach(doc => {
        const docItem = createDocItem(doc);
        docList.appendChild(docItem);
    });
}

function createDocItem(doc) {
    const docItem = document.createElement('div');
    docItem.className = 'doc-item';
    docItem.innerHTML = `
        <span class="doc-id">${doc.id}</span>
        <span class="doc-updated">${doc.updated_at}</span>
        <span class="doc-title">${doc.title}</span>
        <button class="doc-delete">X</button>
    `;
    docItem.querySelector('.doc-delete').addEventListener('click', deleteDocHandler.bind(null, doc.id));
    // 已经绑定了click事件，这里注释掉
    // docItem.querySelector('.doc-title').addEventListener('click', loadDocContentHandler.bind(null, doc.id));
    return docItem;
}

async function searchDocsHandler() {
    const keywords = document.getElementById('searchBox').value;
    const docs = await searchDocs(keywords);
    renderDocs(docs);
}

function newDocHandler() {
    window.currentDocId = null;
    document.getElementById('docTitle').value = '';
    document.getElementById('docContent').value = '';
}

async function saveDocHandler() {
    const title = document.getElementById('docTitle').value;
    const content = document.getElementById('docContent').value;

    if (title && content) {
        // 这里需要判断是新建还是更新，假设有一个全局变量currentDocId来存储当前编辑的文档ID
        let response;
        if (window.currentDocId) {
            response = await updateDoc(window.currentDocId, title, content);
        } else {
            response = await createDoc(title, content);
        }
        if (response && !response.err) {
            await loadDocs(); // 重新加载文档列表
            if (response.id) {
                currentDocId = response.id;
            }
            alert('文档保存成功');
        }
    } else {
        alert('标题和内容不能为空');
    }
}

async function deleteDocHandler(docId) {
    if (confirm('您确定要删除这个文档吗？这个操作是不可逆的！')) {
        await deleteDoc(docId);
        await loadDocs(); // 重新加载文档列表
    }
}

async function loadDocContentHandler(docId) {
    const doc = await getDocById(docId);
    console.log('load doc by id', docId, doc);
    console.log('title', doc.title);
    console.log('content', doc.content);
    if (!doc.err) {
        document.getElementById('docTitle').value = doc.title;
        document.getElementById('docContent').value = doc.content;
        window.currentDocId = doc.id; // 设置当前编辑的文档ID
    }
}


// 拦截ctrl + s 改为保存文档
document.addEventListener('keydown', (event) => {
    if ((event.ctrlKey || event.metaKey) && event.key === 's') {
        event.preventDefault(); // 阻止默认的保存网页行为
        saveDocHandler(); // 触发保存功能
    }
});

