class Timer {


    private Long startTime;
    private Long stopTime;
    private Long elapsedTime = null;

    void start() {
        this.startTime = System.nanoTime();
    }

    Long getStartTime() {
        return startTime;
    }

    void stop() {
        stopTime = System.nanoTime();
        elapsedTime = stopTime - startTime;
    }

    private void reset(){
        startTime = null;
        stopTime = null;
    }

    Long getElapsedTimeInMilliseconds() {
        if (elapsedTime != null)
            return elapsedTime / 1000000;
        else
            return elapsedTime;
    }

}
