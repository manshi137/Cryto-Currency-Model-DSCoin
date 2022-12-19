package DSCoinPackage;

//import HelperClasses.CRF;
import HelperClasses.*;


public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {

    CRF obj = new CRF(64);
    String prevdgst;
    if(tB.previous==null){
      prevdgst=start_string;
    }
    else{
      prevdgst=tB.previous.dgst;  
    }

    if(tB.dgst.substring(0,4).compareTo("0000")!=0){
      return false;
    }
    else{
      
      if(tB.dgst.compareTo(obj.Fn(prevdgst+"#"+tB.trsummary+"#"+tB.nonce))!=0){
        return false;
      }
      else{
        //check trsummary= biuld merkletree and match tb.trsummary to its rootnode
        //====
        MerkleTree mt = new MerkleTree();
        if(tB.trsummary.compareTo(mt.Build(tB.trarray))!=0){
          return false;
        }
        //trsummary verified
        
        for(int i=0;i<tB.trarray.length;i++){
          if(!tB.checkTransaction(tB.trarray[i])){
            return false;
          }
        }

       
      }
    } 
    return true; 
  }

  public TransactionBlock FindLongestValidChain () {
    TransactionBlock tb;
    int maximum;
    for(int i=0;i<this.lastBlocksList.length;i++){
      TransactionBlock tb_iterator = this.lastBlocksList[i];
      int max =0;
      boolean blah=false;
      TransactionBlock temp =null;
      while(tb_iterator!=null){
        if (!blah){
          temp = tb_iterator;
        }
        if(this.checkTransactionBlock(tb_iterator)){
          max ++;
          blah = true;
        }
        else{
          max = 0;
          blah = false;
        }
        tb_iterator= tb_iterator.previous;
      }
      if(maximum < max ){
        maximum = max;
        tb = temp;
      }
    }
    return tb;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock lastBlock=this.FindLongestValidChain();
    newBlock.previous=lastBlock;
    CRF obj=new CRF(64);
    String digest;
    if(lastBlock==null){
      digest=start_string;

    }
    else{
      digest=lastBlock.dgst;
    }
    
    long c=1000000001L;
    String s=""+c;
    newBlock.nonce=s;
    //String.valueOf(c)
    while(obj.Fn(digest+"#"+newBlock.trsummary+"#"+newBlock.nonce).substring(0,4).compareTo("0000")!=0){
      c+=1;
      newBlock.nonce=""+c;
      //newBlock.nonce=Integer.toString(c);
    }
    

    newBlock.dgst=obj.Fn(digest+"#"+newBlock.trsummary+"#"+newBlock.nonce);
    lastBlock=newBlock;
    boolean bool=false;

    for(int i=0;i<lastBlocksList.length;i++){
      if(lastBlocksList[i]==newBlock.previous){
        lastBlocksList[i]=newBlock;
        bool=true;
        break;
      }
    }

    if(!bool){
      for(int i=0;i<lastBlocksList.length;i++){
        if(lastBlocksList[i]==null){
          lastBlocksList[i]=newBlock;
        }
      }
    }
    
  }
}
