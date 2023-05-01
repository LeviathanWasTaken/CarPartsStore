package leviathan.CarPartsStore.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import leviathan.CarPartsStore.model.Status;
import lombok.Data;

@Entity
@Table(name = "catalogs")
@Data
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID UUID;
    @Column(unique = true)
    private String uniqueTag;
    private String catalogName;
    private String imgSource;
    private long popularity;
    private Status status;
    @Column(name = "left_boundary")
    private int left;

    @Column(name = "right_boundary")
    private int right;
    @OneToMany(mappedBy = "catalog")
    private List<Product> products;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_uuid")
    private Catalog parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Catalog> children = new HashSet<>();

    public Catalog() {
    }

    public Catalog(String catalogName, String imgSource, String uniqueTag, Status status) {
        this.catalogName = catalogName;
        this.imgSource = imgSource;
        this.uniqueTag = uniqueTag;
        this.status = status;
        popularity = 0;
    }

}
