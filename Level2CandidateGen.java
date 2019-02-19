import java.util.ArrayList;

public class Level2CandidateGen
{

    public fSequence level2CandidateGen(ArrayList<Integer> L)
    {
        fSequence C2 = new fSequence();

        for (int i=0; i < L.size(); i++) {
            if (MSGSPMain.SUP.get(L.get(i))*1.0/MSGSPMain.N > MSGSPMain.MS.get(L.get(i)).floatValue())
            {

                for (int j=i; j < L.size(); j++)
                {
                    if (MSGSPMain.SUP.get(L.get(j))*1.0/MSGSPMain.N >= MSGSPMain.MS.get(L.get(i)).floatValue())
                    {
                        // For C2, we have L, from which we take the MIS of the least of the 2 involved items
                        // if (x,y) is a pair, we check if it is lesser than least of the two(which is first as it is sorted)

                        if (Math.abs(MSGSPMain.SUP.get(L.get(i)).intValue() - MSGSPMain.SUP.get(L.get(j)).intValue()) <= MSGSPMain.SDC)
                        {
                            Seq itemObj = new Seq();

                            itemObj.items.add(L.get(i));
                            itemObj.items.add(L.get(j));//{L(i), L(j)}

                            if (! L.get(i).equals(L.get(j)))
                            {
                                ExtSequence transObj = new ExtSequence();
                                transObj.itemSets.add(itemObj);
                                C2.addTransaction(transObj);
                            }

                            Seq Li = new Seq();
                            Li.items.add(L.get(i));
                            Seq Lj = new Seq();
                            Lj.items.add(L.get(j)); // {x},{y} to get it seperately
                            ExtSequence sepObj = new ExtSequence();
                            sepObj.itemSets.add(Li);
                            sepObj.itemSets.add(Lj);
                            C2.addTransaction(sepObj);       //tran2 is <{a}, {b}>

                            Li = new Seq();
                            Li.items.add(L.get(i)); // {y}{x}
                            Lj = new Seq();
                            Lj.items.add(L.get(j));
                            if (! L.get(j).equals(L.get(i)))
                            {
                                ExtSequence sepObj1 = new ExtSequence();
                                sepObj1.itemSets.add(Lj);
                                sepObj1.itemSets.add(Li);
                                C2.addTransaction(sepObj1);   // in an instance of C2,  ({x,y}) ({x},{y}),({y},{x})
                            }
                        }
                    }
                }
            }
        }

        return C2;
    }
}
