package hu.deik.boozepal.rest.vo;

import lombok.*;

import java.io.*;

/**
 * Távoli felhasználót repreneztáló érték osztály.
 * <p>
 * A távoli felhasználó az akit az Android kliensről "léptetünk" a
 * rendszerünkbe, azaz az adatait letároljuk a mi adatbázisunkban is.
 *
 * @version 1.0
 */
@Builder
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class RemoteUserDetailsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;
    private hu.deik.boozepal.common.entity.User user;

}
