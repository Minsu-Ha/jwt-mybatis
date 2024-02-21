package com.example.combine.admin.user.dao;

import com.example.combine.admin.user.vo.UserVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class UserDao {
    public Optional<UserVo> selectUser(SqlSession session, String userId) {
        return session.selectOne("userMapper.selectUserById", userId);
    }

    public int insertUser(SqlSession session, UserVo userVo) {
        return session.insert("userMapper.insertUser", userVo);
    }
    public int insertRole(SqlSession session, Map<String, Object> role) {
        return session.insert("userMapper.insertRole", role);
    }

    public int updateUser(SqlSession session, UserVo userVo) {
        return session.update("userMapper.updateUser", userVo);
    }

    public int deleteUser(SqlSession session, String userId) {
        return session.delete("userMpper.deleteUser", userId);
    }
}
