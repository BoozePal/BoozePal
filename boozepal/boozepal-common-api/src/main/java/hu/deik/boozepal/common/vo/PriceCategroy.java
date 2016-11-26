package hu.deik.boozepal.common.vo;

/**
 * Ital kategória típusai.
 */
public enum PriceCategroy {
    /**
     * A legolcsóbb árkategória.
     */
    LOWEST(1),
    /**
     * Az olcsóbnál jobb árkategória.
     */
    LOWMEDIUM(2),
    /**
     * Közepes árkategória.
     */
    MEDIUM(3),
    /**
     * Közepesnél drágább árkategória.
     */
    MEDHIGH(4),
    /**
     * A legdrágább árkategória.
     */
    HIGHER(5);

    /**
     * Számban való értékelés.
     */
    private Integer value;

    private PriceCategroy(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
