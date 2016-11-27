package hu.deik.boozepal.rest.vo;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GPS koordinátát reprezenáltó érték osztály.
 * 
 * @author Nandi
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoordinateVO {

    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;

    @JsonCreator
    public CoordinateVO(String json) {
        ObjectMapper mapper = new ObjectMapper();
        CoordinateVO coordinate;
        try {
            coordinate = mapper.readValue(json, CoordinateVO.class);
            latitude = coordinate.getLatitude();
            longitude = coordinate.getLongitude();
        } catch (IOException e) {
        }

    }
}
