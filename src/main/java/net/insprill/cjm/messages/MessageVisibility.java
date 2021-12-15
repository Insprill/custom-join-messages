package net.insprill.cjm.messages;

public enum MessageVisibility {

    PUBLIC("Public"),
    PRIVATE("Private");

    private final String configSection;

    MessageVisibility(String configSection) {
        this.configSection = configSection;
    }

    public String getConfigSection() {
        return configSection;
    }

}
