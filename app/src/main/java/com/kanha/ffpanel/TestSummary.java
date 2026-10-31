package com.kanha.ffpanel;

public class TestSummary {

    private int totalEvents;
    private int normalEvents;
    private int duplicateEvents;
    private int invalidEvents;
    private int unknownEvents;

    public void record(EventClassifier.Result result) {
        totalEvents++;

        switch (result) {
            case NORMAL:
                normalEvents++;
                break;

            case DUPLICATE:
                duplicateEvents++;
                break;

            case INVALID_SEQUENCE:
                invalidEvents++;
                break;

            default:
                unknownEvents++;
                break;
        }
    }

    public String getReport() {
        return "TEST SUMMARY\n"
                + "Total: " + totalEvents + "\n"
                + "Normal: " + normalEvents + "\n"
                + "Duplicate: " + duplicateEvents + "\n"
                + "Invalid sequence: " + invalidEvents + "\n"
                + "Unknown: " + unknownEvents;
    }
}
