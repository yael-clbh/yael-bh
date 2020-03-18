//submitted by Noa Kohavi, username: noakohavi, ID: 314979113 & Clara Yael Benarroch, username: clarayaelb, ID: 205959158.

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap{

	private int numOfTrees;
	private int size;
	private HeapNode min;
	private int numOfMarked;
	private static int numOfCuts;
	private static int numOfLinks;
	//public int [] countTreesOfRank = new int [45];
	//private HashMap <Integer,Integer> countTreesOfRank = new HashMap <Integer,Integer> ();//saving in HashMap the number of trees of each rank
	private HeapNode first;
	
	
	public FibonacciHeap() {
		
	}
	
	public int getNumberOfTrees() {
		return this.numOfTrees;
	}
	
	public HeapNode getFirst() {
		return this.first;
	}

	
   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean isEmpty()
    {
    	if (this.first == null) {
    		return true;
    	}
    	return false; 
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
    */
    public HeapNode insert(int key)
    {
    	this.size +=1;
    	HeapNode inserted = new HeapNode(key); //creating the new node
    	
    	return addAtFirst(inserted);//inserting the node as a tree of rank 0 in the begining of the roots list
    }
    
    private HeapNode addAtFirst (HeapNode inserted) {
    	if (this.isEmpty()) { //make the node the only tree in the heap (therefore also its minimum)
    		this.first = inserted;
    		this.min = inserted;
    		inserted.nextSister = inserted;
    		inserted.prevSister = inserted;
    	}
    	else {
    		HeapNode temp = this.first.prevSister;
    		this.first.prevSister.nextSister = inserted;
    		this.first.prevSister = inserted;
    		inserted.nextSister = this.first;
    		inserted.prevSister = temp;
    		this.first = inserted;
    		if (inserted.key<this.min.key) {//update minimum
    			this.min = inserted;
    		}
    	}
    	this.numOfTrees +=1;
    	//this.updateCountTreesOfRank(inserted.getRank(), 1); //updating countTreesOfRank
    	return inserted;
    	
    	
    }
    
    
    //private void updateCountTreesOfRank (int rank, int k) {
    	//this.countTreesOfRank[rank] +=k;
    //}
    /*
	private void updateCountTreesOfRank(int rank, int k) {//updates the hash map which contains the number of trees in each rank
		if (this.countTreesOfRank.containsKey(rank)){
			this.countTreesOfRank.replace(rank, this.countTreesOfRank.get(rank)+k);//if there is already a tree at this rank
		}
		else {
			this.countTreesOfRank.put(rank, k);//if there is no tree of this rank
		}
	}
	*/
   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
    	if(this.isEmpty()) {//no need to delete
    		return;
    	}
    	else {
    	this.numOfTrees -=1;
    	this.size-=1;
    	HeapNode myMin = this.min;
    	if (myMin.daughter != null) {//the minimun we delete has children
    		if (myMin == this.first) {//if the minimum was also the first root in the list of the roots
    			this.first = myMin.daughter;//update the first to be its first daughter
    		}
    	
    		if (myMin.nextSister!=myMin) { //if the minimum was not the only node in the list (otherwise, its children will become the new list of roots (therefore the new heap)
    				this.insertRoots(myMin);//insert the children of the minimum as roots to the list of roots and remove the min from it.
    		}
    		deleteParent(this.first);//make the parents of the new roots (the min's children)- null.
    		}
    	else { //the minimum has no children (its a tree of rank 0)
    		if (this.first.nextSister == this.first ) {//if the minimum is the only root- the heap will become empty
    			this.first = null;
    			this.min = null;
    			this.numOfTrees = 0;
    			//this.countTreesOfRank = new int [45];
    			return;
    		}
    		else {
    			if (myMin == this.first) {//if the min is the first node
    				this.first = myMin.nextSister;//update first to be min's first daughter
    			}
    			//remove the min from the list of roots
    			myMin.prevSister.nextSister = myMin.nextSister;
    			myMin.nextSister.prevSister = myMin.prevSister;
    			
    		}	
    		}
    	//disconnect the other roots from the min root. 
    	myMin.prevSister=myMin;
    	myMin.nextSister=myMin;
    	}
    	
    	this.consolidation();//consolidate
     	return; 
     	
    }
    
    private void deleteParent(HeapNode x) {//changing the parents of x and its brothers to null  (we only call it when x and its brothers became roots)
    	HeapNode curr = x;
    	while (curr.mother != null) {//stops when it reaches a root whom it's parent is already null 
    		curr.mother = null;
    		this.numOfTrees ++;
    		if(curr.mark) {
    			this.numOfMarked-=1;
    		}
    		curr.mark=false;
    		curr = curr.nextSister;
    	}
    	}
    
    private void insertRoots(HeapNode myMin) {//inserts all myMin children as roots to the list of roots and delete disconnect myMin from it
    	myMin.prevSister.nextSister = myMin.daughter;
    	myMin.nextSister.prevSister = myMin.daughter.prevSister;
    	myMin.daughter.prevSister.nextSister = myMin.nextSister;
    	myMin.daughter.prevSister = myMin.prevSister;
    }
    
   private HeapNode [] toBuckets() {//implemented according to the successive linking shown in class

	HeapNode [] buckets = new HeapNode [(int)Math.floor(1.4404*Math.log(this.size)/Math.log(2))+1];//the number of buckets is the maximum rank in fibonacciHeap
	HeapNode myNode = this.first;
	do {
	HeapNode xNext = myNode.nextSister;
	HeapNode x=myNode;
	int k = x.rank;
   		while (buckets[k]!= null) {
   			HeapNode z = buckets[k];
   			if (z.key > x.key) {
   				link(z,x);
   			}
   			else {
   				link(x,z);
   				x=z;
   			}
   		
   			buckets[k] = null;
   			k+=1;
   		}

   		buckets[k] = x;
   		myNode=xNext;
 
   	}
   		while(myNode !=this.first);
   		
		return buckets;
   	}
   	
   private void fromBuckets(HeapNode [] buckets) {//links all the roots in the buckets to one list of roots
	   this.first = null;
	   this.min = null;
	   this.numOfTrees = 0;
	   //this.countTreesOfRank = new int [45];
	   HeapNode prevBucket = null;
	   for (int i =0; i<buckets.length;i++) {
		   HeapNode bucket = buckets[i];
		   if (bucket!= null) {
			   this.numOfTrees +=1;
			   //this.updateCountTreesOfRank(bucket.getRank(), 1);
			   if (this.first==null) {
				   this.first = bucket;
				   bucket.nextSister = bucket;
				   bucket.prevSister = bucket;
				   this.min = bucket;
				   prevBucket=bucket;
			   }
			   else {
				   prevBucket.nextSister = bucket;
				   bucket.prevSister = prevBucket;
				   prevBucket = bucket;
				   if (this.min.key>bucket.key) {//updating the minimum
					   this.min = bucket;
				   }
			   }
			   
		   }

	   }
	   this.first.prevSister = prevBucket;
	   prevBucket.nextSister = this.first;
   }
   
   private void consolidation() {
	   this.fromBuckets(this.toBuckets());   
   }
	private void link (HeapNode child,HeapNode mother) { //links to nodes of the same rank: child is the root with the larger key, it becomes a child of the mother root
  		child.prevSister=child;
  		child.nextSister=child;
  		if(mother.daughter!=null) {//if the node mother already has children, adding child as the first daughter
  			child.nextSister = mother.daughter;
  			child.prevSister = mother.daughter.prevSister;
  			mother.daughter.prevSister = child;
  			child.prevSister.nextSister = child;
  		}
  		child.mother = mother;
  		mother.daughter = child;
  		mother.rank = mother.rank + 1;
  		mother.prevSister=mother;
  		mother.nextSister=mother;
 		numOfLinks +=1;
	}
   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin()
    {
    	if(this.isEmpty()) { 
    		return null;
    	}
    	return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2){//adding all the roots of heap2 after the roots of the heap we called meld on    
    	this.first.prevSister.nextSister = heap2.first;
    	heap2.first.prevSister.nextSister = this.first;
    	HeapNode temp = heap2.first.prevSister;
    	heap2.first.prevSister = this.first.prevSister;
    	this.first.prevSister = temp;
    	
    	//update minimum
    	if (this.min.key > heap2.min.key) {
    		this.min = heap2.min;
    	}
    	this.numOfTrees += heap2.numOfTrees;
    	this.numOfMarked += heap2.numOfMarked;
    	this.size += heap2.size;
    	return;  		
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep()
    {
    	if (this.isEmpty()) {
    		return new int[0];
    	}
	int [] arr = new int [(int)Math.floor(1.4404*Math.log(this.size)/Math.log(2))+1];//create an array of the length of the maximal rank
	HeapNode curr = this.first;
	int lastIndex=0;
	do {
		lastIndex = curr.rank;
		arr[lastIndex] +=1;
		curr = curr.nextSister;
	}
	while (curr!=this.first);
	
	int [] ans = new int [lastIndex +1];
	for (int i = 0; i<ans.length; i++) {
		ans[i] = arr[i];
	}
    return ans;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) 
    {    
    	this.decreaseKey(x,x.key-this.min.key+1);//decrease the key so its smaller than the minimum (becomes the new minimum) 
    	this.deleteMin();//deletes the key
    	return; 
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {   
    	x.key-=delta;
    	if (x.mother!=null) {
    		if(x.key<x.mother.key) {//if x key is smaller then its mother cascading cuts are needed
    			this.cascadingCut(x, x.mother);
    		}
    	}
    	if (x.key<this.min.key) {//updating x to be the min if needed
    		this.min=x;
    	}
    	
    	return; 
    }
    private void cut(HeapNode child,HeapNode parent) {//cuts the child from it's parent and insert it to the roots' list
    	numOfCuts+=1;
    	child.mother=null;
    	if(child.mark) {
    		this.numOfMarked-=1;
    	}
    	child.mark=false;
    	parent.rank = parent.rank - 1;
    	//if (parent.mother==null) {
    		//this.updateCountTreesOfRank(parent.getRank()+1, -1);
    		//this.updateCountTreesOfRank(parent.getRank(), 1);

    	//}
    	if (child.nextSister==child) {//child was the only child of its parent
    		parent.daughter=null;
    	}
    	
    	else {//disconnect child from it's sisters
    		child.prevSister.nextSister=child.nextSister;
    		child.nextSister.prevSister=child.prevSister;
    	
    	if(parent.daughter==child) {//if child was the first daughter of parent
    		parent.daughter=child.nextSister;
    	}
    	}
    	this.addAtFirst(child);
    	//addAtFirst also updating numOfTrees and countTreesOfRank
    	
    }
    private void cascadingCut(HeapNode child,HeapNode parent) {//implemented according to cascading cuts showed in class
    	this.cut(child,parent);
    	if (parent.mother!=null) {//we did not get to one of the stop conditions- to the root
    		if(!parent.mark) {//if parent is not mark we mark it and stop- this is the other stop condition.
    			parent.mark=true;
    			this.numOfMarked+=1;
    		}
    		else {
    			this.cascadingCut(parent,parent.mother);//recursively- going up in the tree until reaching the stop condition 
    		}
    		
    	}
    	
    }
   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return (this.numOfTrees + 2*this.numOfMarked); 
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks()
    {    
    	return numOfLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return numOfCuts;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * The function should run in O(k(logk + deg(H)). 
    */
    public static int[] kMin(FibonacciHeap H, int k) {   
    	int[] kMinArray= new int[k];//create an array to which we will inserts the keys of the k minimal elements.
    	FibonacciHeap smallHeap=new FibonacciHeap(); //small heap formed in order to find k min element efficiently 
    	if(H.isEmpty() || k==0) {//should return an empty array
    		return kMinArray;
    	}
		HeapNode curr= H.findMin();					//curr is the original node in the binomial tree
		HeapNode temp = smallHeap.insert(curr.key); //temp is the node inserted with curr.key in the smallHeap 
		temp.checkNode = curr;
    	for (int i=0;i<k-1;i++) {	//in each iteration delete the minimum in the small heap and inserts its children from H heap to the small heap
    		curr=smallHeap.findMin();
    		kMinArray[i]=curr.getKey(); 
    		smallHeap.deleteMin();
    		smallHeap.insertToSmallHeap(curr.checkNode);

    	}
    	kMinArray[k-1]=smallHeap.findMin().key;//for the k elements- no need to inserts its children to the small heap
        return kMinArray; 
        
    }
    private void insertToSmallHeap(HeapNode parent) {//inserts the children of the parent from the original heap it belong to to the heap we called this method on
    	HeapNode temp = parent.daughter;
    	HeapNode firstChild = parent.daughter;
    	if(firstChild!=null) {
        	do {
        		HeapNode inserted = this.insert(temp.key); 
        		inserted.checkNode = temp; //inserted.checkNode is the pointer from the node in small heap to original node in binary tree H
        		temp = temp.nextSister;
        	}
        	while(temp!=firstChild);
    	}
    }
    
 
    /**
     * public class HeapNode
     * 
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in 
     * another file 
     *  
     */
    public class HeapNode{

    	public int key;
    	private HeapNode mother;
    	private HeapNode nextSister;
    	private HeapNode prevSister;
    	private HeapNode daughter;
    	private boolean mark = false;
    	private int rank = 0;
    	private HeapNode checkNode = null;//will be used only in the smallHeap created in the method kMin.

      	public HeapNode(int key) {
    	    this.key = key;
          }

      	public int getKey() {
    	    return this.key;
          }


    
}
}
