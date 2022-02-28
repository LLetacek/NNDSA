package nndsa.sem.a.railway;

public enum RailwayTrackType {
    DIRECT(true),
    SWITCH(false);

    private final boolean allowedToStop;

    private RailwayTrackType(boolean isStoppable) {
        this.allowedToStop = isStoppable;
    }

    public boolean isAllowedToStop() {
        return allowedToStop;
    }
}
