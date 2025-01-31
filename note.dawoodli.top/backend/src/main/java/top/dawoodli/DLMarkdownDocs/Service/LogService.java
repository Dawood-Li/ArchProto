package top.dawoodli.DLMarkdownDocs.Service;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    public boolean log(String level, String src, String msg) {
        String sql = """
        insert into Logs (Level, Src, Msg)
        values ({0}, {1}, {2})""";
        return SqlRunner.db().insert(sql, level, src, msg);
    }

    public boolean info(String src, String msg) {
        return log("info", src, msg);
    }

    public boolean warn(String src, String msg) {
        return log("warn", src, msg);
    }

    public boolean error(String src, String msg) {
        return log("error", src, msg);
    }

    public boolean debug(String src, String msg) {
        return log("debug", src, msg);
    }
    
    // public List<Map<String, Object>> fetchLogs(String level, String keyword) {
    //     return SqlRunner.db().selectList("""
    //         SELECT 
    //             ID as id,
    //             Level as level,
    //             Src as src,
    //             Msg as msg,
    //             CreatedAt as created_at
    //         FROM Logs
    //         WHERE (Level = {0} or {0} IS NULL)
    //         AND (
    //             (Msg LIKE CONCAT('%', {1}, '%') 
    //             or Src LIKE CONCAT('%', {1}, '%'))
    //             or {1} IS NULL
    //         )
    //         ORDER BY CreatedAt DESC""", level, keyword);
    // }


}
