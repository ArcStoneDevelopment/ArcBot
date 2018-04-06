package Appeal;

public enum AppealStatus {
    INCOMPLETE("Unfinished."),
    ARCHIVED("Completed."),
    NEW("Unread"),
    VIEWED("Read. Undecided.");

    String status;
    AppealStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }

    public static AppealStatus[] getStatuses() {
        return new AppealStatus[]{INCOMPLETE, NEW, VIEWED};
    }
}
