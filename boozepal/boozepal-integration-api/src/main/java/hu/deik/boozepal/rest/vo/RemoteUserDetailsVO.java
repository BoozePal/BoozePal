package hu.deik.boozepal.rest.vo;

import java.io.Serializable;

import lombok.*;

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
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class RemoteUserDetailsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;

    private RemoteUserVO user;
}
