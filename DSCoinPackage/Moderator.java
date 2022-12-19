package DSCoinPackage;
import java.util.Arrays;

import HelperClasses.*;

public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    Transaction[] tarr=new Transaction[coinCount];
    
    if(DSObj.latestCoinID==null){
      DSObj.latestCoinID="100000";
    }
    Members m=new Members();
    m.UID="Moderator";

    for(int i=0;i<coinCount;i++){
      Transaction tran=new Transaction();
      tran.Source=m;
      tran.coinsrc_block=null;
      tran.coinID=DSObj.latestCoinID;
      tran.Destination=DSObj.memberlist[i%DSObj.memberlist.length];
     // Pair<String, TransactionBlock> p=new Pair<String, TransactionBlock> (DSObj.latestCoinID,null);

     // DSObj.memberlist[i%DSObj.memberlist.length].mycoins.add(p);
      DSObj.latestCoinID=Integer.toString(1+Integer.parseInt(DSObj.latestCoinID));
      tarr[i]=tran;
    }
    int trcount=DSObj.bChain.tr_count;
    for(int i=trcount;i<=coinCount;i+=trcount){

      TransactionBlock tblock=new TransactionBlock(Arrays.copyOfRange(tarr,i-trcount,i));
      BlockChain_Honest bchain =DSObj.bChain ;
      bchain.InsertBlock_Honest(tblock);
      
      for(int j=0;j<tblock.trarray.length;j++){
      
        boolean b=false;
        Pair<String, TransactionBlock> p=new Pair<String, TransactionBlock> (tblock.trarray[j].coinID,tblock);
        for(int k=0;k<tblock.trarray[j].Destination.mycoins.size();k++){
          if(tblock.trarray[j].coinID.compareTo(tblock.trarray[j].Destination.mycoins.get(k).first)<0){
            tblock.trarray[j].Destination.mycoins.add(k,p);
            b=true;
            break;
          }
         
        }
        if(!b){
          tblock.trarray[j].Destination.mycoins.add(p);
        }
      }

      

      
    }

  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) { Transaction[] tarr=new Transaction[coinCount];
    
    if(DSObj.latestCoinID==null){
      DSObj.latestCoinID="100000";
    }
    Members m=new Members();
    m.UID="Moderator";

    for(int i=0;i<coinCount;i++){
      Transaction tran=new Transaction();
      tran.Source=m;
      tran.coinsrc_block=null;
      tran.coinID=DSObj.latestCoinID;
      tran.Destination=DSObj.memberlist[i%DSObj.memberlist.length];
     // Pair<String, TransactionBlock> p=new Pair<String, TransactionBlock> (DSObj.latestCoinID,null);

     // DSObj.memberlist[i%DSObj.memberlist.length].mycoins.add(p);
      DSObj.latestCoinID=Integer.toString(1+Integer.parseInt(DSObj.latestCoinID));
      tarr[i]=tran;
    }
    int trcount=DSObj.bChain.tr_count;
    for(int i=trcount;i<=coinCount;i+=trcount){

      TransactionBlock tblock=new TransactionBlock(Arrays.copyOfRange(tarr,i-trcount,i));
      BlockChain_Malicious bchain =DSObj.bChain;
      bchain.InsertBlock_Malicious(tblock);
      
      for(int j=0;j<tblock.trarray.length;j++){
      
        boolean b=false;
        Pair<String, TransactionBlock> p=new Pair<String, TransactionBlock> (tblock.trarray[j].coinID,tblock);
        for(int k=0;k<tblock.trarray[j].Destination.mycoins.size();k++){
          if(tblock.trarray[j].coinID.compareTo(tblock.trarray[j].Destination.mycoins.get(k).first)<0){
            tblock.trarray[j].Destination.mycoins.add(k,p);
            b=true;
            break;
          }
         
        }
        if(!b){
          tblock.trarray[j].Destination.mycoins.add(p);
        }
        //tblock.trarray[j].Destination.mycoins.add(p);
      }

      

      
    }

  }
 }
