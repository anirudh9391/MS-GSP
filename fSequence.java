import java.util.HashSet;

public class fSequence
{
    //             To associate sequences with their respective counts

    public HashSet<ExtSequence> sequences = new HashSet<>();
    Integer count;
    fSequence()
    {
        sequences=new HashSet<>();
        count = 0;
    }
    public void addTransaction(ExtSequence transObj)
    {
        sequences.add(transObj);
    }
    public void addFrequentSequence(fSequence freq)
    {
        sequences.addAll(freq.sequences);
    }
    public void setCount(Integer count)
    {
        this.count = count;
    }

}
