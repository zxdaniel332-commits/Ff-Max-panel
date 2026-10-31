package com.kanha.ffpanel;

public class EventClassifier {

    public enum Result {
        NORMAL,
        DUPLICATE,
        INVALID_SEQUENCE,
        UNKNOWN
    }

    public Result classify(String eventType) {

        if (eventType == null || eventType.trim().isEmpty()) {
            return Result.UNKNOWN;
        }

        switch (eventType.trim().toUpperCase()) {

            case "NORMAL_HEARTBEAT":
                return Result.NORMAL;

            case "DUPLICATE_EVENT":
                return Result.DUPLICATE;

            case "INVALID_SEQUENCE":
                return Result.INVALID_SEQUENCE;

            default:
                return Result.UNKNOWN;
        }
    }

    public String getDescription(Result result) {

        switch (result) {

            case NORMAL:
                return "Normal synthetic event";

            case DUPLICATE:
                return "Duplicate synthetic event";

            case INVALID_SEQUENCE:
                return "Invalid synthetic event sequence";

            default:
                return "Unknown synthetic event";
        }
    }
}
