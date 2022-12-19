package DSCoinPackage;
import HelperClasses.*;
public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
    if(numTransactions==0){
      numTransactions=1;
      firstTransaction=transaction;
      lastTransaction=transaction;

    }
    else{  //this.enqueue(t)
    this.lastTransaction.next=transaction;
    this.lastTransaction=transaction;
    this.numTransactions+=1;
  }}
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    Transaction x;
     if(firstTransaction==lastTransaction){
       x= firstTransaction;
        firstTransaction=null;
        lastTransaction=null;
        numTransactions=0;

      }
    else if(this.firstTransaction!=null){
      x= firstTransaction;
      //this.dequeue();
      firstTransaction=firstTransaction.next;   ///////////////////////
      this.numTransactions-=1;
      return x;
      }
    else{
      throw new EmptyQueueException(); }
      return x;
    
  }

  public int size() {
    return this.numTransactions;
  }
}