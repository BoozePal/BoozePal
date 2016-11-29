package hu.deik.boozepal.rest.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RemotePalRequestVO {

    /**
     * Felhasználó ID-ja.
     */
    private Long userId;

    /**
     * A megjelöltnek jelölt ID;
     */
    private Long requestedUserId;

    /**
     * A megjelölt dátum.
     */
    private Date date;
    
    /**
     * Kocsma azonosítója.
     */
    private Long pubId;

}
