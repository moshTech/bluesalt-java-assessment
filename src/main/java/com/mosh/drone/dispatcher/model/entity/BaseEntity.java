package com.mosh.drone.dispatcher.model.entity;

import com.mosh.drone.dispatcher.util.ULIDGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {

  @Id
  @GeneratedValue(generator = "ulid-generator")
  @GenericGenerator(name = "ulid-generator", type = ULIDGenerator.class)
  private String id;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  @Column(nullable = false)
  private Boolean deleted = false;

  @PrePersist
  protected void prePersist() {
    createdAt = OffsetDateTime.now();
  }

  @PreUpdate
  protected void preUpdate() {
    updatedAt = OffsetDateTime.now();
  }
}
