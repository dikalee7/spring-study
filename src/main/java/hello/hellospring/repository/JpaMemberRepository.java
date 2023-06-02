package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return em.createQuery("select m from tb_member m where m.name = :name", Member.class)
                .setParameter("name", name).getResultStream().findAny() ;
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from tb_member m", Member.class).getResultList();
    }
}
