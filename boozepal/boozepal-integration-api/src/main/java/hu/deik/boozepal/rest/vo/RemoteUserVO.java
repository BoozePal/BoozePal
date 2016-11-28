package hu.deik.boozepal.rest.vo;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.deik.boozepal.common.vo.DrinkVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
public class RemoteUserVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("city")
    private String city;
    @JsonProperty("boozes")
    private List<DrinkVO> boozes;
    @JsonProperty("pubs")
    private List<String> pubs;
    @JsonProperty("savedDates")
    private List<Date> savedDates;
    @JsonProperty("searchRadius")
    private int searchRadius;
    @JsonProperty("priceCategory")
    private int priceCategory;
    @JsonProperty("myPals")
    private List<RemoteUserVO> myPals;
    @JsonProperty("lastKnownCoordinate")
    private CoordinateVO lastKnownCoordinate;


    @JsonCreator
    public RemoteUserVO(String json) {
        ObjectMapper mapper = new ObjectMapper();
        RemoteUserVO remoteUser;
        try {
            remoteUser = mapper.readValue(json, RemoteUserVO.class);
            this.id = remoteUser.getId();
            this.name = remoteUser.getName();
            this.city = remoteUser.getCity();
            this.boozes = remoteUser.getBoozes();
            this.pubs = remoteUser.getPubs();
            this.savedDates = remoteUser.getSavedDates();
            this.searchRadius = remoteUser.getSearchRadius();
            this.priceCategory = remoteUser.getPriceCategory();
            this.myPals = remoteUser.getMyPals();
            this.lastKnownCoordinate = remoteUser.getLastKnownCoordinate();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
