package leviathan.CarPartsStore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeDTO {
    private String attributeName;
    private String attributePicture;
    private String attributeValue;
}
