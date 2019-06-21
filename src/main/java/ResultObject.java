class ResultObject {
    private Long timestamp;
    private Long responseTime;
    private int statusCode;

    Long getTimestamp() {
        return timestamp;
    }

    ResultObject setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    Long getResponseTime() {
        return responseTime;
    }

    ResultObject setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
        return this;
    }

    int getStatusCode() {
        return statusCode;
    }

    ResultObject setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
