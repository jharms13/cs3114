/**
 * A binary search tree operating on handles.
 * 
 * This binary search tree class is the outer class to the real BST which is
 * implemented internally with nodes. This is NOT a recursive definition of 
 * the BST (i.e. a BST does not have a BST as a child)
 * 
 * @author Reese Moore
 * @author Tyler Kahn
 * @version 2011.10.10
 */
public class BinarySearchTree {
	
	private BSTNode root;
	private MemoryManager mem;
	
	/**
	 * Instantiate a new BST
	 * @param mem The memory manager to use.
	 */
	public BinarySearchTree(MemoryManager mem)
	{
		this.mem = mem;
	}
	
	/**
	 * Insert a new KV pair into the BST.
	 * 
	 * @param key The key to insert.
	 * @param value The value to insert.
	 */
	public void insert(Handle key, Handle value) // Key -> String, Value -> City
	{
		// We may have to insert the first node into the tree.
		if (root == null) { 
			root = new BSTNode(key, value); 
			return;
		}
		
		// In the general case this is a lot easier.
		new BSTNode(key, value, root);
	}
	
	/**
	 * Find a value from the BST based on the key.
	 * @param key The key to search on.
	 * @return an ArrayList (ours, not JavaAPI) of values matching the key.
	 */
	public ArrayList<Handle> find(String key)
	{
		ArrayList<Handle> found = new ArrayList<Handle>();
		if (root == null) { return found; }
		
		BSTNode found_node = root.find(key);
		while (found_node != null) {
			found.add(found_node.getValueHandle());
			if (found_node.getRight() == null) { break; }
			found_node = found_node.getRight().find(key);
		}
		
		return found;
	}
	
	/**
	 * Remove a value from the BST. This removes the first occurrence of the 
	 * key to be found
	 * @param key The key to search on.
	 * @return The removed value.
	 */
	public Handle remove(String key)
	{
		// Find the node to remove, and it's parent
		BSTNode parent = null;
		BSTNode remove_node = root;
		
		while (remove_node != null && !remove_node.getKeyVal().equals(key)) {
			parent = remove_node;
			if (key.compareTo(remove_node.getKeyVal()) < 0) {
				remove_node = remove_node.getLeft();
			} else {
				remove_node = remove_node.getRight();
			}
		}
		
		if (remove_node == null) { return null; }

		// No children.
		if (remove_node.getLeft() == null && remove_node.getRight() == null) {
			if (parent == null) {
				root = null;
			} else if (parent.getLeft() == remove_node) {
				parent.setLeft(null);
			} else {
				parent.setRight(null);
			}
			return remove_node.getValueHandle();
		}
		
		// One child.
		if (remove_node.getLeft() == null) {
			if (parent == null) {
				root = remove_node.getRight();
			} else if (parent.getLeft() == remove_node) {
				parent.setLeft(remove_node.getRight());
			} else {
				parent.setRight(remove_node.getRight());
			}
			return remove_node.getValueHandle();
		} else if (remove_node.getRight() == null) {
			if (parent == null) {
				root = remove_node.getLeft();
			} else if (parent.getLeft() == remove_node) {
				parent.setLeft(remove_node.getLeft());
			} else {
				parent.setRight(remove_node.getLeft());
			}
			return remove_node.getValueHandle();
		}
		
		// Both children, find in order predecessor.
		BSTNode IOP_parent = remove_node;
		BSTNode InOrderPred = remove_node.getLeft();
		while (InOrderPred.getRight() != null) {
			IOP_parent = InOrderPred;
			InOrderPred = InOrderPred.getRight();
		}
		
		// Hoist the child (if any) of the IOP.
		IOP_parent.setRight(InOrderPred.getLeft());
		
		// Replace remove_node with replacement (IOP)
		BSTNode replacement = new BSTNode(InOrderPred.getKey(), InOrderPred.getValueHandle());
		replacement.setLeft(remove_node.getLeft());
		replacement.setRight(remove_node.getRight());
		if (parent == null) {
			root = replacement;
		} else if (parent.getLeft() == remove_node) {
			parent.setLeft(replacement);
		} else {
			parent.setRight(replacement);
		}
		return remove_node.getValueHandle();
	}
	
	/**
	 * The internal nodes of the Binary Search Tree.
	 * This is where all of the work is done.
	 * 
	 * @author Reese Moore
	 * @author Tyler Kahn
	 * @version 2011.10.09
	 */
	private class BSTNode {
		private final Handle key;
		private final Handle value;
		private BSTNode left;
		private BSTNode right;
		
		/**
		 * Create a new Node for the tree.
		 * 
		 * @param key The key to store in this node.
		 * @param value The value of this node.
		 */
		public BSTNode(Handle key, Handle value) 
		{
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Create a new Node for the tree and insert it in the proper place,
		 * starting from the root node passed in.
		 * 
		 * @param key The key to store in this node.
		 * @param value The value to store in this node.
		 * @param root The root node to start from for insertion.
		 */
		public BSTNode(Handle key, Handle value, BSTNode root)
		{
			this(key, value);
			root.insert(this);
		}
		
		/**
		 * Insert a node below this. 
		 * Either insert the new node as the proper child, or pass it on down
		 * the tree until it reaches the proper place.
		 * @param node The new node to insert.
		 */
		public void insert(BSTNode node)
		{
			if (node.getKeyVal().compareTo(getKeyVal()) < 0) {
				if (getLeft() == null) {
					setLeft(node);
				} else {
					getLeft().insert(node);
				}
			} else {
				if (getRight() == null) {
					setRight(node);
				} else {
					getRight().insert(node);
				}
			}
		}
		
		/**
		 * Retrieve a value from the BST.
		 * @param key The key to search on.
		 * @return The BSTNode containing a found value.
		 */
		public BSTNode find(String key)
		{
			if (key.equals(getKeyVal())) { return this; }
			if (key.compareTo(getKeyVal()) < 0) {
				return (left == null ? null : left.find(key));
			} else {
				return (right == null ? null : right.find(key));
			}
		}

		/**
		 * Get the key value of a given node
		 * @return The key from this node.
		 */
		public String getKeyVal() {
			return DiskString.deref(mem, key);
		}
		
		/**
		 * Get the key handle
		 * @return The key handle
		 */
		public Handle getKey()
		{
			return key;
		}
		
		/**
		 * Get the value handle
		 * @return the value handle
		 */
		public Handle getValueHandle() {
			return value;
		}

		/**
		 * Get the left child node of this node.
		 * @return the left child.
		 */
		public BSTNode getLeft() {
			return left;
		}

		/**
		 * Set the left child node of this node.
		 * @param left The new left child node of this one.
		 */
		public void setLeft(BSTNode left) {
			this.left = left;
		}

		/**
		 * Get the right child node of this node.
		 * @return the right child.
		 */
		public BSTNode getRight() {
			return right;
		}

		/**
		 * Set the right child node of this node.
		 * @param right The new right child node of this one.
		 */
		public void setRight(BSTNode right) {
			this.right = right;
		}
	}
}
