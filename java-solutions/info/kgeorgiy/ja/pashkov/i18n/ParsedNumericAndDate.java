package info.kgeorgiy.ja.pashkov.i18n;

import java.util.Date;
import java.util.List;
import java.util.Objects;

record ParsedNumericAndDate(List<Double> numbers, List<Double> currencies, List<Date> dates) {
    ParsedNumericAndDate {
        Objects.requireNonNull(numbers);
        Objects.requireNonNull(currencies);
        Objects.requireNonNull(dates);
    }
}
