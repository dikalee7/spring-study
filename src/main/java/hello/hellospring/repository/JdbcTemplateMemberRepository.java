package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateMemberRepository implements MemberRepository{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("tb_member").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());
        parameters.put("created_on", new Timestamp(System.currentTimeMillis()).toString() );
        Number key = jdbcInsert.executeAndReturnKey(new
                MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
//        int result = jdbcTemplate.update("INSERT INTO tb_member (name, created_on, last_login) VALUES(?, current_timestamp, null)",
//                new Object[] { member.getName() });
//        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {

        try {
            Member member = jdbcTemplate.queryForObject("SELECT * FROM tb_member WHERE id=?",
                    BeanPropertyRowMapper.newInstance(Member.class), id);

            return Optional.of(member);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<Member> findByName(String name) {
        try {
            Member member = jdbcTemplate.queryForObject("SELECT * FROM tb_member WHERE name=?",
                    BeanPropertyRowMapper.newInstance(Member.class), name);

            return Optional.of(member);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("SELECT * from tb_member", BeanPropertyRowMapper.newInstance(Member.class));
    }

}
