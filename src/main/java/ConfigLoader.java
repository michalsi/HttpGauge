class ConfigLoader {

    static String getEnvOrElse(String var, String defaultValue) {
        String env = System.getenv(var);
        return env != null ? env : defaultValue;
    }

    static long getEnvOrElse(String var, long defaultValue){
        String env = System.getenv(var);
        return env != null ? Long.parseLong(env) : defaultValue;
    }
}
