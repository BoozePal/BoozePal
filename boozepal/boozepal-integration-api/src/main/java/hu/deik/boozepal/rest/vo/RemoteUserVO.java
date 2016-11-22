package hu.deik.boozepal.rest.vo;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
public class RemoteUserVO {

    private Long id;
    private String name;
    private String city;
    private List<String> boozes;
    private List<String> pubs;
    private List<Date> savedDates;
    private int searchRadius;
    private int priceCategory;
    private List<RemoteUserVO> myPals;
}
