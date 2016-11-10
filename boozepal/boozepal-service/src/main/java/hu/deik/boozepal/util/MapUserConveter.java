package hu.deik.boozepal.util;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.vo.MapUserVO;

public class MapUserConveter {
    private MapUserConveter() {
        // empty constructor
    }

    /**
     * Felhaszáló entitást térképen való megjeleníthető érték osztállyá
     * konvertál át.
     * 
     * @param user
     *            felhasználó entitást.
     * @return a kapott felhasználó entitás térképen megejeleníthető változata.
     */
    public static MapUserVO toMapUserVO(User user) {
        if (notNull(user) && notNull(user.getLastKnownCoordinate()))
            return MapUserVO.builder().username(user.getUsername())
                    .latitude(user.getLastKnownCoordinate().getLatitude())
                    .altitude(user.getLastKnownCoordinate().getAltitude()).build();
        else
            return MapUserVO.builder().build();
    }

    private static boolean notNull(Object object) {
        return object != null;
    }
}
