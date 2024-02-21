package com.example.combine.admin.user.service;

import com.example.combine.admin.security.token.service.JwtService;
import com.example.combine.admin.user.dao.UserDao;
import com.example.combine.admin.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final SqlSessionTemplate session;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager manager;
    private final JwtService jwtService;

    public String addUser(UserVo userVo) {

        userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));
        int result = userDao.insertUser(session, userVo);
        userVo.getRole().forEach(value-> {
            int i = userDao.insertRole(session, new HashMap<>(){
                @Override
                public Object put(String key, Object value) {
                    return super.put(key, value);
                }
            });
        });

        String jwt = jwtService.generateToken(userVo);

        if (result > 0) {
            return jwt;
        } else {
            return null;
        }
    }

    public UserVo modifyUser(UserVo userVo) {
        int result = userDao.updateUser(session, userVo);

        return result > 0 ? userVo : null;
    }

    public UserVo removeUser(UserVo userVo) {
        int result = userDao.deleteUser(session, userVo.getUserId());

        return result > 0 ? userVo : null;
    }

    public String authenticate(UserVo userVo) {
        UserVo result = userDao.selectUser(session, userVo.getUserId()).orElseThrow();
        if (result == null) return null;
        manager.authenticate(new UsernamePasswordAuthenticationToken(userVo.getUserId(), userVo.getPassword()));

        return passwordEncoder.matches(userVo.getPassword(), result.getPassword()) ? jwtService.generateToken(userVo) : null;
    }



}
