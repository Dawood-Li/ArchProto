package top.dawoodli.DLMarkdownDocs;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@RestController
public class DlMarkdownDocsApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(DlMarkdownDocsApplication.class, args);
    }
	
    // 1. 获取所有文档列表
    @GetMapping("/docs")
    public Map<String, Object> getDocs() {
        String sql = """
        select 
            meta.ID as id,
            meta.CreatedAt as 'created_at',
            data.CreatedAt as 'updated_at',
            data.Title as title
        from NoteMetaData meta
        left join NoteData data on meta.NewestNoteDataID = data.ID
        where meta.DeletedAt is null
        order by data.ID desc;
        """;
        List<Map<String, Object>> data = SqlRunner.db().selectList(sql);
        return Collections.singletonMap("data", data);
    }

    // 2. 查看单个文档
    @GetMapping("/docs/{id}")
    public Map<String, Object> getDocById(@PathVariable Long id) {
        String sql = """
        select
            meta.ID as id,
            meta.CreatedAt as 'created_at',
            data.CreatedAt as 'updated_at',
            data.Title as title,
            data.Content as content
        from NoteMetaData meta
        join NoteData data on meta.NewestNoteDataID = data.ID
        where meta.ID = {0}
        and meta.DeletedAt is null
        """;
        Map<String, Object> doc = SqlRunner.db().selectOne(sql, id);
        if (doc == null) {
            return Collections.singletonMap("err", "not found");
        }
        return Collections.singletonMap("data", doc);
    }

    // 3. 搜索文档
    @GetMapping("/docs/search/{keywords}")
    public Map<String, Object> searchDocs(@PathVariable String keywords) {
        String sql = """
        select
            meta.ID as id,
            meta.CreatedAt as 'created_at',
            data.CreatedAt as 'updated_at',
            data.Title as title
        from NoteMetaData meta
        join NoteData data on meta.NewestNoteDataID = data.ID
        where meta.DeletedAt is null
        and (
            meta.ID = {0}
            or data.Title LIKE CONCAT('%', {0}, '%')
            or data.Content LIKE CONCAT('%', {0}, '%')
        )
        order by data.ID desc;
        """;
        List<Map<String, Object>> results = SqlRunner.db().selectList(sql, keywords);
        return Collections.singletonMap("data", results);
    }

    // 4. 创建文档
    @PostMapping("/docs")
    public Map<String, Object> createDoc(@RequestBody Map<String, String> body) {
        Long UserID = 1L;
        int IsPublic = 1;
        String Title = body.get("title");
        String Collectionsontent = body.get("content");

        String sql2 = """
        begin;
        set @UserID   = {0};
        set @IsPublic = {1};
        set @Title    = {2};
        set @Content  = {3};
        set @MetaID = 0;
        set @DataID = 0;

        insert into NoteMetaData(UserID, IsPublic) values (@UserID, @IsPublic);
        set @MetaID = last_insert_id();

        insert into NoteData(NoteMetaID, Title, Content) values (@MetaID, @Title, @Content);
        set @DataID = last_insert_id();

        update NoteMetaData set NewestNoteDataID = @DataID where ID = @MetaID;
        select @MetaID as id;
        commit;
        """;
        Object[] params = {UserID, IsPublic, Title, Collectionsontent};
        Map<String, Object> result = SqlRunner.db().selectOne(sql2, params);
        return result;
    }

    // 5. 更新文档
    @PutMapping("/docs/{id}")
    public Map<String, String> updateDoc(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String title = body.get("title");
        String content = body.get("content");
        String sql = "insert into NoteData(NoteMetaID, Title, Content) values ({0}, {1}, {2})";        
        SqlRunner.db().update(sql, id, title, content);
        return Collections.singletonMap("msg", "ok");
    }

    // 6. 删除文档
    @DeleteMapping("/docs/{id}")
    public Map<String, String> deleteDoc(@PathVariable Long id) {
        String sql = "update NoteMetaData set DeletedAt = NOW() where ID = {0}";
        SqlRunner.db().update(sql, id);
        return Collections.singletonMap("msg", "ok");
    }
}
