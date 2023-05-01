package ru.yandex.practicum.filmorate.InMemoryStorageTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@SpringBootTest
public class FilmControllerTest {

    @Autowired
    FilmController filmController;
    Film testFilm;
    Film testFilmTwo;

    @BeforeEach
    void setUp() {
        testFilm = new Film(1, "Some name", "Some description",
                LocalDate.of(2000, 1, 1), 100, new Mpa(1, "G"),
                new ArrayList<>(), new HashSet<>());
        testFilmTwo = new Film(2, "Some name2", "Some description2",
                LocalDate.of(1990, 1, 1), 90, new Mpa(3, "PG_13"),
                new ArrayList<>(), new HashSet<>());
    }

    @Test
    void createFilm() {
        filmController.create(testFilm);
        final Film testFilmCopy = testFilm;
        assertNotNull(testFilm);
        assertEquals(testFilmCopy, testFilm);
        assertEquals(1, filmController.findAll().size());
    }

    @Test
    void getAllFilms() {
        filmController.create(testFilm);
        filmController.create(testFilmTwo);
        final Collection<Film> testCollection = filmController.findAll();
        assertNotNull(testCollection);
        assertEquals(2, testCollection.size());
        assertEquals(testCollection, filmController.findAll());
    }

    @Test
    void shouldCreateFilmWithWrongName() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        filmController.create(new Film(1, " ", "Some description",
                                LocalDate.of(2000, 1, 1), 100, new Mpa(1, "G"),
                                new ArrayList<>(), new HashSet<>()));
                    }
                });
        assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldCreateFilmWithTooLongDescription() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        filmController.create(new Film(1, "Some name", "В октябре 1805 года русские войска занимали " +
                                "села и города эрцгерцогства Австрийского, и еще новые полки приходили из России, и, " +
                                "отягощая постоем жителей, располагались у крепости Браунау. В Браунау была главная " +
                                "квартира главнокомандующего Кутузова.\n" +
                                "11-го октября 1805 года один из только что пришедших к Браунау пехотных полков, ожидая" +
                                " смотра главнокомандующего, стоял в полумиле от города. Несмотря на нерусскую местность" +
                                " и обстановку: фруктовые сады, каменные ограды, черепичные крыши, горы, видневшиеся " +
                                "вдали, — на нерусский народ, с любопытством смотревший на солдат, — полк имел точно " +
                                "такой же вид, какой имел всякий русский полк, готовившийся к смотру где-нибудь в " +
                                "середине России.\n" +
                                "С вечера, на последнем переходе, был получен приказ, что главнокомандующий будет " +
                                "смотреть полк на походе. Хотя слова приказа и показались неясны полковому командиру и " +
                                "возник вопрос, как разуметь слова приказа: в походной форме или нет? — в совете " +
                                "батальонных командиров было решено представить полк в парадной форме на том основании, " +
                                "что всегда лучше перекланяться, чем недокланяться. И солдаты, после тридцативерстного " +
                                "перехода, не смыкали глаз, всю ночь чинились, чистились: адъютанты и ротные " +
                                "рассчитывали, отчисляли; и к утру полк, вместо растянутой беспорядочной толпы, " +
                                "какою он был накануне на последнем переходе, представлял стройную массу двух тысяч " +
                                "людей, из которых каждый знал свое место, свое дело, из которых на каждом каждая " +
                                "пуговка и ремешок были на своем месте и блестели чистотой. Не только наружное было " +
                                "исправно, но ежели бы угодно было главнокомандующему заглянуть под мундиры, то на " +
                                "каждом он увидел бы одинаково чистую рубаху и в каждом ранце нашел бы узаконенное " +
                                "число вещей, «шильце и мыльце», как говорят солдаты. Было только одно обстоятельство, " +
                                "насчет которого никто не мог быть спокоен. Это была обувь. Больше чем у половины людей " +
                                "сапоги были разбиты. Но недостаток этот происходил не от вины полкового командира," +
                                " так как, несмотря на неоднократные требования, ему не был отпущен товар от " +
                                "австрийского ведомства, а полк прошел тысячу верст.\n" +
                                "Полковой командир был пожилой, сангвинический, с седеющими бровями и бакенбардами " +
                                "генерал, плотный и широкий больше от груди к спине, чем от одного плеча к другому. " +
                                "На нем был новый, с иголочки, со слежавшимися складками, мундир и густые золотые " +
                                "эполеты, которые как будто не книзу, а кверху поднимали его тучные плечи. Полковой " +
                                "командир имел вид человека, счастливо совершающего одно из самых торжественных дел " +
                                "жизни. Он похаживал перед фронтом и, похаживая, подрагивал на каждом шагу, слегка " +
                                "изгибаясь спиною. Видно было, что полковой командир любуется своим полком, счастлив" +
                                " им и что все его силы душевные заняты только полком; но несмотря на то, его " +
                                "подрагивающая походка как будто говорила, что, кроме военных интересов, в душе его " +
                                "немалое место занимают и интересы общественного быта и женский пол.\n" +
                                "— Ну, батюшка Михайло Митрич, — обратился он к одному батальонному командиру " +
                                "(батальонный командир, улыбаясь, подался вперед; видно было, что они были счастливы)," +
                                " — досталось на орехи нынче ночью. Однако, кажется, ничего, полк не из дурных... А?\n" +
                                "Батальонный командир понял веселую иронию и засмеялся.\n" +
                                "— И на Царицыном Лугу с поля бы не прогнали.\n" +
                                "— Что? — сказал командир.\n" +
                                "В это время по дороге из города, по которой были расставлены махальные, показались " +
                                "два верховые. Это были адъютант и казак, ехавший сзади.\n" +
                                "Адъютант был прислан из главного штаба подтвердить полковому командиру то, что было " +
                                "сказано неясно во вчерашнем приказе, а именно то, что главнокомандующий желал видеть" +
                                " полк совершенно в том положении, в котором он шел — в шинелях, в чехлах и без всяких" +
                                " приготовлений.\n" +
                                "К Кутузову накануне прибыл член гофкригсрата из Вены, с предложениями и требованиями" +
                                " идти как можно скорее на соединение с армией эрцгерцога Фердинанда и Мака, и Кутузов," +
                                " не считавший выгодным это соединение, в числе прочих доказательств в пользу своего" +
                                " мнения намеревался показать австрийскому генералу то печальное положение, в котором" +
                                " приходили войска из России. С этой целью он и хотел выехать навстречу полку, так " +
                                "что, чем хуже было бы положение полка, тем приятнее было бы это главнокомандующему." +
                                " Хотя адъютант и не знал этих подробностей, однако он передал полковому командиру" +
                                " непременное требование главнокомандующего, чтобы люди были в шинелях и чехлах, и " +
                                "что в противном случае главнокомандующий будет недоволен.",
                                LocalDate.of(2000, 1, 1), 100, new Mpa(1, "G"),
                                new ArrayList<>(), new HashSet<>()));
                    }
                });
        assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void shouldCreateFilmWithWrongReleaseDate() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        filmController.create(new Film(1, "Some name", "Some description",
                                LocalDate.of(1000, 1, 1), 100, new Mpa(1, "G"),
                                new ArrayList<>(), new HashSet<>()));
                    }
                });
        assertEquals("Дата релиза — не раньше 28 декабря 1895 г.", exception.getMessage());
    }

    @Test
    void shouldCreateFilmWithWrongDuration() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        filmController.create(new Film(1, "Some name", "Some description",
                                LocalDate.of(2000, 1, 1), -100, new Mpa(1, "G"),
                                new ArrayList<>(), new HashSet<>()));
                    }
                });
        assertEquals("Продолжительность фильма должна быть больше 0", exception.getMessage());
    }

    @Test
    void shouldUpdateFilmWithWrongId() {
        final FilmNotFoundException exception = assertThrows(
                FilmNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        filmController.update(new Film(10, "Some name", "Some description",
                                LocalDate.of(2000, 1, 1), 100, new Mpa(1, "G"),
                                new ArrayList<>(), new HashSet<>()));
                    }
                });
        assertEquals("Фильм не найден в базе данных", exception.getMessage());
    }
}