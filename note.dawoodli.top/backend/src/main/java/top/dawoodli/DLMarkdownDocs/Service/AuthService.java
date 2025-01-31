package top.dawoodli.DLMarkdownDocs.Service;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;

import org.apache.ibatis.exceptions.PersistenceException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

@Service
public class AuthService {

    public Long register(String email, String password, String name) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        String sql = """
        insert into Users (Email, Password, Salt, Name)
        values ({0}, {1}, {2}, {3});
        select last_insert_id() as ID;""";
        try {
            Object ID = SqlRunner.db().selectOne(sql, email, hashedPassword, salt, name).get("ID");
            return ((BigInteger) ID).longValue();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                return null; // 邮箱已存在
            }
            throw e;
        }
    }

    public Map<String, Object> login(String email, String password) {
        String sql = """
        select
            ID,
            Email,
            Password,
            Salt,
            Name,
            Role,
            date_format(CreatedAt, '%Y-%m-%dT%H:%i:%s') as CreatedAt,
            date_format(LastLoginAt, '%Y-%m-%dT%H:%i:%s') as LastLoginAt
        from Users
        where Email = {0}
        and DeletedAt is null;""";
        Map<String, Object> user = SqlRunner.db().selectOne(sql, email);
        if (user == null || !BCrypt.checkpw(password, (String) user.get("Password"))) {
            return null;
        }
        SqlRunner.db().update("update Users set LastLoginAt = NOW() where ID = {0};", user.get("ID"));
        user.remove("Password");
        user.remove("Salt");
        return user;
    }
}
