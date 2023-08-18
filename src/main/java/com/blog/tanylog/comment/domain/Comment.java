package com.blog.tanylog.comment.domain;

import com.blog.tanylog.common.BaseEntity;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE comments SET is_deleted = true WHERE id = ?")
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  @Column(nullable = false)
  private String content;

  private boolean isDeleted;

  private int replyDepth;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parentComment;

  @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Comment> childComments = new ArrayList<>();

  @Builder
  public Comment(String content, boolean isDeleted, int replyDepth) {
    this.content = content;
    this.isDeleted = isDeleted;
    this.replyDepth = replyDepth;
  }

  public boolean checkDepth() {
    if (this.replyDepth > 0) {
      return false;
    }

    return true;
  }

  public boolean checkUser(User loginUser) {
    return this.user.getOauthId().equals(loginUser.getOauthId());
  }

  public void updateComment(String updateContent) {
    this.content = updateContent;
  }

  public void addDepth() {
    this.replyDepth += 1;
  }

  public void addUser(User user) {
    this.user = user;
  }

  public void addPost(Post post) {
    this.post = post;
  }

  public void addRelationByComment(Comment parentComment) {
    this.parentComment = parentComment;
    parentComment.childComments.add(this);
  }
}
