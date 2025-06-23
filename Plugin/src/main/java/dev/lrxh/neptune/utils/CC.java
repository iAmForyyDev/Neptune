package dev.lrxh.neptune.utils;

import dev.lrxh.neptune.configs.impl.MessagesLocale;
import dev.lrxh.neptune.providers.clickable.Replacement;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public final class CC {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("<[^>]+>");

    public TextComponent error(String message) {
        return color(MessagesLocale.ERROR_MESSAGE.getString().replace("<error>", message));
    }

    public TextComponent success(String text) {
        return color("&a[+] " + text);
    }

    public TextComponent info(String text) {
        return color("&7[~] " + text);
    }

    public TextComponent color(final @Nullable String message) {
        return message == null || message.isEmpty() ? Component.empty() :
                LegacyComponentSerializer.legacyAmpersand()
                        .deserialize(message);
    }

    public @NotNull Component returnMessage(final String message, final Replacement @NotNull ... replacements) {
        final Map<String, String> replacementMap = new HashMap<>(replacements.length);
        for (final Replacement replacement : replacements) {
            replacementMap.put(replacement.getPlaceholder(), replacement.getReplacement());
        }

        final StringBuilder resultBuilder = new StringBuilder();
        final Matcher matcher = PLACEHOLDER_PATTERN.matcher(message);

        while (matcher.find()) {
            final String foundPlaceholder = matcher.group();
            final String replacementString = replacementMap.get(foundPlaceholder);
            if (replacementString != null) {
                matcher.appendReplacement(resultBuilder, Matcher.quoteReplacement(replacementString));
            } else {
                matcher.appendReplacement(resultBuilder, Matcher.quoteReplacement(foundPlaceholder));
            }
        }

        matcher.appendTail(resultBuilder);
        return LegacyComponentSerializer.legacyAmpersand()
                .deserialize(resultBuilder.toString());
    }
}