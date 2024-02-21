package com.example.combine.admin.user.controller;

import com.example.combine.admin.user.service.UserService;
import com.example.combine.admin.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST, path = "/joinUser")
    public ResponseEntity<String> joinUser(@RequestBody(required = true) UserVo userVo) {
        String result = userService.addUser(userVo);

        if (result == null) {
            ResponseEntity.badRequest();
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody(required = true) UserVo userVo) {
        Map<String, Object> map = new HashMap<>();
        String resp = userService.authenticate(userVo);
        if (resp == null) return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok(resp);
    }
}
