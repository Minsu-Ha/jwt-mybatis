package com.example.combine.admin.user.dao;

import com.example.combine.admin.user.vo.UserVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserDao {
    public UserVo selectUser(SqlSession session, String userId) {
        return session.selectOne("UserMapper.selectUserById", userId);
    }

    public int insertUser(SqlSession session, UserVo userVo) {
        return session.insert("UserMapper.insertUser", userVo);
    }
    public int insertRole(SqlSession session, Map<String, Object> role) {
        return session.insert("UserMapper.insertRole", role);
    }

    public int updateUser(SqlSession session, UserVo userVo) {
        return session.update("UserMapper.updateUser", userVo);
    }

    public int deleteUser(SqlSession session, String userId) {
        return session.delete("UserMapper.deleteUser", userId);
    }
}
