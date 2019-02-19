import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class ReadFile
{

    public static int min(int a, int b)
    {
        if(b<0)
        {
            return a;
        }
        return a<b?a:b;
    }
    public static HashMap<Integer,Float> mis_file(String path){
        HashMap<Integer,Float> mis=new HashMap<Integer,Float>();
        try{
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line="";
            while((line=br.readLine())!=null)
            {
                //                           To extract the SDC value
                if(line.charAt(0)=='S')
                {
                    int eq_pos = 0;
                    eq_pos = line.indexOf('=');

                    MSGSPMain.SDC = Float.valueOf(line.substring(eq_pos + 2));
                    break;
                }
                int op_pos = line.indexOf('(');
                int cl_pos = line.indexOf(')');
                Integer itemID=Integer.valueOf(line.substring(op_pos+1, cl_pos));

                int eq_pos = 0;
                eq_pos = line.indexOf('=');

                //                            To extract the MIS value

                Float itemMIS=Float.valueOf(line.substring(eq_pos+2));
                mis.put(itemID,itemMIS);

            }
            return mis;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }


    public static ArrayList<ExtSequence> transactions_file(String filename){
        ArrayList<ExtSequence> transObj=new ArrayList<ExtSequence>();
        try{
            File file = new File(filename);
            BufferedReader br=new BufferedReader(new FileReader(file));

            String line="";
            while((line = br.readLine()) != null)
            {
                ExtSequence transaction=new ExtSequence();
                //keep track of next open parenthesis in current line
                int op_pos=line.indexOf('{');
                //keep track of next closed parenthesis in current line
                int cl_pos=line.indexOf('}');

                while(op_pos<line.length()-1){
                    Seq is=new Seq();

                    int comm_pos=line.indexOf(',',op_pos);

                    int NumI=op_pos+1;

                    int NumJ=min(cl_pos,comm_pos);

                    while(NumJ<=cl_pos)
                    {
                        is.items.add(Integer.valueOf(line.substring(NumI,NumJ).trim())); // add an item to the itemset
                        if(NumJ==cl_pos) //reach the end of the itemset
                            break;
                        NumI=comm_pos+1;
                        comm_pos=line.indexOf(',',NumI);
                        NumJ=min(cl_pos,comm_pos);
                    }


                    op_pos=cl_pos+1;
                    cl_pos=line.indexOf('}',op_pos);
                    transaction.itemSets.add(is); // append the transaction to the transaction set
                }

                transObj.add(transaction);

            }
            return transObj;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }





}


