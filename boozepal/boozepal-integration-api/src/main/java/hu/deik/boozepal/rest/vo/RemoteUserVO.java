package hu.deik.boozepal.rest.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
public class RemoteUserVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Token.
     */
    @NonNull
    private String token;

}
