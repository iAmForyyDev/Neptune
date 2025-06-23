package dev.lrxh.neptune.providers.clickable;

import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public final class Replacement {

    private final String placeholder;
    private final String replacement;

    public Replacement(final @NotNull String placeholder, String replacement) {
        this.placeholder = placeholder;
        this.replacement = replacement;
    }

    public Replacement(final @NotNull String placeholder, final @NotNull List<String> replacement) {
        this.placeholder = placeholder;

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < replacement.size(); i++) {
            builder.append(replacement.get(i));
            if (i < replacement.size() - 1) {
                builder.append("\n");
            }
        }

        this.replacement = builder.toString();
    }

    public Replacement(String placeholder, TextComponent replacement) {
        this.placeholder = placeholder;
        this.replacement = LegacyComponentSerializer.legacyAmpersand().serialize(replacement);
    }
}