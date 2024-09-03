package org.sejong.sulgamewiki.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sejong.sulgamewiki.object.constants.IntroTag;
import org.sejong.sulgamewiki.object.constants.IntroType;
import org.sejong.sulgamewiki.object.constants.SourceType;
import org.sejong.sulgamewiki.object.constants.GameTag;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@ToString
public class BasePostCommand {
  private Long basePostId;
  private Long memberId;
  private SourceType sourceType;
  private BasePost basePost;
  private String thumbnailIcon;
  private Boolean isPrivate;

  // official, creation
  private String introduction;
  private String title;
  private String description;
  @Builder.Default
  private List<MultipartFile> gameMultipartFiles = new ArrayList<>();
  private List<String> imageUrls;
  private Set<GameTag> gameTags;

  // creation, intro
  private Long relatedOfficialGameId;

  // intro
  private String lyrics;
  private IntroType introType;
  private Set<IntroTag> introTags;
  @Builder.Default
  private List<MultipartFile> introMultipartFiles = new ArrayList<>();

}
