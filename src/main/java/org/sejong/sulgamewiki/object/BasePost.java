package org.sejong.sulgamewiki.object;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.sejong.sulgamewiki.object.constants.ScoreRule;
import org.sejong.sulgamewiki.object.constants.SourceType;
import org.sejong.sulgamewiki.util.exception.CustomException;
import org.sejong.sulgamewiki.util.exception.ErrorCode;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@DiscriminatorColumn(name = "dtype")
@Slf4j
public abstract class BasePost extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long basePostId;

  // 20
  @Column(length = 100)
  @Size(max = 20, message = "제목 20자 제한")
  private String title;

  // 20
  @Column(length = 100)
  @Size(max = 20, message = "소개 20자 제한")
  private String introduction;

  // 1000
  @Lob
  @Size(max = 1000, message = "설명 1000자 제한")
  private String description;

  @Builder.Default
  private int likes = 0;

  @Builder.Default
  @ElementCollection
  private Set<Long> likedMemberIds = new HashSet<>();

  @Builder.Default
  private int views = 0;

  @Builder.Default
  private int reportedCount = 0;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @Builder.Default
  private int dailyScore = 0;  // 하루마다 초기화

  @Builder.Default
  private int weeklyScore = 0;  // 매주 일요일마다 초기화

  @Builder.Default
  private int totalScore = 0;   // 초기화 x

  @Builder.Default
  private int commentCount = 0;

  private SourceType sourceType;

  // TODO: 썸네일 정해지면 ENUM타입 생성하기
  // 썸네일 아이콘 선택 필드
  @Column(length = 255)
  private String thumbnailIcon;


  // 내 정보 공개 여부 필드
  @Builder.Default
  private boolean isCreatorInfoPrivate = false; // 기본값은 공개


  public void postLike(Long memberId){
    if(this.likedMemberIds.contains(memberId)){
      cancelLike(memberId);
    } else if (!this.likedMemberIds.contains(memberId)) {
      upLike(memberId);
    }
  }

  public void cancelLike(Long memberId) {
    if(!this.likedMemberIds.contains(memberId)) {
      throw new CustomException(ErrorCode.NO_LIKE_TO_CANCEL);
    }
    if (likes > 0) {
      this.likes--;
      this.likedMemberIds.remove(memberId);
    } else if(likes <= 0) {
      throw new CustomException(ErrorCode.LIKE_CANNOT_BE_UNDER_ZERO);
    }
  }

  public void upLike(Long memberId) {
    if(this.likedMemberIds.contains(memberId)) {
      throw new CustomException(ErrorCode.ALREADY_LIKED);
    }
    likes++;
    this.likedMemberIds.add(memberId);
  }


  public void updateScore(ScoreRule scoreRule){
    increaseDailyScore(scoreRule.getPoint());
    increaseWeeklyScore(scoreRule.getPoint());

    log.info("[ 포인트 POINT ] 게시물 {}에 {}점 부여 (사유: {})", basePostId,
        scoreRule.getPoint(), scoreRule.getDescription());

  public void increaseCommentCount(){this.commentCount++;}

  public void decreaseCommentCount(){this.commentCount--;}

  // 데일리 점수 증가
  public void increaseDailyScore(int score) {
    this.dailyScore += score;
  }

  // 위클리 점수 증가
  public void increaseWeeklyScore(int score) {
    this.weeklyScore += score;
  }

  // 점수 초기화 로직
  public void resetDailyScore() {
    this.dailyScore = 0;
  }

  public void resetWeeklyScore() {
    this.weeklyScore = 0;
  }

  public static Boolean checkCreatorInfoIsPrivate(Boolean isCreatorInfoPrivate) {
    return Optional.ofNullable(isCreatorInfoPrivate).orElse(false);
  }
 }