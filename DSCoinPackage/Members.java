package DSCoinPackage;

import java.util.*;
import HelperClasses.*;

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
    if(this.in_process_trans==null){
		this.in_process_trans = new Transaction[100];
	  }
    Pair <String,TransactionBlock> fir=mycoins.get(0);
    mycoins.remove(0);
    Transaction tobj=new Transaction();
    tobj.coinID=fir.first;
    tobj.coinsrc_block=fir.second;
    tobj.Source= this ;           /////////////////
    for(int i=0;i<DSobj.memberlist.length;i++){
      if(DSobj.memberlist[i].UID==destUID){
        tobj.Destination= DSobj.memberlist[i];
        break;
      }
    }
    //=============================================
    //DSCoin_Honest DSobj = new DSCoin();
    DSobj.pendingTransactions.AddTransactions(tobj);
    for(int i=0;i<in_process_trans.length;i++){
      if(in_process_trans[i]==null){
        in_process_trans[i]=tobj;
        break;
      }
    }}


  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
    TransactionBlock tB=DSObj.bChain.lastBlock;   //to traverse 
    //TransactionBlock tB =new TransactionBlock();
    boolean found=false;
    int k=0;

    while(tB!=null && found ==false){

      for(int i=0;i<tB.trarray.length;i++){
        if(tB.trarray[i]==tobj){
          //tB=tb2;
          k=i;
          found=true;
          break;
        }  
      }
      if(found){break;}  
      tB=tB.previous;
    }  

    if(!found){//here tb is null
      throw new MissingTransactionException() ;
    } 
    //how to specify exception
    //found tb
    //get path
    List <Pair<String,String>> path= new ArrayList<Pair<String,String>>();
    MerkleTree mt= tB.Tree;
    
   
    TreeNode tn= mt.rootnode;
    Pair<String,String> p=new Pair<String,String>(mt.rootnode.val,null);
		path.add(0,p);
    int num=tB.trarray.length;
    int len=2;
    while(len<=num){
		//Pair<String,String>n=new Pair<String,String>();		
		if(k<=num/len){
      Pair<String,String>n=new Pair<String,String>(tn.left.val,tn.right.val);		
			// n.first=tn.left.val;
			// n.second=tn.right.val;
			tn=tn.left;							//tn vo node hai jiske child dekhne hai
			len=len*2;
			path.add(0,n);
		}
		else{
      Pair<String,String>n=new Pair<String,String>(tn.left.val,tn.right.val);
			// n.first=tn.left.val;
			// n.second=tn.right.val;
			tn=tn.right;
			path.add(0,n);
			k=k-num/len;
			len=len*2;
		}
		}

    //get list2;
    List <Pair<String,String>> list1= new ArrayList<Pair<String,String>>();
    List <Pair<String,String>> list2= new ArrayList<Pair<String,String>>();
    String dig;
    if(tB.previous==null){
      dig="DSCoin";
    }
    else{
      dig =tB.previous.dgst;
    }
    Pair<String,String> node=new Pair<String,String>(dig,null);
    list1.add(0,node);
    //tB=tB.previous;

    TransactionBlock tb2=DSObj.bChain.lastBlock;   //to traverse

    //what if tb  is null
    while(tb2!=tB){
      Pair<String,String> n=new Pair<String,String>(tb2.dgst,tb2.previous.dgst+"#"+tb2.trsummary+"#"+tb2.nonce);
      list2.add(n);
      tb2=tb2.previous;
    }
    for(int i=list2.size()-1;i>=0;i--){
      list1.add(list2.get(i));
    }
    


    Pair<List<Pair<String, String>>, List<Pair<String, String>>> ans= new Pair<List<Pair<String, String>>, List<Pair<String, String>>> (list1,path);
    // ans.second=list1;
    // ans.first=path;
    
    //delete from 
    int i=0;
    while(in_process_trans[i]!=null){
      if(in_process_trans[i]==tobj){
        in_process_trans[i]=null;
        break;
      }
      else{
        i++;
      }
      
    }
    boolean added=false;
    for(int m=0;m<tobj.Destination.mycoins.size();m++){
      if(tobj.Destination.mycoins.get(m).first.compareTo(tobj.coinID)>0){
        tobj.Destination.mycoins.add(m,new Pair<String, TransactionBlock>(tobj.coinID,tB));
        added =true;
      }
    }
    if(!added){
      tobj.Destination.mycoins.add(new Pair<String, TransactionBlock>(tobj.coinID,tB));
    }
    //in_process_trans.RemoveTransaction(tobj);
    

    return ans;
  }

  public void MineCoin(DSCoin_Honest DSObj) {
    Transaction tran =DSObj.pendingTransactions.firstTransaction;
    int size=0;
    Transaction[] array = new Transaction[DSObj.bChain.tr_count];
    //to store the valid transactions in array
    while(size<DSObj.bChain.tr_count -1){
    //while(tran!=null){//not at lastt ===========================================================tran.next!=null?
    try{
        Transaction curr=DSObj.pendingTransactions.RemoveTransaction();
    //if(DSObj.bChain.lastBlock.checkTransaction(tran)){
      if(size==0){
        
        if(DSObj.bChain.lastBlock.checkTransaction(curr)){
            array[size]=curr;
        }
        size+=1;
      }
      else{
        boolean found=false;
        if( DSObj.bChain.lastBlock.checkTransaction(curr)){
        for(int i=0;i<size;i++){
          if(array[i].coinID.compareTo(tran.coinID)==0){
            DSObj.pendingTransactions.RemoveTransaction();
           
            found=true;}}
          }
        if(!found ){
          size+=1;
          array[size]=curr;
          }
          }}
  
    catch(EmptyQueueException e){
      return;
    }
    }
    Transaction minerRewardTransaction=new Transaction();
    minerRewardTransaction.Source=null;
    minerRewardTransaction.Destination=this;


    String newCoinID = String.valueOf(Integer.parseInt(DSObj.latestCoinID)+1);
    DSObj.latestCoinID=newCoinID;   //incremented latestcoinid
    minerRewardTransaction.coinID=newCoinID;
    minerRewardTransaction.coinsrc_block=null;

    // minerRewardTransaction.coinID=null;
    array[DSObj.bChain.tr_count-1]=minerRewardTransaction;

    TransactionBlock tB= new TransactionBlock(array);//create trans block

    DSObj.bChain.InsertBlock_Honest(tB); //insert block


  
    this.mycoins.add(new Pair<String, TransactionBlock>(minerRewardTransaction.coinID,tB));
    DSObj.latestCoinID = minerRewardTransaction.coinID;
 }


    //minerRewardTransaction.coinID=newCoinID;
 

  public void MineCoin(DSCoin_Malicious DSObj) {
    
    Transaction tran =DSObj.pendingTransactions.firstTransaction;
    int size=0;
    Transaction[] array = new Transaction[DSObj.bChain.tr_count];
    //to store the valid transactions in array
    while(size<DSObj.bChain.tr_count -1){
    //while(tran!=null){//not at lastt ===========================================================tran.next!=null?
    try{
        Transaction curr=DSObj.pendingTransactions.RemoveTransaction();
    //if(DSObj.bChain.lastBlock.checkTransaction(tran)){
      if(size==0){
        
        if(DSObj.bChain.FindLongestValidChain().checkTransaction(curr)){
            array[size]=curr;
        }
        size+=1;
      }
      else{
        boolean found=false;
        if( DSObj.bChain.FindLongestValidChain().checkTransaction(curr)){
        for(int i=0;i<size;i++){
          if(array[i].coinID.compareTo(tran.coinID)==0){
            DSObj.pendingTransactions.RemoveTransaction();
           
            found=true;}}
          }
        if(!found ){
          size+=1;
          array[size]=curr;
          }
          }}
  
    catch(EmptyQueueException e){
      return;
    }
    }
    Transaction minerRewardTransaction=new Transaction();
    minerRewardTransaction.Source=null;
    minerRewardTransaction.Destination=this;


    String newCoinID = String.valueOf(Integer.parseInt(DSObj.latestCoinID)+1);
    DSObj.latestCoinID=newCoinID;   //incremented latestcoinid
    minerRewardTransaction.coinID=newCoinID;
    minerRewardTransaction.coinsrc_block=null;

    // minerRewardTransaction.coinID=null;
    array[DSObj.bChain.tr_count-1]=minerRewardTransaction;

    TransactionBlock tB= new TransactionBlock(array);//create trans block

    DSObj.bChain.InsertBlock_Malicious(tB); //insert block 
    this.mycoins.add(new Pair<String, TransactionBlock>(minerRewardTransaction.coinID,tB));
    DSObj.latestCoinID = minerRewardTransaction.coinID;
    //minerRewardTransaction.coinID=newCoinID;
  }  
}
