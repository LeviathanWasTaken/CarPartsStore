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
    private boolean hasChildren;

    public CatalogDTO(UUID catalogUUID) {
        this.catalogUUID = catalogUUID;
    }

    public CatalogDTO(UUID catalogUUID, String catalogName) {
        this.catalogUUID = catalogUUID;
        this.catalogName = catalogName;
    }

    public CatalogDTO(UUID catalogUUID, String catalogName, String catalogPicture, boolean hasChildren) {
        this.catalogUUID = catalogUUID;
        this.catalogName = catalogName;
        this.catalogPicture = catalogPicture;
        this.hasChildren = hasChildren;
    }

    public CatalogDTO(UUID catalogUUID, String catalogName, String catalogPicture, long popularity, String parentCatalogName, RemovalStatus removalStatus, boolean isRemoved) {
        this.catalogUUID = catalogUUID;
        this.catalogName = catalogName;
        this.catalogPicture = catalogPicture;
        this.popularity = popularity;
        this.parentCatalogName = parentCatalogName;
        this.removalStatus = removalStatus;
        this.isRemoved = isRemoved;
    }
}
