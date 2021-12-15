package net.insprill.cjm.messages;

public enum MessageAction {

    FIRST_JOIN("First-Join"),
    JOIN("Join"),
    QUIT("Quit");

    private final String configSection;

    MessageAction(String configSection) {
        this.configSection = configSection;
    }

    public String getConfigSection() {
        return configSection;
    }

}
