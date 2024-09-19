package org.sejong.sulgamewiki.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.sejong.sulgamewiki.object.Member;
import org.sejong.sulgamewiki.object.RankingHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingHistoryRepository extends JpaRepository<RankingHistory, Long> {
  Page<RankingHistory> findByMemberOrderByRecordDateDesc(Member member, Pageable pageable);
  Optional<RankingHistory> findByMemberAndRecordDate(Member member, LocalDate date);
  Page<RankingHistory> findTop100ByRecordDateOrderByRankAsc(LocalDate date, Pageable pageable);
}