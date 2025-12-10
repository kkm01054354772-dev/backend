package com.example.jpa.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.jpa.entity.Team;
import com.example.jpa.entity.TeamMember;

import jakarta.persistence.OneToMany;

@SpringBootTest
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Test
    public void insertTest() {
        // 팀 생성
        Team team = Team.builder().name("team5").build();
        teamRepository.save(team);
        // 팀원 추가
        TeamMember member = TeamMember.builder().name("홍길동").team(team).build();
        teamMemberRepository.save(member);

    }

    @Test
    public void insertTest2() {
        // 기존에 존재하는 team에 팀원 추가

        // Team team = Team.builder().id(1L).build();
        Team team = teamRepository.findById(3L).get();

        TeamMember member = TeamMember.builder().name("홍경민").team(team).build();
        teamMemberRepository.save(member);
    }

    @Test
    public void insertTest3() {
        // 팀 생성

        Team team = Team.builder().name("team3").build();
        teamRepository.save(team);

    }

    @Test
    public void readTest() {
        // 팀 정보 검색
        // Team team = Team.builder().id(1L).build(); // Team(id=1, name= null)
        Team team = teamRepository.findById(1L).get(); // // Team(id=1, name=team1)
        System.out.println(team);

        // 선수 검색 / 외래키가 적용된 테이블이기 때문에 join을 바로 해서 코드 실행(Fetchtype.LAZY 하면 join X)
        TeamMember member = teamMemberRepository.findById(1L).get();
        // TeamMember(id=1, name=홍길동)
        System.out.println(member);

        // 팀원 => 팀 조회
        // System.out.println("팀명 : " + member.getTeam().getName()); // 팀명 : team1

        // 팀 => 팀원 조회 (X)
    }

    @Test
    public void updateTest() {
        // 팀 이름 변경
        Team team = teamRepository.findById(1L).get();
        team.changeName("플라워");
        System.out.println(teamRepository.save(team));

        // 홍경민의 팀 변경
        TeamMember member = teamMemberRepository.findById(2L).get();
        member.changeTeam(Team.builder().id(2L).build());
        System.out.println(teamMemberRepository.save(member));

    }

    @Test
    public void deleteTest() {

        // 팀 삭제 시도
        // teamRepository.deleteById(1L); // DataIntegrityViolationException

        // 팀 변경
        // 팀 정보를 이용해 팀원 찾기
        List<TeamMember> result = teamMemberRepository.findByTeam(teamRepository.findById(1L).get());
        result.forEach(m -> {
            m.changeTeam(teamRepository.findById(2L).get());
            teamMemberRepository.save(m);
        });

        // 팀 삭제 재시도
        teamRepository.deleteById(1L);
    }

    @Test
    public void deleteTest2() {

        // 팀 삭제 시도
        // teamRepository.deleteById(1L); // DataIntegrityViolationException

        // 팀원 삭제
        // 팀 정보를 이용해 팀원 찾기
        List<TeamMember> result = teamMemberRepository.findByTeam(teamRepository.findById(4L).get());
        result.forEach(m -> {
            teamMemberRepository.delete(m);

        });

        // 팀 삭제 재시도
        teamRepository.deleteById(4L);
    }

    @Transactional
    // 팀 => 멤버 접근
    @Test
    public void readTest2() {

        Team team = teamRepository.findById(3L).get();

        // @OneToMany(mappedBy = "team")
        // private List<TeamMember> members = new ArrayList<>();
        // 팀 => 팀원 조회
        System.out.println(team); // select * from teamtbl where id = 3;
        System.out.println(team.getMembers()); // select * from team_member where
        // team_id = 3;

    }

    // 팀 => 멤버 접근
    @Test
    public void readTest3() {

        Team team = teamRepository.findById(3L).get();

        // @OneToMany(mappedBy = "team", fetch = Fetchtype.EAGER)
        // private List<TeamMember> members = new ArrayList<>();
        // 팀 => 팀원 조회 (left join)
        System.out.println(team);
        // System.out.println(team.getMembers());

    }

    @Transactional
    @Test
    public void readTest4() {

        TeamMember member = teamMemberRepository.findById(6L).get();
        System.out.println(member);
        System.out.println(member.getTeam());
    }

    // cascade 개념 적용
    @Test
    public void insertCascadeTest() {
        Team team = Team.builder().name("new").build();
        team.getMembers().add(TeamMember.builder().name("강감찬").team(team).build());

        teamRepository.save(team);
    }

    @Test
    public void removeCascadeTest() {

        teamRepository.deleteById(5L);
    }

    // orphanRemoval = true 적용
    @Commit
    @Transactional
    @Test
    public void removeOrphanTest() {
        Team team = teamRepository.findById(6L).get();
        team.getMembers().remove(0);
        teamRepository.save(team);
    }

    // dirty checking 적용
    @Commit
    @Transactional
    @Test
    public void updateCascadeTest() {

        Team team = teamRepository.findById(7L).get();
        team.changeName("sunflower");
        TeamMember teamMember = team.getMembers().get(0);
        teamMember.changeName("홍시루");

        // teamRepository.save(team);
    }

    // 팀 아이디를 이용해
    @Test
    public void testQuery() {
        Team team = teamRepository.findById(7L).get();
        List<Object[]> result = teamMemberRepository.findByMemeberAndTeam(team);
        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
            TeamMember member1 = (TeamMember) objects[0];
            Team team1 = (Team) objects[1];
            System.out.println(member1);
            System.out.println(team1);
        }
    }

    @Test
    public void testQuery2() {
        List<Object[]> result = teamMemberRepository.findByMemeberAndTeam2(7L);
        for (Object[] objects : result) {
            // System.out.println(Arrays.toString(objects));
            TeamMember member1 = (TeamMember) objects[0];
            Team team1 = (Team) objects[1];
            System.out.println(member1);
            System.out.println(team1);
        }
    }

    @Test
    public void testQuery3() {

        List<Object[]> result = teamMemberRepository.findByMemeberAndTeam3();
        for (Object[] objects : result) {
            // System.out.println(Arrays.toString(objects));
            TeamMember member1 = (TeamMember) objects[0];
            Team team1 = (Team) objects[1];
            System.out.println(member1);
            System.out.println(team1);
        }
    }
}
