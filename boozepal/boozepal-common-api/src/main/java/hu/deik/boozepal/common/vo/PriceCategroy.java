package hu.deik.boozepal.common.vo;

/**
 * Ital kategória típusai.
 */
public enum PriceCategroy {
    LOWEST(1), LOWMEDIUM(2), MEDIUM(3), MEDHIGH(4), HIGHER(5);

    private Integer value;

    private PriceCategroy(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
