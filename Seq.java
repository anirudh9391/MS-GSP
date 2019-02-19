
import java.util.ArrayList;

public class Seq {

    public ArrayList<Integer> items;

    public Seq()
    {
        items=new ArrayList<Integer>(); // initialize inside constructor
    }

    public boolean isSubset(Seq itemSet)
    {
        return itemSet.items.containsAll(this.items); // checks if the itemset passed into function contains the itemset belonging to the parent class that called and created the instance
    }
}

