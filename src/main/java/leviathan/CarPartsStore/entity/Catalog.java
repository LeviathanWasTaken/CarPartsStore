package leviathan.CarPartsStore.entity;

import jakarta.persistence.*;
import leviathan.CarPartsStore.domain.RemovalStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "catalogs")
@Data
@NoArgsConstructor
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID catalogUUID;

    @Column(unique = true)
    private String catalogName;

    private String imgSource;
    private long popularity;

    @Enumerated(EnumType.STRING)
    private RemovalStatus removalStatus;

    @Column(name = "left_boundary")
    private int left;

    @Column(name = "right_boundary")
    private int right;

    @OneToMany(mappedBy = "catalog")
    private List<Product> products = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_uuid")
    private Catalog parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Catalog> children = new HashSet<>();

    public Catalog(String catalogName, String imgSource, RemovalStatus removalStatus) {
        this.catalogName = catalogName;
        this.imgSource = imgSource;
        this.removalStatus = removalStatus;
        popularity = 0;
    }

    @Override
    public int hashCode() {
        return catalogUUID.hashCode();
    }

    @Override
    public String toString() {
        return "{catalogName: "+catalogName+", left: "+left+", right: "+right+"}";
    }
}
