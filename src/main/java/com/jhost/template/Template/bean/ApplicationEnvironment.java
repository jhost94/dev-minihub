package com.jhost.template.Template.bean;

public enum ApplicationEnvironment {
    NONE,
    BUILD,
    LOCAL_DEV,
    DEV,
    QA,
    PRE_RELEASE,
    PROD;

    public static ApplicationEnvironment fromString(String env) {
        switch (env) {
            case "local_dev":
                return ApplicationEnvironment.LOCAL_DEV;
            case "dev":
                return ApplicationEnvironment.DEV;
            case "qa":
                return ApplicationEnvironment.QA;
            case "pre-release":
                return ApplicationEnvironment.PRE_RELEASE;
            case "prod":
                return ApplicationEnvironment.PROD;
            case "build":
                return ApplicationEnvironment.BUILD;
            default:
                return ApplicationEnvironment.NONE;
        }
    }

    public static boolean isBuilding(ApplicationEnvironment env) {
        return env.equals(ApplicationEnvironment.BUILD);
    }
}
