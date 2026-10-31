private void loadSavedLogs() {
    try (BufferedReader reader = new BufferedReader(
            new FileReader(logger.getLogFile()))) {

        String line;

        while ((line = reader.readLine()) != null) {
            eventLog.append(line + "\n");

            int timestampEnd = line.indexOf("] ");

            if (timestampEnd >= 0) {
                String eventData = line.substring(timestampEnd + 2);

                int separator = eventData.indexOf(" |");

                String eventType = separator >= 0
                        ? eventData.substring(0, separator).trim()
                        : eventData.trim();

                summary.record(classifier.classify(eventType));
                eventCount++;
            }
        }

        updateSummary();

    } catch (IOException exception) {
        eventLog.append("No previous logs available.\n");
        updateSummary();
    }
}
