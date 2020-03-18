import java.util.Arrays;


/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {
	
	private IAVLNode root = new AVLNode(-1,null, true);
	private IAVLNode min = new AVLNode(-1,null, true) ;
	public IAVLNode max = new AVLNode(-1,null, true);

	
	
	public AVLTree() {//complexity = O(1)
	}

  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() { //complexity = O(1)
	  if (this.root.getKey() == -1) {//root is null if and only if the tree is empty
		  return true;
	  }
    return false; 
  }
  
  
  private IAVLNode findPlace(int k) //complexity = O(log(n))
  {
	  IAVLNode curr = root;
	  IAVLNode x=null;
	  while (curr != null && curr.getKey() != -1){
		  if (k == curr.getKey()){ // if node with key 'k' is already in tree
			  return (curr);
		  }
		  else if (k<curr.getKey()) {
			  x= curr;				//save a pointer to the parent
			  curr = curr.getLeft();
		  }
		  else {
			  x=curr; 				//save a pointer to the parent
			  curr=curr.getRight();
		  }  
		  }
	return x;  //if key is not in tree, x is the last node we visited
  }
 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k) //complexity = O(log(n))
  {
	if (empty()) {
		return null;
	}
	IAVLNode node= this.findPlace(k);//if node with key k exists- find place returns it. else, returns the last node visited while searching for it. 
	if (node.getKey() == k) {
		return node.getValue();
	}
	else {
		return null;
	}
  }
  
  private int [] computeRanksDiff (IAVLNode nodeCheck) { //complexity = O(1)
	   int rightDiff = nodeCheck.getHeight() - nodeCheck.getRight().getHeight();
		  int leftDiff = nodeCheck.getHeight() - nodeCheck.getLeft().getHeight();
		  int [] ranksDiff = new int [] {leftDiff,rightDiff};
		  return (ranksDiff);
  }
  
  
  private void updateSize(IAVLNode node, int i) {//complexity = O(log(n)-node.getHeight()) = O(log(n))
	   IAVLNode parent = node.getParent();
	   while (parent != null) {
		   parent.setSize(parent.getSize() +i);   
		   parent = parent.getParent();
	   }
  }
  

  private void rotateRight (IAVLNode parent, IAVLNode child) {//complexity = O(1)
	   boolean is_parent_root = (parent.getParent() == null);
	   parent.setSize(parent.getSize()-child.getLeft().getSize()-1);
	   child.setSize(child.getSize()+1+parent.getRight().getSize());
	   parent.setLeft(child.getRight());
	   child.setRight(parent);
	   child.setParent(parent.getParent());
	   parent.setParent(child);
	   parent.getLeft().setParent(parent);
	   if (is_parent_root) { // if the rotation changes the root
		   root = child;
	   }
	   else{//change of pointers if the rotation doesn't change the root
		   if (child.getParent().getKey()>child.getKey()) { 
			   child.getParent().setLeft(child);
	   }
	   else {
		   child.getParent().setRight(child);
	   }
	 }
  }
  
  private void rotateLeft (IAVLNode parent, IAVLNode child) {//complexity = O(1)
	   //boolean is_parent_root = (parent.getKey() == root.getKey());
	   boolean is_parent_root = (parent.getParent() == null);
	   parent.setSize(parent.getSize()-child.getRight().getSize()-1);
	   child.setSize(child.getSize()+1+parent.getLeft().getSize());
	   parent.setRight(child.getLeft());
	   child.setLeft(parent);
	   child.setParent(parent.getParent());
	   parent.setParent(child);
	   parent.getRight().setParent(parent);
	   if (is_parent_root) {// if the rotation changes the root
		   root = child;
	   }

	   else { //change of pointers if the rotation doesn't change the root
		   if (child.getParent().getKey()>child.getKey()) {
		   child.getParent().setLeft(child);
	   }
	   else {
		   child.getParent().setRight(child);
	   	}
	   }
	  }
	   
	   
  
  private int reBalanceForInsert (IAVLNode nodeCheck, IAVLNode child) {//complexity = O(log(n)-child.getHeight()) = O(log(n))
	   int counter = 0;
		  while (nodeCheck!= null && nodeCheck.getHeight() == child.getHeight()) {//while not in the root and difference in ranks is 0
			  int [] ranksDiff = computeRanksDiff(nodeCheck);
			  if ((Arrays.equals(ranksDiff,new int [] {1,0})|(Arrays.equals(ranksDiff,new int [] {0,1})))) { //case 1 - only promote is needed
				  nodeCheck.promote();		
				  counter +=1;
				  child = nodeCheck;
				  nodeCheck = nodeCheck.getParent();


			  }
			  else if ((Arrays.equals(ranksDiff,new int [] {0,2}))) {
				  if (Arrays.equals(computeRanksDiff(child), new int [] {2,1})) {//case 3 - double rotation needed
				  rotateLeft(child,child.getRight());
				  counter +=1;
				  rotateRight(nodeCheck,nodeCheck.getLeft());
				  counter +=1;
				  nodeCheck.demote();
				  counter +=1;
				  child.demote();
				  counter+=1;
				  nodeCheck.getParent().promote();
				  counter +=1;
				  child = child.getParent();
				  nodeCheck = child.getParent();
					  
				  }
				  else if (Arrays.equals(computeRanksDiff(child), new int [] {1,1})){//case happens only in join - single rotation needed
					rotateRight(nodeCheck,child);
					counter +=1;
					IAVLNode temp = nodeCheck;
				    nodeCheck = child;
				    child = temp;				 }
				 else{						//case 2 - single rotation needed
				 rotateRight(nodeCheck,child);
				 counter +=1;
				 nodeCheck.demote();
				 counter +=1;

				  }
			  }
				  
			  
			  else if ((Arrays.equals(ranksDiff,new int [] {2,0}))){
				  if (Arrays.equals(computeRanksDiff(child), new int [] {1,2})){ //case 3 - double rotation needed
					 rotateRight(child,child.getLeft());
					 counter +=1;
					 rotateLeft(nodeCheck, nodeCheck.getRight());
					 counter +=1;
					 nodeCheck.demote();
					 counter +=1;
					 child.demote();
					 counter +=1;
					 nodeCheck.getParent().promote();
					  counter +=1;
					  child = child.getParent();
					  nodeCheck = child.getParent();
					  
				  }
				  else if (Arrays.equals(computeRanksDiff(child), new int [] {1,1})){//case happens only in join - single rotation needed
					  rotateLeft(nodeCheck,child);
						counter +=1;
						 IAVLNode temp = nodeCheck;
						 nodeCheck = child;
						 child = temp;
				  }
				  else {					//case 2 - single rotation needed
				  rotateLeft(nodeCheck,child);
				  counter +=1;
				  nodeCheck.demote();
				  counter +=1;
				  nodeCheck = child.getParent();
				  } 
			  }
			  }			  
		  
		return counter;  
  }
  
 
  
  
  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) { //complexity = O(log(n))
	   //insertion of root
	   if (root.getKey()== -1) {
		   root = new AVLNode(k,i);
		   root.getLeft().setParent(root);
		   root.getRight().setParent(root);
		   max=root;
		   min=root;
		   return(0);
	   }
	   //insertion when tree is not empty
	  IAVLNode nodeCheck = this.findPlace(k); 
	  if (nodeCheck.getKey() == k) {
		  return -1; //node with key 'k' already exists
	  }
	  else {		//node with key 'k' doesn't exist
		  IAVLNode child = new AVLNode (k,i);
		 if (child.getKey()<min.getKey()) {//update min
			  min=child;
		  }
		  if(child.getKey()>max.getKey()) {//update max
			  max=child;
		  }
		  child.setParent(nodeCheck);
		  child.getLeft().setParent(child);
		  child.getRight().setParent(child);
		  if (nodeCheck.getKey()<k) {//node with key 'k' needs to be inserted as the right child of node 'k'
		  nodeCheck.setRight(child);
		  }
		  else{//node with key 'k' needs to be inserted as the left child of node 'k'
			  nodeCheck.setLeft(child);
		  }
		 
	   updateSize(child,1);
	   //balancing
	   return reBalanceForInsert(nodeCheck,child);	
	  }
   }

   
   private IAVLNode findsuccessor (IAVLNode x) { //complexity = O(log(n))
	   if (x.getRight().getKey() != -1) {
		   return findMin(x.getRight());
	   }
		   IAVLNode y = x.getParent();
		   while (y != null && x.getKey() == y.getRight().getKey()) {
			   x = y;
			   y = x.getParent();
		   }
		   return y;
   } 
   
   public IAVLNode findPredecessor (IAVLNode x) {//complexity = O(log(n))
	   if (x.getLeft().getKey() != -1) {
		   return findMax(x.getLeft());
	   }
		   IAVLNode y = x.getParent();
		   while (y != null && x.getKey() == y.getLeft().getKey()) {
			   x = y;
			   y = x.getParent();
		   }
		   return y;
   } 
	  
private IAVLNode deleteUnaryOrLeaf (IAVLNode child, IAVLNode parent, IAVLNode leftChild, IAVLNode rightChild) { //complexity = O(log(n))
	IAVLNode updateFromHere;
		if (child.getKey() < parent.getKey()) { // if child is left son of its parent
		  if (leftChild.getKey() == -1) { 
			  parent.setLeft(rightChild);
			  rightChild.setParent(parent);
		  }
		  else {
			  parent.setLeft(leftChild); 
			  leftChild.setParent(parent);
		  }
	  updateFromHere = parent.getLeft();
	  
	  }
	  else {								// if child is right son of its parent
		  if (leftChild.getKey() == -1) { 
			  parent.setRight(rightChild);
			  rightChild.setParent(parent);
		  }
		  else {
			  parent.setRight(leftChild);
			  leftChild.setParent(parent);
	  }  
		  updateFromHere = parent.getRight();
	  }
		updateSize(updateFromHere,-1);
		  return (updateFromHere);
	}
	

private int reBalanceForDelete (IAVLNode nodeCheck, IAVLNode child) { //complexity = O(log(n)-child.getHeight()) = O(log(n))
	   int counter = 0;
	   int [] ranksDiff = computeRanksDiff(nodeCheck);
	   while (nodeCheck!=null &&
			  !(Arrays.equals(ranksDiff, new int []{2,1}) ||
			   Arrays.equals(ranksDiff, new int []{1,2})||
			   Arrays.equals(ranksDiff, new int []{1,1}))){
		   if (Arrays.equals(ranksDiff, new int []{2,2})) { // case 1 - promote is needed
				  nodeCheck.demote();
				  counter +=1;
				  child = nodeCheck;
				  nodeCheck = nodeCheck.getParent();
				  if (nodeCheck == null) {
					  break;
				  }
				  else {
				  ranksDiff = computeRanksDiff(nodeCheck);
				  }
		   }
		   else if (Arrays.equals(ranksDiff, new int []{3,1})) {
			   
			   if(Arrays.equals(computeRanksDiff(nodeCheck.getRight()), new int []{1,1})) { //case 2 (a) - single rotation is needed
				   rotateLeft(nodeCheck,nodeCheck.getRight());
				   counter +=1;
				   nodeCheck.demote();
				   counter +=1;
				   nodeCheck.getParent().promote();
				   counter +=1;
				  
			   }
			   else if (Arrays.equals(computeRanksDiff(nodeCheck.getRight()), new int []{2,1})) { //case 2 (b) - single rotation is needed
				   rotateLeft(nodeCheck,nodeCheck.getRight());
				   counter +=1;
				   nodeCheck.demote();
				   nodeCheck.demote();
				   counter +=2;
			   }
			   else if (Arrays.equals(computeRanksDiff(nodeCheck.getRight()), new int []{1,2})) { //case 4 - double rotation is needed
				   rotateRight(nodeCheck.getRight(),nodeCheck.getRight().getLeft());
				   counter +=1;
				   rotateLeft(nodeCheck,nodeCheck.getRight());
				   counter +=1;
				   nodeCheck.demote();
				   nodeCheck.demote();
				   counter +=2;
				   nodeCheck.getParent().promote();
				   counter +=1;
				   nodeCheck.getParent().getRight().demote();
				   counter+=1;

			   }
			child = nodeCheck.getParent();
			nodeCheck = child.getParent(); 
			if (nodeCheck==null)
			{
				break;
				}
			  ranksDiff = computeRanksDiff(nodeCheck);
			}
	   
		   else if (Arrays.equals(ranksDiff, new int []{1,3})) {
			   if(Arrays.equals(computeRanksDiff(nodeCheck.getLeft()), new int []{1,1})) { //case 2 (a) - single rotation is needed
				   rotateRight(nodeCheck,nodeCheck.getLeft());
				   counter +=1;
				   nodeCheck.demote();
				   counter +=1;
				   nodeCheck.getParent().promote();
				   counter +=1;
				   
			   }
			   else if (Arrays.equals(computeRanksDiff(nodeCheck.getLeft()), new int []{1,2})) { //case 2 (b) - single rotation is needed
				   rotateRight(nodeCheck,nodeCheck.getLeft());
				   counter +=1;
				   nodeCheck.demote();
				   nodeCheck.demote();
				   counter +=2;
			   }
			   else if (Arrays.equals(computeRanksDiff(nodeCheck.getLeft()), new int []{2,1})) { //case 4 - double rotation is needed
				   rotateLeft(nodeCheck.getLeft(),nodeCheck.getLeft().getRight());
				   counter +=1;
				   rotateRight(nodeCheck,nodeCheck.getLeft());
				   counter +=1;
				   nodeCheck.demote();
				   nodeCheck.demote();
				   counter +=2;
				   nodeCheck.getParent().promote();
				   counter +=1;
				   nodeCheck.getParent().getLeft().demote();
				   counter+=1;

			   }
			child = nodeCheck.getParent();
			nodeCheck = child.getParent();
			if (nodeCheck==null)
			{
				break;
				}
			  ranksDiff = computeRanksDiff(nodeCheck);
			}
			   
		   }
	   
	   return counter;
	   
}

  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k) //complexity = O(log(n))
   {
	   if(empty()) {
		   return -1;
	   }
	   IAVLNode child = this.findPlace(k); 
		  if (child.getKey() != k) {
			  return -1; //node with key 'k' does not exists
		  }
		  else {//node with key 'k' is in tree
			  if (min.getKey()==k) {//update min
				  min=this.findsuccessor(child);
			  }
			  if(max.getKey()==k) {//update max
				  max=this.findPredecessor(child);
			  }
			  IAVLNode parent = child.getParent();
			  IAVLNode leftChild = child.getLeft();
			  IAVLNode rightChild = child.getRight();
			  IAVLNode reBalanceStartNode;
			  
			  // deleting a leaf or an unary node
			 if (leftChild.getKey() == -1 || rightChild.getKey() == -1) {
				  if (parent == null) {//if root is an unary node or leaf and needs to be deleted
					  if (leftChild.getKey() == -1) {
						  root = rightChild;
						  root.setParent(null);
					  }
					  else {
						  root = leftChild;
						  root.setParent(null);
					  }
					  if (root.getKey() == -1) {
						  return 0;
					  }
					  return 0;
				  }
				 reBalanceStartNode =  deleteUnaryOrLeaf(child,parent,leftChild,rightChild);
				  }

			  //deleting a node with 2 children
			  
			  else {
				  IAVLNode y = findsuccessor(child);
				  int key = y.getKey();
				  String value = y.getValue();
				  reBalanceStartNode = deleteUnaryOrLeaf(y,y.getParent(),y.getLeft(),y.getRight());
				  child.setValue(value);
				  child.setKey(key);	  
				  }
		//balancing
		
	   return reBalanceForDelete(reBalanceStartNode.getParent(), reBalanceStartNode);
		  }  
   }
   
   
   
   private IAVLNode findMin(IAVLNode node) { //complexity = O(log(n))
	   IAVLNode curr = node;
	   IAVLNode x = null;
	   while (curr.getKey() != -1) {
		   x = curr;
		   curr = curr.getLeft();
	   }
	   
	   return x;
   }
   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   
   public String min() //complexity = O(1)
   {
	   if (empty()) {
		   return null;
	   }
	   return min.getValue();
	   
   }
   

   private IAVLNode findMax(IAVLNode node) { //complexity = O(log(n))
	   IAVLNode curr = node;
	   IAVLNode x = null;
	   while (curr.getKey() != -1) {
		   x = curr;
		   curr = curr.getRight();
	   }
	   
	   return x;
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()//complexity = O(1)
   {
	   if (empty()) {
		   return null;
	   }
	   return max.getValue();
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray() //complexity = O(n)
  {
        int[] arr = new int[this.size()];
        if (empty()) {
        	return arr;
        }
        int [] index = {0};
        	keysToArray(root, arr, index);
        return arr;
        }
   
    
  private void keysToArray(IAVLNode curr, int[] arr, int[] index) {	 //complexity = O(n)
    		if (curr.getKey() == -1) {
    			return;
    		}
    		keysToArray(curr.getLeft(), arr, index);
    		arr[index[0]] = curr.getKey();
    		index[0] +=1;
    		keysToArray(curr.getRight(),arr, index);
    	    }
  

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()//complexity = O(n)
  {
	        String[] arr = new String[this.size()];
	        if (empty()) {
	        	return arr;
	        }
	        int [] index = {0};
	        infoToArray(root, arr, index);
	        return arr;
	  }

	  private void infoToArray(IAVLNode curr, String[] arr, int[] index) {	//complexity = O(n)
  		if (curr.getValue() == null) {
  			return;
  		}
  		infoToArray(curr.getLeft(), arr, index);
  		arr[index[0]] = curr.getValue();
  		index[0] +=1;
  		infoToArray(curr.getRight(),arr, index);
  	    }
	  

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()//complexity = O(1)
   {
	   if (empty()) {
		   return 0;
	   }
	   return root.getSize(); 
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot() //complexity = O(1)
   {
	   if (root.getKey() == -1) {
		   return null;
	   }
	   return root;
   }
     /**
    * public string split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	  * precondition: search(x) != null
    * postcondition: none
    */   
   public AVLTree[] split(int x) { //complexity = O(log(n))
	   
//find node with key x 
IAVLNode xNode=findPlace(x);

//initialize the smaller and bigger tree

AVLTree smallerKeysTree=new AVLTree();
AVLTree biggerKeysTree=new AVLTree();
//set max's and min's
smallerKeysTree.max=findPredecessor(xNode); 
smallerKeysTree.min=this.min;
biggerKeysTree.min=findsuccessor(xNode);
biggerKeysTree.max=this.max;
//set roots
smallerKeysTree.root=xNode.getLeft();
biggerKeysTree.root=xNode.getRight();
smallerKeysTree.root.setParent(null);
biggerKeysTree.root.setParent(null);

//start splitting !
IAVLNode curr=xNode;
AVLTree temp=new AVLTree();
IAVLNode nextNode;
IAVLNode midNode;
while(curr.getParent()!=null) {
	nextNode = curr.getParent();
	midNode = new AVLNode (curr.getParent().getKey(), curr.getParent().getValue());
	   if (curr.getParent().getKey()<xNode.getKey()) {
		   temp.root=curr.getParent().getLeft();
		   temp.min=smallerKeysTree.min;
		   temp.max=smallerKeysTree.max;
		   temp.root.setParent(null);
		   smallerKeysTree.join(midNode, temp);
	   }
	   else {
		   temp.root=curr.getParent().getRight();
		   temp.min=biggerKeysTree.min;
		   temp.max=biggerKeysTree.max;
		   temp.root.setParent(null);
		   biggerKeysTree.join(midNode,temp);
		   }
	   
	   curr=nextNode;

}

AVLTree[] ans= new AVLTree[] {smallerKeysTree,biggerKeysTree};
return ans; 
}
   
   /**
    * public join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (rank difference between the tree and t)
	  * precondition: keys(x,t) < keys() or keys(x,t) > keys()
    * postcondition: none
    */   
   public int join(IAVLNode x, AVLTree t)//complexity = O(abs(t.getHeigh-this.getHeight)+1)
   {
	   AVLTree smallTree;
	   AVLTree bigTree;
	    
	// if my tree is empty or a virtual leaf
	   if (this.root.getKey() == -1) {   
	   bigTree = t;
		   bigTree.insert(x.getKey(), x.getValue());
		   this.root = bigTree.root;
		   return this.root.getHeight();
	   }
	   
	   
	   
	   //if t is empty or a virual leaf
	   else if (t.root.getKey() == -1) { 
	   bigTree = this;

		   bigTree.insert(x.getKey(),x.getValue());
		   this.root = bigTree.root;
		   return this.root.getHeight();
		   
	   }
	   
	   //both trees are not empty
	   else {							
	   if (t.root.getHeight()<=this.root.getHeight()) {
		   smallTree = t;
		   bigTree = this;
	   }
	   else {
		   smallTree = this;
		   bigTree = t;
	   }
	   IAVLNode aNode = smallTree.root;
	   IAVLNode bNode = bigTree.root;

	   int rankDiff = bNode.getHeight() - aNode.getHeight();
	   IAVLNode cNode;
	   
	   
	   // if keys in small tree are smaller then keys in big tree
	   if (aNode.getKey() < bNode.getKey()) {
		   min=smallTree.min;
		   max=bigTree.max;
		   if (rankDiff == 0 || rankDiff == 1) {
			   aNode.setParent(x);
			   bNode.setParent(x);
			   x.setLeft(aNode);
			   x.setRight(bNode);
			   this.root = x;
			   x.setHeight(bNode.getHeight()+1);
			   x.setSize(aNode.getSize() + bNode.getSize() + 1);
		   }
		   else {
			   //		   while (aNode.getHeight() <= bNode.getHeight() -1 ) {

		   while (aNode.getHeight() <= bNode.getHeight() -1) {
			   bNode = bNode.getLeft();
		   }
		   cNode = bNode.getParent();
		   cNode.setLeft(x);
		   bNode.setParent(x);
		   x.setParent(cNode);
		   x.setRight(bNode);
		   x.setLeft(aNode);
		   aNode.setParent(x);
		   x.setHeight(aNode.getHeight()+1);
		   x.setSize(aNode.getSize() + bNode.getSize() + 1);
		   updateSize(x, aNode.getSize()+1);
		   bigTree.reBalanceForInsert(cNode,x);
		   this.root = bigTree.root;
		   
		   }
	   }
	   
	// if keys in small tree are bigger then keys in big tree
	   else {	
		   min=bigTree.min;
		   max=smallTree.max;;
		   if (rankDiff == 0 || rankDiff == 1) {
			   aNode.setParent(x);
			   bNode.setParent(x);
			   x.setRight(aNode);
			   x.setLeft(bNode);
			   this.root = x;
			   x.setHeight(bNode.getHeight()+1);
			   x.setSize(aNode.getSize() + bNode.getSize() + 1);
		   }
		   else {
		   while (aNode.getHeight()<= bNode.getHeight() -1) {
			   bNode = bNode.getRight();
			   }
		   cNode = bNode.getParent();
		   cNode.setRight(x);
		   bNode.setParent(x);
		   x.setParent(cNode);
		   x.setLeft(bNode);
		   x.setRight(aNode);
		   aNode.setParent(x);
		   x.setHeight(aNode.getHeight()+1);
		   x.setSize(aNode.getSize() + bNode.getSize() + 1);
		   updateSize(x, aNode.getSize()+1);
		   bigTree.reBalanceForInsert(cNode,x);
		   this.root = bigTree.root;
		   }
	   }
	   return rankDiff+1; 
   }
   }

	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
		public int getSize(); // Returns the size of the node (0 for virtual nodes)
		public void setSize(int size); // sets the size of the node
	    public void setKey(int key); //sets the key
	    public void setValue(String value);//sets the Value
	    public void promote();//increases height by 1
	    public void demote();//decreases height by 1

	    


	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{
	  private int key;
	  private String value;
	  private int height = 0;
	  private IAVLNode left ;
	  private IAVLNode right;
	  private IAVLNode parent;
	  private int size = 1;
	  
	  
	  
	  public AVLNode(int key, String value) {
		  this.key = key;
		  this.value = value;
		  this.left = new AVLNode(-1,null, true);
		  this.right = new AVLNode(-1,null, true);
	  }
	  public AVLNode(int key, String value, boolean is_virtual) {
		  this.key = key;
		  this.value = value; 
		  this.height = -1;
		  this.size = 0;
	  }
	  
		

		public int getKey()
		{
			return key; 
		}
		public String getValue()
		{
			return value; 
		}
		public void setLeft(IAVLNode node)
		{
			this.left =node; 
		}
		
		public IAVLNode getLeft()
		{
			return left; 
		}
		public void setRight(IAVLNode node)
		{
			 this.right = node;
		}
		public IAVLNode getRight()
		{
			return right; 
		}
		public void setParent(IAVLNode node)
		{
			this.parent = node; 
		}
		public IAVLNode getParent()
		{
			return parent; 
		}
		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode()
		{
			if (key!=-1) {
				return true;
			}
			return false; 
		}
    public void setHeight(int height)
    {
      this.height=height;
    }
    public int getHeight()
    {
      return height; 
    }
    public void setSize(int size)
    {
      this.size = size;
    }
    public int getSize()
    {
      return size; 
    }
    public void setKey(int key)
    {
      this.key = key;
    }
    public void setValue(String value)
     {
       this.value = value;
     }
    public void promote() { 
 	   height = height +1;
    }
    
    public void demote() { 
    	height = height -1;
    	}
    }
  }

  

