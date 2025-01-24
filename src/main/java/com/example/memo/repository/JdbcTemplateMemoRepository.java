package com.example.memo.repository;

import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateMemoRepository implements MemoRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemoRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public MemoResponseDto saveMemo(Memo memo) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("memo").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("title", memo.getTitle());
        params.put("content", memo.getContent());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        return new MemoResponseDto(key.longValue(), memo.getTitle(), memo.getContent());
    }

    @Override
    public List<MemoResponseDto> findAllMemos() {

        return jdbcTemplate.query("select * from memo", memoRowMapper());
    }

    @Override
    public Optional<Memo> findMemoById(Long id) {
        List<Memo> result = jdbcTemplate.query("select * from memo where id = ?", memoRowMapperV2(), id);
        return result.stream().findAny();
    }

    @Override
    public Memo findMemoByIdOrOrElseThrow(Long id) {
        List<Memo> result = jdbcTemplate.query("select * from memo where id = ?", memoRowMapperV2(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists id = " + id));
    }

    @Override
    public int updateMemo(Long id, String title, String content) {
        return jdbcTemplate.update("update memo set title = ?, content = ? where id = ?", title, content, id);
    }

    @Override
    public int updateTitle(Long id, String title) {
        return jdbcTemplate.update("update memo set title = ? where id = ?", title, id);
    }


    @Override
    public int deleteMemo(Long id) {
        return jdbcTemplate.update("delete from memo where id = ?", id);
    }


    private RowMapper<MemoResponseDto> memoRowMapper() {
        return new RowMapper<MemoResponseDto>() {

            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new MemoResponseDto(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("content")
                );
            }
        };
    }


    private RowMapper<Memo> memoRowMapperV2() {
        return new RowMapper<Memo>() {

            @Override
            public Memo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Memo(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("content")
                );
            }
        };
    }

}
