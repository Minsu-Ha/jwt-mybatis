package com.example.combine.admin.user.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Lombok Annotation
 *
 * @Data - setter / getter / toString / hashCode 등 자동 구현하고, 구현 Method가 있을 시 개발자가 구현한 Method로 사용
 * @NoArgsConstructor - Paramter가 없는 생성자 생성
 * @AllArgsConstructor - 모든 Parameter가 있는 생성자 생성
 * @Builder - Builder Pattern 으로 생성자 구현
 * <p>
 * 해당 Class는 기본적인 부분만 구현
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserVo implements UserDetails {

    private String userId;
    private String password;

    /**
     * 계정이 가진 Role을 리턴
     */
    private List<String> role;

    /**
     * @return 계정이 가지고 있는 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (this.role != null) {
            this.role.forEach(v -> authorities.add(new SimpleGrantedAuthority(v)));
        }
        return authorities;
    }

    /**
     * @return 계정 비밀번호
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * @return Security에서 사용하는 Username ( userId로 설정 )
     */
    @Override
    public String getUsername() {
        return this.userId;
    }

    /**
     * DB 상 Column 존재한다면 구현
     *
     * @return 계정의 만료 여부
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * DB 상 Column 존재한다면 구현
     *
     * @return 계정의 잠김 여부
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * DB 상 Column 존재한다면 구현
     *
     * @return 계정 비밀번호 만료 여부
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * DB 상 Column 존재한다면 구현
     *
     * @return 계정 활성화 여부
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
