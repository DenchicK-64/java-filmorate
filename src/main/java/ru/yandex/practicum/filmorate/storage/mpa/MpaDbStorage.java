package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    /*@Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }*/

    @Override
    public List<Mpa> findAllMpa() {
        return jdbcTemplate.query(MpaSqlRequestList.FIND_ALL_MPA, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa getMpa(int mpaId) {
        mpaCheckInDb(mpaId);
        return jdbcTemplate.queryForObject(MpaSqlRequestList.GET_MPA, (rs, rowNum) -> makeMpa(rs), mpaId);
    }

    private void mpaCheckInDb(int mpaId) {
        Boolean checkMpa = jdbcTemplate.queryForObject(MpaSqlRequestList.MPA_CHECK_IN_DB, Boolean.class, mpaId);
        if (Boolean.FALSE.equals(checkMpa)) {
            throw new NotFoundException("Рейтинг не найден в базе данных");
        }
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"),
                rs.getString("name"));
    }
}