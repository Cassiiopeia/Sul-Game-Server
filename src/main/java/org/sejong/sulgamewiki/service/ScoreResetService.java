package org.sejong.sulgamewiki.service;

import java.util.List;
import org.sejong.sulgamewiki.object.BasePost;
import org.sejong.sulgamewiki.repository.BasePostRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreResetService {

  private final BasePostRepository basePostRepository;

  // 매일자정마다 실행 (cron 표현식을 사용하여 스케줄링)
  @Scheduled(cron = "0 0 0 1/1 * ? *")
  @Async
  public void resetDailyScores() {
    List<BasePost> posts = basePostRepository.findAll();
    for (BasePost post : posts) {
      post.resetDailyScore();
    }
    basePostRepository.saveAll(posts);  // 실시간 점수 초기화 후 저장
  }

  // 일요일자정마다 실행 (매일 자정에 실행)
  @Scheduled(cron = "0 0 0 ? * SUN *")
  @Async
  public void resetWeeklyScores() {
    List<BasePost> posts = basePostRepository.findAll();
    for (BasePost post : posts) {
      post.resetWeeklyScore();
    }
    basePostRepository.saveAll(posts);  // 하루 점수 초기화 후 저장
  }
}

