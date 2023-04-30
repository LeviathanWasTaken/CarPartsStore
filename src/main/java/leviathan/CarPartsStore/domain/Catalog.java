package leviathan.CarPartsStore.domain;

import jakarta.persistence.*;
import leviathan.CarPartsStore.model.Status;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "catalogs")
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

    public Catalog getParent() {
        return parent;
    }
    public void setParent(Catalog parent) {
        this.parent = parent;
    }
    public Set<Catalog> getChildren() {
        return children;
    }
    public void setChildren(Set<Catalog> children) {
        this.children = children;
    }
    public String getImgSource() {
        return imgSource;
    }
    public void setImgSource(String imgPath) {
        this.imgSource = imgPath;
    }
    public String getUniqueTag() {
        return uniqueTag;
    }
    public void setUniqueTag(String uniqueTag) {
        this.uniqueTag = uniqueTag;
    }
    public String getCatalogName() {
        return catalogName;
    }
    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }
    public List<Product> getProducts() {
        return products;
    }
    public java.util.UUID getUUID() {
        return UUID;
    }
    public void setUUID(java.util.UUID UUID) {
        this.UUID = UUID;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public long getPopularity() {
        return popularity;
    }
    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public int getRight() {
        return right;
    }
    public void setRight(int right) {
        this.right = right;
    }
    public int getLeft() {
        return left;
    }
    public void setLeft(int left) {
        this.left = left;
    }
}
