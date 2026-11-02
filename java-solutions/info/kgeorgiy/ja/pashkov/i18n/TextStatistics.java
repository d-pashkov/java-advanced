package info.kgeorgiy.ja.pashkov.i18n;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.MissingResourceException;

public final class TextStatistics {
    private TextStatistics() {
    }

    public static void main(final String[] args) {
        if (args == null || args.length != 4) {
            System.err.println("Usage: TextStatistics <input locale> <output locale> <input file> <output file>");
            return;
        }
        for (final String arg : args) {
            if (arg == null) {
                System.err.println("Arguments must be non-null");
                return;
            }
        }

        final Locale textLocale = Locale.forLanguageTag(args[0].trim());
        final Locale outputLocale = Locale.forLanguageTag(args[1].trim());

        final Path input;
        final Path output;
        
        try {
            input = Path.of(args[2]);
            output = Path.of(args[3]);
        } catch (final InvalidPathException e) {
            System.err.println("Invalid path: " + e.getInput());
            return;
        }

        try {
            final String text = Files.readString(input, StandardCharsets.UTF_8);
            final Report report = TextAnalyzer.analyze(text, textLocale);
            final String rendered = ReportRenderer.render(report, outputLocale, input.getFileName().toString());
            final Path parent = output.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(output, rendered, StandardCharsets.UTF_8);
        } catch (final MissingResourceException e) {
            System.err.println("Missing resource bundle: " + e.getClassName());
        } catch (final IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}
