package com.kanha.ffpanel;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventLogger {

    private final File logFile;

    public EventLogger(Context context) {
        File directory = new File(context.getFilesDir(), "research_logs");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        logFile = new File(directory, "events.log");
    }

    public synchronized boolean log(String event) {
        String timestamp = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
        ).format(new Date());

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write("[" + timestamp + "] " + event + "\n");
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    public File getLogFile() {
        return logFile;
    }
}
