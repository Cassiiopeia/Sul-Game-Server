package org.sejong.sulgamewiki.repository;

import java.util.List;
import java.util.Optional;
import org.sejong.sulgamewiki.object.BaseMedia;
import org.sejong.sulgamewiki.object.BasePost;
import org.sejong.sulgamewiki.object.CreationGame;
import org.sejong.sulgamewiki.object.Intro;
import org.sejong.sulgamewiki.object.OfficialGame;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BasePostRepository extends JpaRepository<BasePost, Long> {

  @Query("SELECT entity FROM CreationGame entity WHERE entity.isDeleted = false")
  Slice<CreationGame> findCreationGames(Pageable pageable);

  @Query("SELECT entity FROM Intro entity WHERE entity.isDeleted = false")
  Slice<Intro> findIntros(Pageable pageable);

  @Query("SELECT entity FROM OfficialGame entity WHERE entity.isDeleted = false")
  Slice<OfficialGame> findOfficialGames(Pageable pageable);

  /*
  금주 가장 핫했던 술게임
   */
  //금주의 점수를 기준으로 술게임(창작,공식)을 정렬해서 Slice로 가져오는 JPQL 쿼리(인트로 제외)
  @Query("SELECT entity FROM BasePost entity WHERE TYPE(entity) IN (OfficialGame, CreationGame) AND entity.isDeleted = false")
  Slice<BasePost> findGames(Pageable pageable);

  @Query("SELECT entity FROM BasePost entity WHERE entity.basePostId IN :ids AND entity.isDeleted = false")
  List<BasePost> findByBasePostIdIn(List<Long> ids);

  @Query("SELECT entity FROM BasePost entity WHERE entity.basePostId = :basePostId AND entity.isDeleted = false")
  Optional<BasePost> findByBasePostId(Long basePostId);

  @Query("SELECT entity FROM BaseMedia entity WHERE entity.basePost.basePostId = :basePostId AND entity.basePost.isDeleted = false")
  List<BaseMedia> findMediasByBasePostId(Long basePostId);

  @Query("SELECT entity FROM Intro entity WHERE entity.basePostId = :basePostId AND entity.isDeleted = false")
  Optional<Intro> findIntroByBasePostId(Long basePostId);

  @Query("SELECT entity FROM OfficialGame entity WHERE entity.basePostId = :basePostId AND entity.isDeleted = false")
  Optional<OfficialGame> findOfficialGameByBasePostId(Long basePostId);

  @Query("SELECT entity FROM CreationGame entity WHERE entity.basePostId = :basePostId AND entity.isDeleted = false")
  Optional<CreationGame> findCreationGameByBasePostId(Long basePostId);

}
