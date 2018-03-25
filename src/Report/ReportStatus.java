package Report;

public enum ReportStatus {
    INCOMPLETE("Unfinished"),
    OPEN("Not Claimed"),
    WORKING("Claimed, Not Completed"),
    ARCHIVED("Completed");

    String status;
    ReportStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }
}
