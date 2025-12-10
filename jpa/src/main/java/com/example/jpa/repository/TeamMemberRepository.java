package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.jpa.entity.Team;
import com.example.jpa.entity.TeamMember;
import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByTeam(Team team);

    @Query("select m,t from TeamMember m join m.team t where t=:team")
    List<Object[]> findByMemeberAndTeam(@Param("team") Team team);

    @Query("select m,t from TeamMember m join m.team t where t.id=:id")
    List<Object[]> findByMemeberAndTeam2(@Param("id") Long id);

    @Query("select m,t from TeamMember m left join m.team t")
    List<Object[]> findByMemeberAndTeam3();
}
