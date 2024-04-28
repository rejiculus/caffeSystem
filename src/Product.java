public class Product implements Cloneable, Comparable<Product> {
    private String name;
    private ProductType type;
    private double cost;
    private boolean isToGo;

    public Product(String name, ProductType type, double cost, boolean isToGo) {
        if (name == null || type == null) throw new RuntimeException("Cant be null");
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.isToGo = isToGo;
    }

    // getters
    public String getName() {
        return name;
    }

    public ProductType getType() {
        return type;
    }

    public double getCost() {
        return cost;
    }

    public boolean getIsToGo() {
        return isToGo;
    }

    @Override
    public int compareTo(Product product) {
        return name.compareTo(product.getName());
    }

    @Override
    public Product clone() {
        return new Product(name, type, cost, isToGo);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !this.getClass().equals(o.getClass())) return false;
        Product p = (Product) o;
        return this.name.equals(p.getName())
                && this.type.equals(p.getType())
                && this.cost == p.getCost()
                && this.isToGo == p.getIsToGo();
    }

    @Override
    public String toString() {
        return "Product{" +
                "name: " + this.name + ", " +
                "type: " + this.type + ", " +
                "cost: " + this.cost + ", " +
                "to go?: " + this.isToGo + "}";
    }
}
