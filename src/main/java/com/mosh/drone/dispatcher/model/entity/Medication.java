package com.mosh.drone.dispatcher.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Thu Mar, 2025
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Table(name = "medication")
@EqualsAndHashCode(callSuper = false)
@Where(clause = "deleted = false")
@SQLDelete(sql = "update medication set deleted = true where id=?")
public class Medication extends BaseEntity {

  @Column(nullable = false)
  private String name;

  private double weight;

  @Column(nullable = false, unique = true)
  private String code;

  private String image;
}
