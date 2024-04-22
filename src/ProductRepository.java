import java.util.Set;
import java.util.TreeSet;

public class ProductRepository {
    private static TreeSet<Product> products;

    static {
        products=new TreeSet<>();
        products.add(new Product("soup",ProductType.HOT_FOOD,100.0,false));
        products.add(new Product("tea",ProductType.HOT_DRINK,50.0,true));
        products.add(new Product("coffee",ProductType.HOT_DRINK,150.0,true));
        products.add(new Product("cake",ProductType.FOOD,300.0,true));
        products.add(new Product("sidr",ProductType.ALCOHOL,200.0,true));

    }
    public static Set<Product> getAll(){
        return products;
    }
    public static Product getByName(String name){
        for(Product p:products){
            if(name.equals(p.getName()))
                return p;
        }
        return null;
    }

}
