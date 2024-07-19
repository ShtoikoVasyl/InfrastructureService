package edu.shtoiko.infrastructureservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "currency")
public class Currency {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(name = "code", nullable = false)
 private String code;

 @Column(name = "full_name", nullable = false)
 private String fullName;

 @Column(name="sign", unique = true)
 private String sign;
}
