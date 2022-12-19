package DSCoinPackage;

import HelperClasses.*;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;
  

  public void InsertBlock_Honest (TransactionBlock newBlock) {
    CRF obj = new CRF(64);
    newBlock.previous=lastBlock;
    long c=1000000001L;
    String digest;
    
    //set nonce
    if(lastBlock==null){
      digest=start_string;

    }
    else{
      digest=lastBlock.dgst;
    }
    
    String s=""+c;
    newBlock.nonce=s;
    while(obj.Fn(digest+"#"+newBlock.trsummary+"#"+newBlock.nonce).substring(0,4).compareTo("0000") !=0 ){
      c+=1;
      newBlock.nonce=""+c;
    }
    //set dgst
    newBlock.dgst=obj.Fn(digest+"#"+newBlock.trsummary+"#"+newBlock.nonce);
    lastBlock=newBlock;
    

  }
}
