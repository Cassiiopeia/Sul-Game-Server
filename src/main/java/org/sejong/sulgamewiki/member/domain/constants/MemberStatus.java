package org.sejong.sulgamewiki.member.domain.constants;

import lombok.Getter;

@Getter
public enum MemberStatus {
  ACTIVE("활성화된 멤버"),
  INACTIVE("휴먼 멤버"),
  DELETED("계정 삭제된 멤버");

  private final String description;
  MemberStatus(String description) {
    this.description = description;
  }
}
