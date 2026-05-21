package id.ac.ui.cs.advprog.bidmartcatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerPublicProfileDTO {
    private Long id;
    private String name;
    private String bio;
    private String profilePictureUrl;
}
