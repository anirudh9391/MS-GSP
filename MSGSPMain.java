
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;


public class MSGSPMain
{


    final String outputPath = "C:\\Users\\aniru\\MS-GSP\\src\\output.txt";


    //                                    Declaration of variables

    public static HashMap<Integer,Float> MS;
    public ArrayList<ExtSequence> S;
    public static int N;
    public static double SDC;
    public static HashMap<Integer, Integer> SUP = new HashMap<Integer, Integer>();


    //                                  Extracting data from input files (data.txt and para.txt)

    MSGSPMain(String paraFileName, String dataFileName)
    {
        MS=ReadFile.mis_file(paraFileName);
        S=ReadFile.transactions_file(dataFileName);
    }

    //                                   Provide the file path below

    public static void main(String[] args) throws IOException
    {
        String dataFilePath = "C:\\Users\\aniru\\MS-GSP\\src\\data.txt";
        String parameterFilePath = "C:\\Users\\aniru\\MS-GSP\\src\\para.txt";
        new MSGSPMain(parameterFilePath,dataFilePath).msgsp();
    }

    public void msgsp() throws IOException
    {
        //                                 Sorting M based on MIS values
        LinkedList<Integer> M=sort(MS);

        //                                 Inite-Pass function to generate L
        ArrayList<Integer> L= initPass(M);

        //                  Class which keeps track of sequences with their respective counts
        ArrayList<fSequence> F=new ArrayList<fSequence>();

        F.add(new fSequence());

        F.add(initPrune(L));

        MSCandidateGen lvln = new MSCandidateGen();
        Level2CandidateGen lvl2 = new Level2CandidateGen();

        //                               Main Loop of the Algorithm

        fSequence Fk_1;
        for(int k=2; !(Fk_1=F.get(k-1)).sequences.isEmpty(); k++)
        {
            fSequence Ck;
            if(k==2)
            {
                Ck = lvl2.level2CandidateGen(L); //Generating Candidates of length 2(C2)
            }
            else
            {
                Ck = lvln.candidateGen(Fk_1);    //Generating Candidates
            }

            //       To check the presence of generated Candidate sequence in Transaction

            for(int i=0;i<S.size();i++)
            {
                ExtSequence s = S.get(i);
                for(ExtSequence T: Ck.sequences)
                {
                    if(T.subsetOf(s))
                    {
                        T.count++;
                    }
                }
            }

            fSequence Fk=new fSequence();

            for(ExtSequence c: Ck.sequences) {

                if(c.count>=MS.get(c.getItems().get(c.minMISItem()))*N){

                    Fk.sequences.add(c);
                    Fk.setCount(c.count);

                }
            }
            F.add(Fk);
        }
        F.remove(F.size()-1);

        //                  Writing the generated sequence to a file

        File file = new File(outputPath);
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        int k = 0;
        while (++k < F.size()) {
            fSequence Fk = F.get(k);
            bw.write(k + "-Sequence :");
            bw.write("\n");
            for (ExtSequence tran : Fk.sequences) {
                int i;
                bw.write("              <"); // two tabs as indent
                for (Seq is : tran.itemSets) {
                    // bw.write("\n");
                    bw.write("{");
                    for (i = 0; i < is.items.size() - 1; i++) {  // print an element except the last item
                        bw.write(is.items.get(i) + ",");
                    }
                    bw.write(is.items.get(i) + "}"); //print the last item in an element
                }
                bw.write("> count: " + tran.count+"\n");
            }

            bw.write("\n\n");
            bw.write("=>The total number of " + k + "-Sequence = " + Fk.sequences.size());
            bw.write("\n\n");

        }
        bw.close();
    }



    //            Implementation of Init-Pass method to generate L

    public ArrayList<Integer> initPass(LinkedList<Integer> M)
    {
        ArrayList<Integer> L=new ArrayList<Integer>();
        Iterator<Integer> it = M.iterator();
        for (Integer i : MS.keySet()) {
            SUP.put(i, new Integer(0));
        }

        for (ExtSequence tran : S) {
            N++;
            HashSet<Integer> items = tran.getItemsAsSet();
            for (Integer id : items) {
                Integer count = SUP.get(id);
                SUP.put(id, new Integer(count.intValue() + 1));
            }
        }
        Integer minId = null;   //used to store the id of the first item who meets its MIS
        while (it.hasNext()) {  //find the first item who meets its MIS
            Integer itemId = it.next();
            if (SUP.get(itemId)*1.0/N >= MS.get(itemId).floatValue()) {
                minId = itemId;
                L.add(itemId);
                break;
            }
        }
        while (it.hasNext()) {  //find in the following items who meets item minId's MIS
            Integer itemId = it.next();
            if (SUP.get(itemId)*1.0/N >= MS.get(minId).floatValue()) {
                L.add(itemId);
            }
        }
        return L;
    }

    //                             Implementation of  Prune step
    public fSequence initPrune(ArrayList<Integer> L) {
        fSequence F1 = new fSequence();
        for (Integer itemId : L) {      //iterate all the items in L to find those who meets their own MIS
            if (SUP.get(itemId)*1.0/N >= MS.get(itemId).floatValue()) {
                //Create a 1-sequence, and add it to F1
                Seq is = new Seq();
                is.items.add(itemId);
                ExtSequence tran = new ExtSequence();
                tran.itemSets.add(is);
                tran.setCount(SUP.get(itemId));

                F1.addTransaction(tran);

            }
        }

        return F1;

    }

    //                  Implementation of the Sort method

    public LinkedList<Integer> sort(HashMap<Integer,Float> MS){
        LinkedList<Integer> M=new LinkedList<Integer>();
        for(Integer itemID: MS.keySet()){
            if(M.size()==0)
                M.add(itemID);
            else{
                int i=0;
                while(i<M.size()&&MS.get(M.get(i))<MS.get(itemID))
                    i++;

                M.add(i,itemID);
            }
        }

        return M;
    }





}