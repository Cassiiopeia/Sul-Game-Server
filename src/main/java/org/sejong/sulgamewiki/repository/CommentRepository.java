package org.sejong.sulgamewiki.repository;

import java.util.List;
import org.sejong.sulgamewiki.object.BasePost;
import org.sejong.sulgamewiki.object.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findByBasePost(BasePost basePost);
}
