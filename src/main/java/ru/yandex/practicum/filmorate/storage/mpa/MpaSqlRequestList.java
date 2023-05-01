package ru.yandex.practicum.filmorate.storage.mpa;

public class MpaSqlRequestList {
    public static final String FIND_ALL_MPA = "SELECT * FROM mpa ORDER BY mpa_id";

    public static final String GET_MPA = "SELECT * FROM mpa WHERE mpa_id = ?";

    public static final String MPA_CHECK_IN_DB = "SELECT exists (SELECT * FROM mpa WHERE mpa_id = ?)";
}