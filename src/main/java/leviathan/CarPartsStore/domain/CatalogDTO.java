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
    private String parentCatalogName;
    private boolean isRemoved;
    private RemovalStatus removalStatus;

    public CatalogDTO(UUID catalogUUID, String catalogName, String catalogPicture) {
        this.catalogUUID = catalogUUID;
        this.catalogName = catalogName;
        this.catalogPicture = catalogPicture;
    }

    public CatalogDTO(String catalogName, String catalogPicture, String parentCatalogName) {
        this.catalogName = catalogName;
        this.catalogPicture = catalogPicture;
        this.parentCatalogName = parentCatalogName;
    }
}
