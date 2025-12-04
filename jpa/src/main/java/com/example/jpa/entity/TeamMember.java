package com.example.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@Getter
@Entity
public class TeamMember {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false) // true : null 허용하는 조인 컬럼 false : null 허용 X
    @JoinColumn(name = "team_id") // 원하는 조인 컬럼명 설정
    // private Team team ; => 오라클 테이블명_기본키명(team_team_id으로 나옴)
    private Team team;

}
