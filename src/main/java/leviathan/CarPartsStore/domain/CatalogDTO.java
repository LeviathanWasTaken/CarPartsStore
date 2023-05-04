package leviathan.CarPartsStore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogDTO {
    private UUID catalogUUID;
    private String catalogName;
    private String catalogPicture;
    private long popularity;
}
