package center.jhub.dev.bean;

public enum ApplicationEnvironment {
    NONE,
    BUILD,
    LOCAL_DEV,
    DEV,
    QA,
    PRE_RELEASE,
    PROD;

    public static ApplicationEnvironment fromString(String env) {
        return switch (env) {
            case "local_dev" -> ApplicationEnvironment.LOCAL_DEV;
            case "dev" -> ApplicationEnvironment.DEV;
            case "qa" -> ApplicationEnvironment.QA;
            case "pre-release" -> ApplicationEnvironment.PRE_RELEASE;
            case "prod" -> ApplicationEnvironment.PROD;
            case "build" -> ApplicationEnvironment.BUILD;
            default -> ApplicationEnvironment.NONE;
        };
    }

    public static boolean isBuilding(ApplicationEnvironment env) {
        return env.equals(ApplicationEnvironment.BUILD);
    }
}
