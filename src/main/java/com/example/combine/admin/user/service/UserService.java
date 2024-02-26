package com.example.combine.admin.user.service;

import com.example.combine.admin.security.token.service.JwtService;
import com.example.combine.admin.user.dao.UserDao;
import com.example.combine.admin.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;
    private final SqlSessionTemplate session;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager manager;
    private final JwtService jwtService;

    @Transactional(rollbackFor = Exception.class)
    public String addUser(UserVo userVo) {

        userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));

        int result = userDao.insertUser(session, userVo);
        if (userVo.getRole() != null) {
            userVo.getRole().forEach(value -> {
                int i = userDao.insertRole(session, new HashMap<>() {
                    @Override
                    public Object put(String key, Object value) {
                        return super.put(key, value);
                    }
                });
            });
        }


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
        log.info("Login Info For Service.authenticat(): id - {}, password(encode) - {}", userVo.getUserId(), passwordEncoder.encode(userVo.getPassword()));
        UserVo result = userDao.selectUser(session, userVo.getUserId());

        if (result == null) return null;
        log.info("Login Info In DB: {}", result.toString());

        log.info("isCorrect?: {}", passwordEncoder.matches(userVo.getPassword(), result.getPassword()));

        manager.authenticate(new UsernamePasswordAuthenticationToken(userVo.getUserId(), userVo.getPassword()));

        return passwordEncoder.matches(userVo.getPassword(), result.getPassword()) ? jwtService.generateToken(userVo) : null;
    }


}
