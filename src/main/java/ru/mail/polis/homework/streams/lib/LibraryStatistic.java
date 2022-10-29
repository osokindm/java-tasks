package ru.mail.polis.homework.streams.lib;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для работы со статистикой по библиотеке.
 * Оценка 5-ть баллов
 */
public class LibraryStatistic {

    /**
     * Вернуть "специалистов" в литературном жанре с кол-вом прочитанных страниц.
     * Специалист жанра считается пользователь который прочел как минимум 5 книг в этом жанре,
     * при этом читал каждую из них не менее 14 дней.
     *
     * @param library - данные библиотеки
     * @param genre   - жанр
     * @return - map пользователь / кол-во прочитанных страниц
     */
    public Map<User, Integer> specialistInGenre(Library library, Genre genre) {
        return library.getUsers().stream()
                .filter(user -> library.getArchive().stream().filter(archivedData -> archivedData.getBook().getGenre().equals(genre)
                        && archivedData.getUser().equals(user)
                        && enoughDays(archivedData, 14)).count() >= 5)
                .collect(Collectors.toMap(user -> user, User::getReadedPages));
    }

    private boolean enoughDays(ArchivedData archivedData, int days) {
        if (archivedData.getReturned() == null) {
            return System.currentTimeMillis() - archivedData.getTake().getTime() >= days * (1000 * 60 * 60 * 24);
        }
        return archivedData.getReturned().getTime() - archivedData.getTake().getTime() >= days * (1000 * 60 * 60 * 24);
    }

    /**
     * Вернуть любимый жанр пользователя. Тот что чаще всего встречается. Не учитывать тот что пользователь читает в данный момент.
     * Если есть несколько одинаковых по весам жанров - брать в расчет то, что пользователь читает в данный момент.
     *
     * @param library - данные библиотеки
     * @param user    - пользователь
     * @return - жанр
     */
    public Genre loveGenre(Library library, User user) {

        Map<Genre, Long> genreFrequency = library.getArchive().stream()
                .filter(archivedData -> archivedData.getUser().equals(user) && archivedData.getReturned() == null)
                .collect(Collectors.groupingBy(archivedData -> archivedData.getBook().getGenre(), Collectors.counting()));

        List<Genre> topGenres = genreFrequency.entrySet().stream()
                .filter(x -> x.getValue().equals(
                        genreFrequency.values().stream().max(Long::compareTo).get())) // maxFrequency
                .map(Map.Entry::getKey).collect(Collectors.toList());

        if (topGenres.contains(user.getBook().getGenre())) {
            return user.getBook().getGenre();
        }

        return topGenres.stream().findFirst().orElse(null);
    }

    /**
     * Вернуть список пользователей которые больше половины книг держали на руках более 30-ти дней. Брать в расчет и книги которые сейчас
     * пользователи держат у себя (ArchivedData.returned == null)
     *
     * @param library - данные библиотеки
     * @return - список ненадежных пользователей
     */
    public List<User> unreliableUsers(Library library) {

        return library.getUsers().stream().filter(
                        user -> library.getArchive().stream().filter(
                                archivedData -> archivedData.getUser().equals(user)
                                        && enoughDays(archivedData, 31)
                        ).count()
                                > library.getArchive().stream()
                                .filter(archivedData -> archivedData.getUser().equals(user)).count() / 2)
                .collect(Collectors.toList());
    }

    /**
     * Вернуть список книг у которых страниц равно или больше чем переданное значение
     *
     * @param library   - данные библиотеки
     * @param countPage - кол-во страниц
     * @return - список книг
     */
    public List<Book> booksWithMoreCountPages(Library library, int countPage) {

        return library.getBooks().stream()
                .filter(book -> book.getPage() > countPage)
                .collect(Collectors.toList());
    }

    /**
     * Вернуть самого популярного автора в каждом жанре. Если кол-во весов у авторов одинаково брать по алфавиту.
     *
     * @param library - данные библиотеки
     * @return - map жанр / самый популярный автор
     */
    public Map<Genre, String> mostPopularAuthorInGenre(Library library) {

        return library.getArchive().stream()
                .collect(Collectors.groupingBy(archivedData -> archivedData.getBook().getGenre(),
                        Collectors.groupingBy(archivedData -> archivedData.getBook().getAuthor(), Collectors.counting())))
                .entrySet().stream().collect(
                        Collectors.toMap(Map.Entry::getKey,
                                entry -> entry.getValue().entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey()));
    }
}
