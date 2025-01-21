import os
import sqlite3
from flask import Flask, request

app = Flask(__name__)
app.config['DATABASE'] = 'docs.db'

from pydantic import BaseModel, ValidationError, constr

class EditDocDTO(BaseModel):
    title: str = constr(min_length=1, max_length=120, strip_whitespace=True)
    content: str

def init_db():
    with app.app_context():
        db = sqlite3.connect(app.config['DATABASE'])
        with app.open_resource('DLMarkdownDocs.sql', mode='r') as f:
            db.cursor().executescript(f.read()).close()
        db.commit()
        db.close()

def get_db():
    db = sqlite3.connect(app.config['DATABASE'])
    db.row_factory = sqlite3.Row
    return db

@app.route('/')
def index():
    return app.send_static_file('index.html')

# 功能1 所有文档列表
@app.get('/docs')
def get_docs():
    sql = """
        select
            dm.id         as id,
            dm.created_at as created_at,
            dm.is_deleted as is_deleted,
            dd.updated_at as updated_at,
            dd.title      as title
        from
            docs_meta dm
        join (
            select
                id,
                max(id),
                meta_id,
                updated_at,
                title,
                content
            from
                docs_data
            group by meta_id
        ) as dd
        on
            dd.meta_id = dm.id
        where
            dm.is_deleted = 0
        order by dd.id desc;
    """
    docs = get_db().execute(sql).fetchall()
    data = [dict(doc) for doc in docs]
    return { 'data': data }

# 功能2 查看文档
@app.get('/docs/<id>')
def get_doc_by_id(id:int):
    sql = """
        select
            dm.id         as id,
            dm.created_at as created_at,
            dd.updated_at as updated_at,
            dd.title      as title,
            dd.content    as content
        from
            docs_meta dm
        join (
            select
                max(id),
                meta_id,
                updated_at,
                title,
                content
            from
                docs_data
            group by meta_id
        ) as dd on dd.meta_id = dm.id
        where
            dm.id = ?;
    """
    doc = get_db().execute(sql, (id,)).fetchone()
    print('doc:', doc)
    print('data:', dict(doc))
    return {
        'data': dict(doc)
    } if doc is not None else {
        'err': 'not found'
    }

# 功能3 搜索文档
@app.get('/docs/search/<keywords>')
def search_docs(keywords:str):
    '''
    1 根据id搜索（匹配）
    2 根据标题搜索（包含）
    3 根据内容搜索（包含）
    '''
    sql = """
        select
            dm.id         as id,
            dm.created_at as created_at,
            dd.updated_at as updated_at,
            dd.title      as title
        from
            docs_meta dm
        join (
            select
                max(id),
                meta_id,
                updated_at,
                title,
                content
            from
                docs_data
            group by meta_id
        ) as dd
        on
            dd.meta_id == dm.id
        where
            dm.is_deleted = 0
        and
            (
                dm.id = ?
            or
                dd.title like ?
            or
                dd.content like ?
            );
    """
    results = get_db().execute(sql,
        [keywords, f'%{keywords}%', f'%{keywords}%']).fetchall()
    data = [dict(result) for result in results]
    return { 'data': data }

# 功能4 创建文档
@app.post('/docs')
def create_doc():
    try:
        doc = EditDocDTO(**request.get_json())
        db = get_db()
        db.execute('INSERT INTO docs_meta (created_at) VALUES (datetime("now","localtime"))')
        meta_id = db.execute('SELECT last_insert_rowid()').fetchone()[0]
        db.execute('INSERT INTO docs_data (meta_id, title, content) VALUES (?, ?, ?)', (meta_id, doc.title, doc.content))
        db.commit()
        db.close()
        return { 'id': meta_id }, 201
    except ValidationError as e:
        return { 'err': e.errors() }, 400

# 功能5 编辑文档
@app.put('/docs/<id>')
def update_doc(id: int):
    try:
        doc = EditDocDTO(**request.get_json())
        sql = 'insert into docs_data(meta_id, title, content) values (?,?,?)'
        db = get_db()
        db.execute(sql, (id, doc.title, doc.content))
        db.commit()
        return { 'msg': 'ok' }, 200
    except ValidationError as e:
        return { 'err': e.errors() }, 400

# 功能6 删除文档
@app.delete('/docs/<id>')
def delete_doc(id: int):
    sql = 'update docs_meta set is_deleted = 1 where id = ?'
    db = get_db()
    db.execute(sql, (id,))
    db.commit()
    return {'msg': 'ok'}

if __name__ == '__main__':
    
    if not os.path.exists(app.config['DATABASE']):
        init_db()

    app.run(host='0.0.0.0', port='5000', debug=True)

'''
CREATE TABLE IF NOT EXISTS docs_meta (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_at TIMESTAMP DEFAULT (datetime('now','localtime')),
    is_deleted INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS docs_data (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    meta_id INTEGER NOT NULL,
    updated_at TIMESTAMP DEFAULT (datetime('now','localtime')),
    title TEXT,
    content TEXT,
    FOREIGN KEY (meta_id) REFERENCES docs_meta (id) ON DELETE CASCADE
);

INSERT INTO docs_meta (created_at) VALUES
(datetime('now','localtime')),
(datetime('now','localtime'));

INSERT INTO docs_data (meta_id, title, content) VALUES
(1, 'Title 1', 'Content 1'),
(1, 'Title 2', 'Content 2'),
(1, 'Title 3', 'Content 3'),
(2, 'Title 4', 'Content 4'),
(2, 'Title 5', 'Content 5');
'''
