

public class MSCandidateGen
{

    public fSequence candidateGen(fSequence F)
    {
        fSequence C = new fSequence();
        
        for (ExtSequence fseq1 : F.sequences)
        {
            for (ExtSequence fseq2 : F.sequences)
            {
                ExtSequence T1 = fseq1.replicate();
                ExtSequence T2= fseq2.replicate();
                int condition = checkCondition(T1, T2);
                if (condition != 0)
                {
                    C.addFrequentSequence(joinStep(T1, T2, condition));
                }
            }
        }
        return prune(C,F);
    }

    private boolean isFrequent(ExtSequence T, fSequence Fk)
    {
        boolean is_Freq=false;
        for(ExtSequence freq: Fk.sequences)
            if(T.subsetOf(freq))
            {
                is_Freq=true; //there is one sequence in F(k-1) includes the subsequence
                break;
            }
        return is_Freq;
    }

    private int checkCondition(ExtSequence T1, ExtSequence T2)
    {
        int result = 0;
       
        int i = splice(T1); 
        if(MISgen(T1, T2, i) == true)
        {
            result = i;
        }
        else if(MISgen(T1, T2, 3) == true)
        {
            result = 3;
        }
        return result;
    }
    /*
     * This method partitions the k-1 length frequent sequence F into three
     * subset s1, s2, and s3.
     * s1 is those whose first item has the minimum MIS.
     * s2 is those whose last item has the minimum MIS.
     * s3 is the rest.
     */
    private int splice(ExtSequence transObj)
    {
        int result = 0;
        Integer firstItem = transObj.getFirstItem();
        Integer lastItem = transObj.getLastItem();
        if (transObj.isMin(firstItem, 0))
        {
            result = 1;
        }
        else if (transObj.isMin(lastItem, 1))
        {
            result = 2;
        }
        return result;
    }


    private boolean MISgen(ExtSequence transObj, ExtSequence transObj1, int i)
    {
        boolean result = false;
        switch (i)
        {
            case 1:
                if (transObj.subsetCompare(transObj1, 1, transObj1.getItems().size()-1) &&
                        MSGSPMain.MS.get(transObj.getFirstItem()).doubleValue() < MSGSPMain.MS.get(transObj1.getLastItem()).doubleValue() &&
                        Math.abs(MSGSPMain.SUP.get(transObj.getItems().get(1)).intValue() - MSGSPMain.SUP.get(transObj1.getLastItem()).intValue()) <= MSGSPMain.SDC)
                    result = true; // If last item has the least MIS, checking if a sequence has its MIS greater than the least last element
                break;
            case 2:
                if (transObj.subsetCompare(transObj1, transObj.getItems().size()-2, 0) &&
                        MSGSPMain.MS.get(transObj1.getFirstItem()).doubleValue() > MSGSPMain.MS.get(transObj.getLastItem()).doubleValue() &&
                        Math.abs(MSGSPMain.SUP.get(transObj.getItems().get(transObj.getItems().size()-2)).intValue() - MSGSPMain.SUP.get(transObj1.getFirstItem()).intValue()) <= MSGSPMain.SDC)
                    result = true;
                break;
            case 3:
                if (transObj.subsetCompare(transObj1, 0, transObj1.getItems().size()-1) &&
                        Math.abs(MSGSPMain.SUP.get(transObj.getFirstItem()).intValue() - MSGSPMain.SUP.get(transObj1.getLastItem()).intValue()) <= MSGSPMain.SDC)
                    result = true;
                break;
        }
        return result;
    }


    private fSequence joinStep(ExtSequence transObj, ExtSequence transObj1, int i)
    {
        fSequence fs = new fSequence();
        ExtSequence candidate = new ExtSequence();
        switch(i)
        {
            case 1:
                ExtSequence td = transObj.replicate();
                if (transObj1.itemSets.get(transObj1.itemSets.size()-1).items.size() == 1) {
                    candidate = new ExtSequence();
                    candidate.itemSets.addAll(td.itemSets);
                    candidate.itemSets.add(transObj1.itemSets.get(transObj1.itemSets.size()-1));
                    fs.addTransaction(candidate);
                    if (td.itemSets.size()==2 && td.getItems().size()==2 && td.getLastItem().toString().compareTo(transObj1.getLastItem().toString()) < 0) // if the sequence size is 2 and the last elements are not equal
                    {
                        candidate = new ExtSequence();
                        candidate.itemSets.addAll(td.replicate().itemSets); // creating a reference to a different instance of same object
                        candidate.itemSets.get(candidate.itemSets.size()-1).items.add(transObj1.replicate().getLastItem());// a succesful join step. if the last element is not the same, it is added
                        fs.addTransaction(candidate);
                    }
                }

                else if (td.getItems().size() > 2 ||(td.itemSets.size()==1 && td.getItems().size()==2 && td.getLastItem().toString().compareTo(transObj1.getLastItem().toString()) < 0)) //comparing last items of the sequence
                {
                    candidate = new ExtSequence();
                    candidate.itemSets.addAll(td.itemSets);
                    candidate.itemSets.get(candidate.itemSets.size()-1).items.add(transObj1.getLastItem()); // it is joined if the minimum ast item is not matched in other sequence
                    fs.addTransaction(candidate);
                }

                break;
            case 2:
                td = transObj.replicate();
                if (transObj1.reverse().itemSets.get(transObj1.reverse().itemSets.size()-1).items.size() == 1)
                {
                    candidate = new ExtSequence();
                    candidate.itemSets.addAll(td.reverse().itemSets);
                    candidate.itemSets.add(transObj1.reverse().itemSets.get(transObj1.reverse().itemSets.size()-1));
                    fs.addTransaction(candidate.reverse());
                    if (td.reverse().itemSets.size()==2 && td.reverse().getItems().size()==2 && td.reverse().getLastItem().toString().compareTo(transObj1.reverse().getLastItem().toString()) < 0) //incase first item is minimum, check the first
                    {
                        candidate = new ExtSequence();
                        candidate.itemSets.addAll(td.replicate().reverse().itemSets);
                        candidate.itemSets.get(candidate.itemSets.size()-1).items.add(transObj1.replicate().reverse().getLastItem());
                        fs.addTransaction(candidate.reverse()); // it is pruned
                    }
                }
                else if (td.reverse().getItems().size() > 2 ||(td.reverse().itemSets.size()==1 && td.reverse().getItems().size()==2 && td.reverse().getLastItem().toString().compareTo(transObj1.reverse().getLastItem().toString()) < 0)) {
                    candidate = new ExtSequence();
                    candidate.itemSets.addAll(td.reverse().itemSets);
                    candidate.itemSets.get(candidate.itemSets.size()-1).items.add(transObj1.reverse().getLastItem());
                    fs.addTransaction(candidate.reverse());
                }
                break;
            case 3:
                td = transObj.replicate();
                if (transObj1.itemSets.get(transObj1.itemSets.size()-1).items.size() == 1) {
                    candidate = new ExtSequence();
                    candidate.itemSets.addAll(td.itemSets);
                    candidate.itemSets.add(transObj1.itemSets.get(transObj1.itemSets.size()-1));
                    fs.addTransaction(candidate);
                }
                else { // this case with general joining with no special cases of MIS in first or last

                    candidate = new ExtSequence();
                    candidate.itemSets.addAll(td.itemSets);
                    candidate.itemSets.get(candidate.itemSets.size()-1).items.add(transObj1.getLastItem());
                    fs.addTransaction(candidate); // it is joined
                }

                break;
        }
        return fs;
    }


    private fSequence prune(fSequence fs, fSequence fk_1)
    {
        Integer minItem;
        fSequence fsPruned=new fSequence();
        for(ExtSequence t: fs.sequences)
        {
            minItem=new Integer(t.minMISItem());
            boolean frequent=true; // if the sequence is is frequent or not
            for(int i=0;i<t.itemSets.size();i++){ //iterating through itemsets
                if(t.itemSets.get(i).items.contains(minItem)){ //  if the itemset contains the minimum MIS element
                    ExtSequence copy=t.replicate(); // create a copy
                    for(int k=0;k<t.itemSets.size();k++)
                    {
                        if(!frequent) // if it is not frequent
                            break;
                        for(Integer item: t.itemSets.get(k).items)
                        {
                            if(!(k==i&&item==minItem))
                            { //apart from the minItem
                                copy.itemSets.get(k).items.remove(item); // generate a sequence of k-1, a copy of the sequence and -1 is done
                                if(!isFrequent(copy, fk_1)) // Downward closure property, if k-1 not frequent, k is not
                                {
                                    frequent=false;
                                    break;
                                }
                            }
                        }
                    }
                    if(!frequent)// if a sequence is already infrequent the no need to continue check the remaining itemsets
                        break;
                }
            }
            if(frequent) //if this sequence is frequent, add to the result
                fsPruned.sequences.add(t);
        }
        return fsPruned;
    }
}
