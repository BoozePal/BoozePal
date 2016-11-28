package hu.deik.boozepal.common.entity;

public enum DrinkTypeEnum {

    /**
     * 
     */
    BEER("beer"),
    /**
     * 
     */
    WINE("wine"),
    /**
     * 
     */
    WHISKEY("whiskey"),
    /**
     * 
     */
    GIN("gin"),
    /**
     * 
     */
    VODKA("vodka"),
    /**
     * 
     */
    BRANDY("brandy"),
    /**
     * 
     */
    CHAMPAGNE("champagne"),
    /**
     * 
     */
    RUM("rum"),
    /**
     * 
     */
    UNKNOWN("unknown");

    private String value;

    private DrinkTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
