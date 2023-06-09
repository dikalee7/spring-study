package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class MemoryMemberRepository implements MemberRepository{
    private static Map<Long, Member> store = new ConcurrentHashMap<>();
    private static AtomicLong sequence = new AtomicLong();
    @Override
    public Member save(Member member) {
        long id = sequence.get();
        member.setId(id);
        store.put(member.getId(), member);
        sequence.set(id+1);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream().filter(member -> member.getName().equals(name)).findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
