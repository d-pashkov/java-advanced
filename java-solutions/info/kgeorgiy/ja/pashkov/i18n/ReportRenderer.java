package info.kgeorgiy.ja.pashkov.i18n;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.Collator;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

final class ReportRenderer {
    private static final String BUNDLE_NAME = "info.kgeorgiy.ja.pashkov.i18n.TextStatisticsBundle";
    private static final int MAX_FRACTION_DIGITS = 3;

    private ReportRenderer() {
    }

    static String render(final Report report, final Locale outputLocale, final String inputFile) {
        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, outputLocale);
        final StringBuilder out = new StringBuilder();

        out.append(format(bundle, "input.file", inputFile)).append('\n');
        out.append(bundle.getString("summary.header")).append('\n');

        appendSummaryLine(out, bundle, "sentences", report.sentences().size(), outputLocale);
        appendSummaryLine(out, bundle, "words", report.words().size(), outputLocale);
        appendSummaryLine(out, bundle, "numbers", report.numbers().size(), outputLocale);
        appendSummaryLine(out, bundle, "currencies", report.currencies().size(), outputLocale);
        appendSummaryLine(out, bundle, "dates", report.dates().size(), outputLocale);

        final Collator collator = Collator.getInstance(report.textLocale());
        appendTextSection(out, bundle, "sentences", report.sentences(), collator, outputLocale);
        appendTextSection(out, bundle, "words", report.words(), collator, outputLocale);
        appendNumberSection(out, bundle, "numbers", report.numbers(), outputLocale, NumberFormat.getNumberInstance(outputLocale));
        appendNumberSection(out, bundle, "currencies", report.currencies(), outputLocale, NumberFormat.getCurrencyInstance(outputLocale));
        appendDateSection(out, bundle, report.dates(), outputLocale);

        return out.toString();
    }

    private static String format(final ResourceBundle bundle, final String key, final Object... args) {
        return MessageFormat.format(bundle.getString(key), args);
    }

    private static void appendSummaryLine(
            final StringBuilder out,
            final ResourceBundle bundle,
            final String kind,
            final int count,
            final Locale outputLocale
    ) {
        out.append('\t') // :NOTE: const
                .append(bundle.getString("count." + kind))
                .append(": ")
                .append(formatInteger(BigInteger.valueOf(count), outputLocale))
                .append(".\n"); // :NOTE: \n?
    }

    private static void appendTextSection(
            final StringBuilder out,
            final ResourceBundle bundle,
            final String kind,
            final List<String> values,
            final Collator collator,
            final Locale outputLocale
    ) {
        out.append(bundle.getString("section." + kind)).append('\n');

        final int count = values.size();
        final Set<String> distinct = new TreeSet<>(collator);
        distinct.addAll(values);

        appendCountWithDistinct(out, bundle, kind, count, distinct.size(), outputLocale);

        if (values.isEmpty()) {
            return;
        }

        final String min = values.stream().min(collator).orElseThrow();
        final String max = values.stream().max(collator).orElseThrow();

        out.append('\t')
                .append(bundle.getString("min." + kind))
                .append(": \"")
                .append(min)
                .append("\".\n");
        out.append('\t')
                .append(bundle.getString("max." + kind))
                .append(": \"")
                .append(max)
                .append("\".\n");

        final Comparator<String> byLength = Comparator.comparingInt(String::length);
        final String shortest = values.stream().min(byLength).orElseThrow();
        final String longest = values.stream().max(byLength).orElseThrow();

        out.append('\t')
                .append(bundle.getString("minLength." + kind))
                .append(": ")
                .append(formatInteger(BigInteger.valueOf(shortest.length()), outputLocale))
                .append(" (\"")
                .append(shortest)
                .append("\").\n");
        out.append('\t')
                .append(bundle.getString("maxLength." + kind))
                .append(": ")
                .append(formatInteger(BigInteger.valueOf(longest.length()), outputLocale))
                .append(" (\"")
                .append(longest)
                .append("\").\n");

        final BigInteger totalLength = values.stream()
                .map(value -> BigInteger.valueOf(value.length()))
                .reduce(BigInteger.ZERO, BigInteger::add);
        final BigDecimal averageLength = new BigDecimal(totalLength)
                .divide(BigDecimal.valueOf(values.size()), MAX_FRACTION_DIGITS, RoundingMode.HALF_UP);
        out.append('\t')
                .append(bundle.getString("avgLength." + kind))
                .append(": ")
                .append(formatAverageNumber(averageLength, outputLocale))
                .append(".\n");
    }

    private static void appendNumberSection(
            final StringBuilder out,
            final ResourceBundle bundle,
            final String kind,
            final List<Double> values,
            final Locale outputLocale,
            final NumberFormat formatter
    ) {
        out.append(bundle.getString("section." + kind)).append('\n');

        if (kind.equals("numbers")) {
            formatter.setGroupingUsed(false);
            formatter.setMinimumFractionDigits(1);
            formatter.setMaximumFractionDigits(MAX_FRACTION_DIGITS);
        } else {
            formatter.setGroupingUsed(true);
        }
        final Set<Double> distinct = new HashSet<>(values);
        appendCountWithDistinct(out, bundle, kind, values.size(), distinct.size(), outputLocale);

        if (values.isEmpty()) {
            return;
        }

        final double min = values.stream().mapToDouble(Double::doubleValue).min().orElseThrow();
        final double max = values.stream().mapToDouble(Double::doubleValue).max().orElseThrow();
        final double avg = values.stream().mapToDouble(Double::doubleValue).average().orElseThrow();

        out.append('\t')
                .append(bundle.getString("min." + kind))
                .append(": ")
                .append(formatter.format(min))
                .append(".\n");
        out.append('\t')
                .append(bundle.getString("max." + kind))
                .append(": ")
                .append(formatter.format(max))
                .append(".\n");
        out.append('\t')
                .append(bundle.getString("avg." + kind))
                .append(": ")
                .append(formatter.format(avg))
                .append(".\n");
    }

    private static void appendDateSection(
            final StringBuilder out,
            final ResourceBundle bundle,
            final List<Date> values,
            final Locale outputLocale
    ) {
        out.append(bundle.getString("section.dates")).append('\n');

        final Set<Long> distinct = values.stream().map(Date::getTime).collect(Collectors.toSet());
        appendCountWithDistinct(out, bundle, "dates", values.size(), distinct.size(), outputLocale);

        if (values.isEmpty()) {
            return;
        }

        final Date min = values.stream().min(Date::compareTo).orElseThrow();
        final Date max = values.stream().max(Date::compareTo).orElseThrow();
        final long avgEpoch = Math.round(values.stream().mapToLong(Date::getTime).average().orElseThrow());
        final Date avg = new Date(avgEpoch);
        final DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG, outputLocale);

        out.append('\t')
                .append(bundle.getString("min.dates"))
                .append(": ")
                .append(formatter.format(min))
                .append(".\n");
        out.append('\t')
                .append(bundle.getString("max.dates"))
                .append(": ")
                .append(formatter.format(max))
                .append(".\n");
        out.append('\t')
                .append(bundle.getString("avg.dates"))
                .append(": ")
                .append(formatter.format(avg))
                .append(".\n");
    }

    private static String formatAverageNumber(final BigDecimal value, final Locale locale) {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(MAX_FRACTION_DIGITS);
        format.setGroupingUsed(false);
        return format.format(value);
    }

    private static void appendCountWithDistinct(
            final StringBuilder out,
            final ResourceBundle bundle,
            final String kind,
            final int count,
            final int distinctCount,
            final Locale outputLocale
    ) {
        out.append('\t')
                .append(bundle.getString("count." + kind))
                .append(": ")
                .append(formatInteger(BigInteger.valueOf(count), outputLocale))
                .append(" (")
                .append(formatInteger(BigInteger.valueOf(distinctCount), outputLocale))
                .append(' ')
                .append(bundle.getString("word.different"))
                .append(").\n");
    }

    private static String formatInteger(final BigInteger value, final Locale locale) {
        final NumberFormat format = NumberFormat.getIntegerInstance(locale);
        format.setGroupingUsed(true);
        return format.format(value);
    }
}
