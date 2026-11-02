package info.kgeorgiy.ja.pashkov.i18n;

import java.text.BreakIterator;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

final class TextAnalyzer {
    private TextAnalyzer() {
    }

    static Report analyze(final String text, final Locale textLocale) {
        final List<String> sentences = extractSentences(text, textLocale);
        final List<String> words = extractWords(text, textLocale);

        final ParsedNumericAndDate parsed = parseNumbersCurrenciesAndDates(text, textLocale);
        return new Report(sentences, words, parsed.numbers(), parsed.currencies(), parsed.dates(), textLocale);
    }

    private static List<String> extractSentences(final String text, final Locale locale) {
        return extractByIterator(
                text,
                BreakIterator.getSentenceInstance(locale),
                token -> true
        );
    }

    private static List<String> extractWords(final String text, final Locale locale) {
        return extractByIterator(
                text,
                BreakIterator.getWordInstance(locale),
                token -> token.codePoints().anyMatch(Character::isLetter)
        );
    }

    private static List<String> extractByIterator(
            final String text,
            final BreakIterator iterator,
            final Predicate<String> tokenFilter
    ) {
        iterator.setText(text);

        final List<String> result = new ArrayList<>();
        int start = iterator.first();

        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            final String token = normalizeToken(text.substring(start, end));
            if (!token.isEmpty() && tokenFilter.test(token)) {
                result.add(token);
            }
        }

        return result;
    }

    private static ParsedNumericAndDate parseNumbersCurrenciesAndDates(final String text, final Locale locale) {
        final List<Date> dates = extractDates(text, locale);
        final List<Double> currencies = extractNumbers(text, NumberFormat.getCurrencyInstance(locale));
        final List<Double> numbers = extractNumbers(text, NumberFormat.getNumberInstance(locale));
        return new ParsedNumericAndDate(numbers, currencies, dates);
    }

    private static List<Double> extractNumbers(final String text, final NumberFormat format) {
        final List<Double> result = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            final ParsePosition position = new ParsePosition(i);
            final Number parsed = format.parse(text, position);
            final int end = position.getIndex();

            if (parsed != null
                    && end > i
                    && isBoundedToken(text, i, end)
                    && !isOrdinalNumericAbbreviation(text, i, end)) {
                result.add(parsed.doubleValue());
                i = end - 1;
            }
        }

        return result;
    }

    private static List<Date> extractDates(final String text, final Locale locale) {
        final List<DateFormat> formats = List.of(
                DateFormat.getDateInstance(DateFormat.SHORT, locale),
                DateFormat.getDateInstance(DateFormat.MEDIUM, locale),
                DateFormat.getDateInstance(DateFormat.LONG, locale),
                DateFormat.getDateInstance(DateFormat.FULL, locale)
        );
        formats.forEach(format -> format.setLenient(false));

        final List<Date> result = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            Date bestDate = null;
            int bestEnd = -1;

            for (final DateFormat format : formats) {
                final ParsePosition position = new ParsePosition(i);
                final Date parsed = format.parse(text, position);
                final int end = position.getIndex();
                if (parsed != null
                        && end > i
                        && end > bestEnd
                        && isBoundedToken(text, i, end)) {
                    bestDate = parsed;
                    bestEnd = end;
                }
            }

            if (bestDate != null) {
                result.add(new Date(bestDate.getTime()));
                i = bestEnd - 1;
            }
        }
        return result;
    }

    private static boolean isBoundedToken(final String text, final int start, final int endExclusive) {
        final boolean leftBounded = start == 0 || !Character.isLetterOrDigit(text.codePointBefore(start));
        final boolean rightBounded = endExclusive == text.length()
                || !Character.isLetterOrDigit(text.codePointAt(endExclusive));
        return leftBounded && rightBounded;
    }

    private static boolean isOrdinalNumericAbbreviation(final String text, final int start, final int endExclusive) {
        return hasHyphenAndLettersOnTheRight(text, endExclusive) || hasLetterAndHyphenOnTheLeft(text, start);
    }

    private static boolean hasHyphenAndLettersOnTheRight(final String text, final int index) {
        if (index >= text.length()) {
            return false;
        }

        final int hyphenCodePoint = text.codePointAt(index);
        if (!isHyphen(hyphenCodePoint)) {
            return false;
        }

        final int nextIndex = index + Character.charCount(hyphenCodePoint);
        return nextIndex < text.length() && Character.isLetter(text.codePointAt(nextIndex));
    }

    private static boolean hasLetterAndHyphenOnTheLeft(final String text, final int index) {
        if (index <= 0) {
            return false;
        }

        final int hyphenCodePoint = text.codePointBefore(index);
        if (!isHyphen(hyphenCodePoint)) {
            return false;
        }

        final int hyphenLength = Character.charCount(hyphenCodePoint);
        final int beforeHyphenIndex = index - hyphenLength;
        return beforeHyphenIndex > 0 && Character.isLetter(text.codePointBefore(beforeHyphenIndex));
    }

    private static boolean isHyphen(final int codePoint) {
        return codePoint == '-' || codePoint == '‐' || codePoint == '‑' || codePoint == '–' || codePoint == '—';
    }

    private static String normalizeToken(final String token) {
        return token.replaceAll("\\s+", " ").trim();
    }
}
