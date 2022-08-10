package com.avs.book.service;

import com.avs.book.dao.Person;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PersonService {

    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static String format(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return DATE_FORMATTER.format(localDate);
    }

    public static LocalDate parse(String dateString) {
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static boolean validDate(String dateString) {
        return PersonService.parse(dateString) != null;
    }

    public static Person personBuilder(String personString) {
        String[] splitString = personString.split("#");
        LocalDate birthday = parse(splitString[6]);
        int postalCode = Integer.parseInt(splitString[7]);
        return new Person(splitString[0], splitString[1], splitString[2], splitString[3],
                splitString[4], splitString[5], birthday, postalCode);
    }

}
