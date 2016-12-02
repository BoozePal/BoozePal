package hu.deik.boozepal.rest.vo;

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
public class RemotePalAcceptVO {

    /**
     * Felhasználó ID-ja.
     */
    private Long userId;

    /**
     * A megjelöltnek jelölt ID;
     */
    private Long requestedUserId;

    /**
     * Elfogadott állapota.
     */
    private boolean accepted;

}
