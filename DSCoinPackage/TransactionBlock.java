package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.*;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    Transaction[] trarr=new Transaction[t.length];
    for(int i=0;i<t.length;i++){
      trarr[i]=t[i];
      
    }
    this.trarray=trarr;
    this.previous=null;
    MerkleTree mt=new MerkleTree();
    mt.Build(this.trarray);
    this.Tree=mt;
    this.trsummary=this.Tree.rootnode.val;
    this.dgst=null;
    this.nonce=null;
    
  }

  public boolean checkTransaction (Transaction t) {
    TransactionBlock b1= t.coinsrc_block; 

    if(b1==null){
      return true;
      
    }

    boolean found =false;
    for(int i=0;i<b1.trarray.length;i++){
      if(t.coinID.compareTo(b1.trarray[i].coinID)==0 ){
        
        if(t.Source==b1.trarray[i].Destination){
          found =true;
          }

          
      }
    } 
    if(!found){
      return false;
    }
    TransactionBlock b2=this;      //current block
          //b2=b2.previous;     //we have to check blocks between cureent block and coinsrc

          while(b2!=b1){
            for(int j=0;j<b2.trarray.length;j++){
              if(t.coinID==b2.trarray[j].coinID && b2.trarray[j] !=t){
                return false;
              }
            }
            b2=b2.previous;
          }
    return true;
  }
}
