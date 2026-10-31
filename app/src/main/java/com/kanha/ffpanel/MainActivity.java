
package com.kanha.ffpanel;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView eventLog;
    private TextView summaryView;
    private EventLogger logger;
    private EventClassifier classifier;
    private TestSummary summary;
    private int eventCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logger = new EventLogger(this);
        classifier = new EventClassifier();
        summary = new TestSummary();

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 32, 24, 24);
        root.setBackgroundColor(0xFF101820);

        TextView title = new TextView(this);
        title.setText("Anti-Cheat Research Lab");
        title.setTextSize(24);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(0xFFFFFFFF);

        TextView subtitle = new TextView(this);
        subtitle.setText("Synthetic Event Testing Environment");
        subtitle.setTextColor(0xFFB0BEC5);

        Button testButton = new Button(this);
        testButton.setText("Generate Test Event");

        summaryView = new TextView(this);
        summaryView.setTextColor(0xFFFFFFFF);
        summaryView.setTextSize(16);
        summaryView.setPadding(0, 20, 0, 20);

        eventLog = new TextView(this);
        eventLog.setTextColor(0xFF80CBC4);
        eventLog.setTextSize(14);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(eventLog);

        Button clearButton = new Button(this);
        clearButton.setText("Clear Screen Log");
        clearButton.setOnClickListener(v -> eventLog.setText(""));
        testButton.setOnClickListener(v -> generateTestEvent());

        root.addView(title);
        root.addView(subtitle);
        root.addView(testButton);
        root.addView(summaryView);

        TextView logTitle = new TextView(this);
        logTitle.setText("EVENT CLASSIFICATION LOG");
        logTitle.setTextColor(0xFFFFFFFF);
        logTitle.setTypeface(null, Typeface.BOLD);
        logTitle.setPadding(0, 12, 0, 12);

        root.addView(logTitle);
        root.addView(scrollView);
        root.addView(clearButton);

        setContentView(root);
        loadSavedLogs();
    }

    private void generateTestEvent() {
        eventCount++;

        String[] testTypes = {
                "NORMAL_HEARTBEAT",
                "DUPLICATE_EVENT",
                "INVALID_SEQUENCE",
                "UNKNOWN_EVENT"
        };

        String eventType = testTypes[(eventCount - 1) % testTypes.length];
        EventClassifier.Result result = classifier.classify(eventType);

        summary.record(result);
        updateSummary();

        String description = classifier.getDescription(result);
        String time = new SimpleDateFormat(
                "HH:mm:ss", Locale.getDefault()
        ).format(new Date());

        String event = eventType
                + " | Classification: " + description
                + " | Source: Local Simulator";

        boolean saved = logger.log(event);

        eventLog.append(
                "[" + time + "] " + event
                + (saved ? " | SAVED\n" : " | SAVE FAILED\n")
        );
    }

    private void updateSummary() {
        summaryView.setText(summary.getReport());
    }

    private void loadSavedLogs() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(logger.getLogFile()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                eventLog.append(line + "\n");

                int timestampEnd = line.indexOf("] ");
                if (timestampEnd < 0) {
                    continue;
                }

                String eventData = line.substring(timestampEnd + 2);
                int separator = eventData.indexOf(" |");

                if (separator < 0
                        || !eventData.contains("| Source: Local Simulator")) {
                    continue;
                }

                String eventType = eventData.substring(0, separator).trim();
                summary.record(classifier.classify(eventType));
                eventCount++;
            }

        } catch (IOException exception) {
            eventLog.append("No previous logs available.\n");
        }

        updateSummary();
    }
}
