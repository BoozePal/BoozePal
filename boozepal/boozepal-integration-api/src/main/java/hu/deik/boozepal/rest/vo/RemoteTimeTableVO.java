package hu.deik.boozepal.rest.vo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Távoli felhasználót repreneztáló érték osztály.
 * 
 * A távoli felhasználó az akit az Android kliensről "léptetünk" a
 * rendszerünkbe, azaz az adatait letároljuk a mi adatbázisunkban is.
 * 
 * @version 1.0
 *
 */
@NoArgsConstructor
@AllArgsConstructor
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
    private String token;

    private List<Date> timeTableList;

}
