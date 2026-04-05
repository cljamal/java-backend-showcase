package com.sultanov.present_project.core.utils;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Lang {
    private final MessageSource messageSource;

    public Locale locale() {
        return LocaleContextHolder.getLocale();
    }

    public String text(String key) {
        return messageSource
                .getMessage(
                        key,
                        null,
                        locale()
                );
    }

    public String args(String key, Object... args) {
        return messageSource
                .getMessage(
                        key,
                        args,
                        locale()
                );
    }

    public String text(String key, String lang) {
        return messageSource
                .getMessage(
                        key,
                        null,
                        Locale.forLanguageTag(lang)
                );
    }

    public String text(String key, String lang, Object... args) {
        Locale locale = lang != null
                ? Locale.forLanguageTag(lang)
                : locale();

        return messageSource
                .getMessage(
                        key,
                        args,
                        locale
                );
    }

    public String textOrDefault(String key, String defaultMessage) {
        return messageSource.getMessage(key, null, defaultMessage, locale());
    }
}
