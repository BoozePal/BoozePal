package hu.deik.boozepal.rest.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Távoli felhasználót repreneztáló érték osztály.
 * <p>
 * A távoli felhasználó az akit az Android kliensről "léptetünk" a
 * rendszerünkbe, azaz az adatait letároljuk a mi adatbázisunkban is.
 *
 * @version 1.0
 */
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class RemoteTimeTableVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Token.
     */
    @NonNull
    @JsonProperty("token")
    private String token;

    @JsonProperty("timeTableList")
    private List<Date> timeTableList;

    @JsonCreator
    public RemoteTimeTableVO(@JsonProperty("token") String token, @JsonProperty("timeTableList") List<Date> timeTableList) {
        super();
        this.token = token;
        this.timeTableList = timeTableList;
    }
}
