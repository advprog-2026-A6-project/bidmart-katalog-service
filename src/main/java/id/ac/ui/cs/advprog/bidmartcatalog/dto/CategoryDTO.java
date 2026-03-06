package id.ac.ui.cs.advprog.bidmartcatalog.dto;

import lombok.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private UUID id;
    private String name;
    private String fullPath;
    private List<CategoryDTO> children;
}