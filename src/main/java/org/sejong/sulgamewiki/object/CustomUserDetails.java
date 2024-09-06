package org.sejong.sulgamewiki.object;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import org.sejong.sulgamewiki.object.constants.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

  private final Member member;
  private Map<String, Object> attributes;

  public CustomUserDetails(Member member) {
    this.member = member;
  }

  public CustomUserDetails(Member member, Map<String, Object> attributes) {
    this.member = member;
    this.attributes = attributes;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(() -> member.getRole().name());
  }

  @Override
  public String getPassword() {
    return null;  // OAuth2 기반의 인증이라 비밀번호가 없음
  }

  @Override
  public String getUsername() {
    return String.valueOf(member.getMemberId());
  }

  @Override
  public boolean isAccountNonExpired() {
    // AccountStatus가 DELETED인 경우, 계정이 만료된 것으로 간주
    return member.getAccountStatus() != AccountStatus.DELETED;
  }

  @Override
  public boolean isAccountNonLocked() {
    // AccountStatus가 DELETED인 경우에만 계정을 잠긴 것으로 간주
    return member.getAccountStatus() != AccountStatus.DELETED;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;  // 인증 정보는 항상 유효함
  }

  @Override
  public boolean isEnabled() {
    // AccountStatus가 ACTIVE인 경우에만 계정이 활성화됨
    return member.getAccountStatus() != AccountStatus.DELETED;
  }

  @Override
  public String getName() {
    return member.getEmail();
  }
}