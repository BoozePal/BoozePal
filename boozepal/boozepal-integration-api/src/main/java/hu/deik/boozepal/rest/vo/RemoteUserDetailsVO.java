package hu.deik.boozepal.rest.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
// @AllArgsConstructor
@Data
@Builder
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class RemoteUserDetailsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("token")
    private String token;
    @JsonProperty("user")
    private RemoteUserVO user;

    @JsonCreator
    public RemoteUserDetailsVO(@JsonProperty("token") String token, @JsonProperty("user") RemoteUserVO user) {
        super();
        this.token = token;
        this.user = user;
    }

}
