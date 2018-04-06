package Report;

public enum ReportStatus {
    INCOMPLETE("Unfinished"),
    ARCHIVED("Completed"),
    OPEN("Not Claimed"),
    WORKING("Claimed, Not Completed");

    String status;
    ReportStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }
}
