package hu.deik.boozepal.rest.vo;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import hu.deik.boozepal.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PayloadUserVO {
    private Payload payload;
    private User user;
}
