
import java.util.ArrayList;
import java.util.HashSet;


public class ExtSequence
{
    public ArrayList<Seq> itemSets;
    public int count;  // count the occurrence of the transaction

    ExtSequence()
    {
        itemSets=new ArrayList<Seq>();
        count=0;
    }

    public HashSet<Integer> getItemsAsSet()
    {
        HashSet<Integer> item = new HashSet<>();
        for(int i=0;i<itemSets.size();i++)
        {
            item.addAll(itemSets.get(i).items);
        }
        return item;

    }

    public ArrayList<Integer> getItems()
    {
        ArrayList<Integer> item = new ArrayList<>();
        for(int i=0;i<itemSets.size();i++)
        {
            item.addAll(itemSets.get(i).items);
        }
        return item;
    }

    public Integer getFirstItem()
    {
        return itemSets.get(0).items.get(0);
    }


    public Integer getLastItem()
    {

        ArrayList<Integer> items = itemSets.get(itemSets.size()-1).items;
        return items.get(items.size()-1);
    }


    public boolean isMin(Integer elementKey, int flag) // we need to test if
    {
        boolean result = true;
        ArrayList<Integer> items = this.getItems();
        if (flag == 0) // if this element is the first item
            items.remove(0);
        else if (flag == 1) // its the last item
        {
            items.remove(items.size() - 1);
        }
        for (int i=0;i<items.size();i++)//Integer id : items)
        {

            if (MSGSPMain.MS.get(items.get(i)).doubleValue() <= MSGSPMain.MS.get(elementKey).doubleValue())
            {// if we find another item with a smaller MIS, we are done.
                result = false;
                break;
            }
        }
        return result;
    }


    public boolean subsetCompare(ExtSequence tran, int index1, int index2)
    {
        boolean result = false;
        int s1 = this.itemSets.size();
        int s2 = tran.itemSets.size();
        ExtSequence current = new ExtSequence();
        ExtSequence compare = new ExtSequence();
        current = this.replicate();
        compare = tran.replicate();
        int i=0, j=0, index;
        for (index=0; index<s1; index++) {
            Seq is = current.itemSets.get(index);
            j = i;
            i += is.items.size();
            if (i > index1)
                break;
        }
        current.itemSets.get(index).items.remove(index1 - j);
        if (current.itemSets.get(index).items.size() == 0)
            current.itemSets.remove(index);
        i = 0;
        j = 0;
        for (index=0; index<s2; index++) {
            Seq is = compare.itemSets.get(index);
            j = i;
            i += is.items.size();
            if (i > index2)
                break;
        }
        compare.itemSets.get(index).items.remove(index2 - j);
        if (compare.itemSets.get(index).items.size() == 0)
            compare.itemSets.remove(index);
        int s12 = current.itemSets.size();
        int s22 = compare.itemSets.size();
        int l12 = current.getItems().size();
        int l22 = compare.getItems().size();
        if (current.subsetOf(compare) && s12 == s22 && l12 == l22)
            result = true;
        return result;
    }


    public ExtSequence replicate() // to replicate the sequence when we dont want a change
    {
        ExtSequence transObj = new ExtSequence();
        for (int i=0; i<this.itemSets.size(); i++)
        {
            Seq item = new Seq();
            item.items.addAll(this.itemSets.get(i).items);
            transObj.itemSets.add(item);
        }
        return transObj;
    }


    public boolean subsetOf(ExtSequence transObj) // to check if the input sequence is a subset of this parent(this object)
    {
        boolean result = true;
        int m = this.itemSets.size();
        int n = transObj.itemSets.size();
        int i=0, j=0;
        for(i=0; i<m; i++) {
            Seq item = this.itemSets.get(i);
            do {
                if (m-i > n-j)
                {
                    result = false;
                    break;
                }
                if (item.isSubset(transObj.itemSets.get(j)))  // from Seq class returns itemSet.items.containsAll(this.items);
                {
                    j++;
                    break;
                }
                j++;
            } while(j<n);
            if (result == false)
            {
                break;
            }
            if (i==m-1 && j==n) {
                result = item.isSubset(transObj.itemSets.get(j-1));
            }
        }
        return result;
    }

    /*
     * This method is used to return the item that has the lowest MIS value in the transaction
     */
    public int minMISItem()
    {
        ArrayList<Integer> items = this.getItems();
        int minItem=0;
        for(int i=1;i<items.size();i++)
        {
            if(MSGSPMain.MS.get(items.get(i))<MSGSPMain.MS.get(items.get(minItem)))
            {
                minItem = i;
            }
        }
        return minItem;
    }

    public ExtSequence reverse()
    {
        ExtSequence revList=new ExtSequence();
        Seq revIS=new Seq();
        int i,j;

        int size=this.itemSets.size();
        for(i=0;i<size;i++)
        {
            Seq it=this.itemSets.get(size-i-1);
            int n = it.items.size();
            for(j=0;j<n;j++)
            {
                revIS.items.add(new Integer(it.items.get(n - j -1).intValue()));
            }
            revList.itemSets.add(revIS);
            revIS = new Seq();
        }
        return revList;
    }

    public boolean equals(Object tr)
    {
        ExtSequence trans = (ExtSequence) tr;
        boolean result = false;
        if (this.subsetOf(trans) && this.itemSets.size() == trans.itemSets.size() && this.getItems().size() == trans.getItems().size())
            result = true;
        return result;
    }

    public int hashCode() {
        int result = 0;
        for (Integer it : this.getItems())
            result += it.intValue();
        return result;
    }


    public void setCount(Integer _count){
        this.count = _count;
    }
}
