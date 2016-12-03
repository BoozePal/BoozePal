package hu.deik.boozepal.common.entity;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PalRequest {

    @ManyToOne
    private Pub pub;

    private Date date;
    
    @ManyToOne
    @JsonBackReference
    private User requesterUser;
    
    private boolean accepted;

}
