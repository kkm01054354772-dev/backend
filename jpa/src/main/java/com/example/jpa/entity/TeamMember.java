package com.example.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = "team") // 연관관계가 있을 때는 순환참조에 걸릴 수 있어서 toString에서 제외시킴
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class TeamMember {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    // @ManyToOne(optional = false)
    @JoinColumn(name = "team_id") // 원하는 조인 컬럼명 설정
    // private Team team ; => 오라클 테이블명_기본키명(team_team_id으로 나옴)
    private Team team;

    public void changeTeam(Team team) {
        this.team = team;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
