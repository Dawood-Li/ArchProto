package top.dawoodli.DLMarkdownDocs.Service;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class NoteService2 {

    // 创建
    public Long create(Long UserID, int IsPublic, String Title, String Content) {
        String sql = """
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
        select @MetaID as ID;
        commit;
        """;
        return (Long)SqlRunner.db().selectOne(sql, UserID, IsPublic, Title, Content).get("ID");
    }

    // 更新
    public boolean update(Long NoteID, String Title, String Content) {
        return SqlRunner.db().update("""
        insert into NoteData(NoteMetaID, Title, Content)
        values ({0}, {1}, {2})""", NoteID, Title, Content);
    }

    // 删除
    public boolean delete(Long NoteID) {
        return SqlRunner.db().update("""
        update NoteMetaData set DeletedAt = now() 
        where ID = {0}""", NoteID);
    }
    
    // 恢复
    public boolean restore(Long NoteID) {
        return SqlRunner.db().update("""
        update NoteMetaData set DeletedAt = null 
        where ID = {0}""", NoteID);
    }
    
    // 公开
    public boolean show(Long noteID) {
        return SqlRunner.db().update("""
        update NoteMetaData
        set IsPublic = 1
        where ID = {0} and DeletedAt is null
        """, noteID);
    }

    // 隐藏
    public boolean hide(Long noteID) {
        return SqlRunner.db().update("""
        update NoteMetaData
        set IsPublic = 0
        where ID = {0} and DeletedAt is null
        """, noteID);
    }

    // 所有人公开笔记列表
    public List<Map<String, Object>> GetPublicList() {
        String sql = """
        select 
            meta.ID as NoteID,
            data.ID as DataID,
            date_format(meta.CreatedAt, '%Y-%m-%d %H:%i:%s') as CreatedAt,
            date_format(data.CreatedAt, '%Y-%m-%d %H:%i:%s') as UpdatedAt,
            data.Title as Title
        from NoteMetaData meta
        join NoteData data on meta.NewestNoteDataID = data.ID
        where
            meta.DeletedAt is null
            and meta.isPublic = 1
        order by data.ID desc;
        """;
        return SqlRunner.db().selectList(sql);
    }

    // 用户笔记列表 public / private / all
    public List<Map<String, Object>> GetUserList(Long UserID, String Type) {
        String condition = switch (Type.toLowerCase()) {
            case "public"  -> "and meta.IsPublic = 1";
            case "private" -> "and meta.IsPublic = 0";
            case "all"     -> "";
            default        -> "";
        };
        String sql = String.format("""
        select 
            meta.ID as NoteID,
            data.ID as DataID,
            date_format(meta.CreatedAt, '%Y-%m-%d %H:%i:%s') as CreatedAt,
            date_format(data.CreatedAt, '%Y-%m-%d %H:%i:%s') as UpdatedAt,
            data.Title as Title
        from NoteMetaData meta
        left join NoteData data on meta.NewestNoteDataID = data.ID
        where
            meta.UserID = {0}
            and meta.DeletedAt is null
            %s
        order by data.ID desc;
        """, condition);
        return SqlRunner.db().selectList(sql, UserID);
    }

    // 笔记详情
    public Map<String, Object> GetNoteNewestContent(Long DataID) {
        String sql = """
        select
            meta.ID as NoteID,
            data.ID as DataID,
            meta.UserID as UserID,
            user.Name as Username,
            date_format(meta.CreatedAt, '%Y-%m-%d %H:%i:%s') as CreatedAt,
            date_format(data.CreatedAt, '%Y-%m-%d %H:%i:%s') as UpdatedAt,
            data.Title as Title,
            data.Content as Content
        from NoteData data
        join NoteMetaData meta on data.NoteMetaID = meta.ID
        join Users user on meta.UserID = user.ID
        where data.ID = {0};
        """;
        return SqlRunner.db().selectOne(sql, DataID);
    }

    // 用户已删除笔记列表
    // public List<Map<String, Object>> GetUserDeletedList(Long UserID) {
    //     String sql = """
    //     select
    //         meta.ID as ID,
    //         date_format(meta.CreatedAt, '%Y-%m-%d %H:%i:%s') as CreatedAt,
    //         date_format(data.CreatedAt, '%Y-%m-%d %H:%i:%s') as UpdatedAt,
    //         data.Title as Title
    //     from NoteMetaData meta
    //     left join NoteData data on meta.NewestNoteDataID = data.ID
    //     where
    //         meta.UserID = {0}
    //         and meta.DeletedAt is not null
    //     order by data.ID desc;
    //     """;
    //     return SqlRunner.db().selectList(sql, UserID);
    // }

    // 用户笔记历史列表
    // public List<Map<String, Object>> getHistoryList(Long noteID) {
    //     String sql = """
    //     select 
    //         meta.ID as ID,
    //         date_format(meta.CreatedAt, '%Y-%m-%d %H:%i:%s') as CreatedAt,
    //         date_format(data.CreatedAt, '%Y-%m-%d %H:%i:%s') as UpdatedAt,
    //         data.Title as Title,
    //         left(data.content, 100) as contentPreview
    //     from NoteData data
    //     join NoteMetaData meta on data.noteMetaID = meta.id
    //     where meta.id = {0}
    //     order by data.createdAt desc;
    //     """;
    //     return SqlRunner.db().selectList(sql, noteID);
    // }
    


    // 笔记搜索
    // TODO: 改用elasticsearch实现搜索
    // public List<Map<String, Object>> search(String keywords, Long UserID) {
    //     return SqlRunner.db().selectList("""
    //     select 
    //         meta.ID as ID,
    //         date_format(meta.CreatedAt, '%Y-%m-%d %H:%i:%s') as CreatedAt,
    //         date_format(data.CreatedAt, '%Y-%m-%d %H:%i:%s') as UpdatedAt,
    //         data.Title as Title
    //     from NoteMetaData meta
    //     join NoteData data on meta.NewestNoteDataID = data.ID
    //     where meta.DeletedAt is null
    //       and (meta.UserID = {0} or meta.IsPublic = 1)
    //       and (
    //           cast(meta.ID as char) like concat('%%', {1}, '%%')
    //           or data.Title like concat('%%', {1}, '%%')
    //           or data.Content like concat('%%', {1}, '%%')
    //       )
    //     order by data.ID desc
    //     """, UserID, keywords);
    // }
}
