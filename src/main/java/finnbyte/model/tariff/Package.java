package finnbyte.model.tariff;

public enum Package {
    SMALL("S"),
    MEDIUM("M"),
    LARGE("L");

    private final String code;

    Package(String code) {
        this.code = code;
    }

    public static Package forCode(String code) {
        for (Package pckg : values()) {
            if (pckg.getCode().equalsIgnoreCase(code)) {
                return pckg;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}
