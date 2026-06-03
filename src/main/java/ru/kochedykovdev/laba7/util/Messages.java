package ru.kochedykovdev.laba7.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

    public static final ResourceBundle bundle =
        //ResourceBundle.getBundle("ru.kochedykovdev.laba7.messages", Locale.ENGLISH);
        //ResourceBundle.getBundle("ru.kochedykovdev.laba7.messages", Locale.forLanguageTag("ru"));
        //ResourceBundle.getBundle("ru.kochedykovdev.laba7.messages", Locale.forLanguageTag("ua"));
        ResourceBundle.getBundle("ru.kochedykovdev.laba7.messages", Locale.getDefault());
}
