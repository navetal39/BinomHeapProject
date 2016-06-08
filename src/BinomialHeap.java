
/*
 * Submitted by:
 * navetal - 208518456
 * amitbanay - 208520528
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers. Based on
 * exercise from previous semester.
 *
 * For describing the complexity of its methods if not said otherwise we denote
 * by n the number of nodes/values in the heap. We usually don't mention the
 * trivial O(1) computations in the process of calculating the complexity of the
 * methods. A binomial heap is an implementation of a dynamic heap using a list
 * of binomial trees of ranks representing the binary representation of the
 * number of values (or nodes, because the trees can be represented as a graph)
 * in the heap.
 *
 * For describing the complexity of the methods if not said otherwise we denote
 * by n the number of nodes/values in the heap. We usually don't mention the
 * trivial O(1) computations in the process of calculating the complexity of the
 * methods.
 * 
 * Note that the terms key and value are identical in our context.
 */

public class BinomialHeap
{
	/**
	 * Describes an heap node, the basic unit of binomial trees who assemble a
	 * binomial heap.
	 * <p>
	 * Every heap node is a root to a binomial tree. We say that a node is a
	 * complete root of a binomial tree or a complete binomial tree if it is on
	 * the root list of the binomial heap.
	 */
	public class HeapNode
	{
		/**
		 * The node's identifier and the mean with which he can be compared to
		 * other nodes (key).
		 */
		private int value;
		/**
		 * The node's parent, null if it is a root of a complete binomial tree.
		 */
		private HeapNode parent;
		/**
		 * If the node is a complete root then it is the complete root of the
		 * binomial tree with the next higher rank on the root list or itself if
		 * this node's tree is the binomial heap only (and lonely) binomial
		 * tree.
		 * <p>
		 * If the node is not a complete root it is his next brother- the root
		 * of the single binomial tree with a rank higher by one whose parent is
		 * the same. If the node has the highest rank (see
		 * {@link HeapNode#child}) then it points to the brother with rank 0.
		 */
		private HeapNode sibling;
		/**
		 * The child of this node with the highest rank. null iff this node's
		 * rank is 0.
		 */
		private HeapNode child;
		/**
		 * The rank the binomial tree whose root is this node.
		 */
		private int rank;

		/**
		 * Creates a new binomial zero ranked tree with the given value.
		 *
		 * @param value
		 */
		public HeapNode(int value)
		{
			this.value = value;
			this.parent = null;
			this.sibling = this;
			this.child = null;
			this.rank = 0;
		}

		/**
		 * Returns the vaue of this node.
		 * 
		 * @return value of the node
		 */
		public int getValue()
		{
			return value;
		}

		/**
		 * Sets the value of the node to the input value.
		 * 
		 * @param value
		 */
		public void setValue(int value)
		{
			this.value = value;
		}

		/**
		 * Returns the parent of the node
		 * 
		 * @return node's parent
		 */
		public HeapNode getParent()
		{
			return parent;
		}

		/**
		 * Sets the parent of the node to be the input node.
		 * 
		 * @param parent
		 */
		public void setParent(HeapNode parent)
		{
			this.parent = parent;
		}

		/**
		 * Returns the immediate sibling of the node (only the one this node
		 * points to, not the other ones in the chain)
		 * 
		 * @return This nodes immediate sibling.
		 */
		public HeapNode getSibling()
		{
			return sibling;
		}

		/**
		 * Sets the immediate sibling of this node to be the input node.
		 * 
		 * @param sibling
		 */
		public void setSibling(HeapNode sibling)
		{
			this.sibling = sibling;
		}

		/**
		 * Returns the rank of the node (the rank of the binomial tree which
		 * this node's the root of.
		 * 
		 * @return This node's rank.
		 */
		public int getRank()
		{
			return rank;
		}

		/**
		 * Sets the rank of this node to be the input rank. Does not actually
		 * change the tree which this node's the root of, only the numerical
		 * value.
		 * 
		 * @param rank
		 */
		public void setRank(int rank)
		{
			this.rank = rank;
		}

		/**
		 * Returns the immediate child of this node (That is, the node which
		 * this node points to as a child, not this child's siblings.)
		 * 
		 * @return The node this node points to as a child.
		 */
		public HeapNode getChild()
		{
			return this.child;
		}

		/**
		 * public boolean addChild()
		 * <p>
		 * The method adds child as a child of this node (also called unifying
		 * child into this). The method assumes the rank of child and the rank
		 * of this are the same (if not, the structure will break). runs in
		 * O(1).
		 *
		 * @param child
		 *            A non null HeapNode whose rank is the same as this.
		 */
		public void addChild(HeapNode child)
		{
			OurTester.LINK_COUNT++;
			assert child != null;
			assert this.rank == child.rank;

			if (this.child == null)
			{
				this.child = child;
				this.child.setParent(this);
				this.child.setSibling(child);
				this.rank++;
				return;
			}

			// We set child as the child with the highest rank.
			HeapNode oldChild = this.child;
			child.setSibling(oldChild.getSibling());
			oldChild.setSibling(child);
			this.child = child;
			this.child.setParent(this);
			this.rank++;
		}

		/**
		 * The method checks whether the current node is a root to a valid
		 * binomial tree. the function is called recursively on the children of
		 * this down to the leaves. Thus it takes O(n) when n is the number of
		 * nodes in the binomial tree (or another creature if it not a valid)
		 * rooted by this.
		 *
		 * @return true if this is a root to a valid binomial tree, false
		 *         otherwise.
		 */
		public boolean isValidRoot()
		{
			if (this.getChild() == null)
				return this.getRank() == 0;

			HeapNode first = this.getChild().getSibling();
			HeapNode current = first;

			// Go through the children from rank 0 upto {our rank}-1.
			for (int i = 0; i < this.getRank(); i++)
			{
				if (current.getRank() != i || current.getValue() <= this.getValue() || current.getParent() != this
						|| !current.isValidRoot())
				{
					return false;
				}
				if (current == first && i != 0)
				{
					return false;
				}
				current = current.getSibling();
			}

			return current == first;
		}
	}

	/**
	 * A sentinel heap node whose sibling the first node of the root list. (head
	 * himself is not part of the root list). The root list is a cyclic list of
	 * the complete binomial trees that comprise the heap. see {@link HeapNode}
	 * 's documentation.
	 */
	private HeapNode head;
	/**
	 * A map from the values of the nodes in this heap to the nodes.
	 */
	Map<Integer, HeapNode> nodes;
	/**
	 * The value of the node with the minimal value. If the heap is empty
	 * usually set to -1.
	 */
	private int min;

	/**
	 * Instantiates an empty binomial heap.
	 */
	public BinomialHeap()
	{
		this.head = new HeapNode(-42);
		this.nodes = new HashMap<Integer, HeapNode>();
		this.min = -1;
	}

	/**
	 * Instantiates a new binomial heap with a single node who has the given
	 * value
	 *
	 * @param value
	 */
	public BinomialHeap(int value)
	{
		HeapNode node = new HeapNode(value);
		this.head = new HeapNode(-42);
		this.head.sibling = node;
		this.nodes = new HashMap<Integer, HeapNode>();
		this.nodes.put(node.getValue(), node);
		this.min = node.getValue();
	}

	/**
	 * Instantiates a new binomial heap with the given root list. All fields of
	 * the heap but the {@link #nodes} field are set according to the given root
	 * list. The nodes map will only have one node - {@param rootList}. All
	 * parents of the roots in {@param rootList} are nullified.
	 * <p>
	 * Nullifying the root list takes O(log(n)) if it has n nodes and
	 * {@link #updateMin()} takes O(log(n)) time so the method runs in
	 * O(log(n)).
	 *
	 * @param rootList
	 *            A valid cyclic list of complete binomial trees with ascending
	 *            ranks. The parents of their roots may be null.
	 */
	public BinomialHeap(HeapNode rootList)
	{
		this.head = new HeapNode(-42);
		this.head.setSibling(rootList);
		this.nodes = new HashMap<Integer, HeapNode>();
		this.nodes.put(rootList.getValue(), rootList);

		// Nullify parents.
		HeapNode current = rootList;
		do
		{
			current.setParent(null);
			current = current.getSibling();
		} while (current != rootList);

		updateMin();
	}

	/**
	 * public boolean empty()
	 * <p>
	 * precondition: none
	 * <p>
	 * The method returns true if and only if the heap is empty. runs in O(1).
	 */
	public boolean empty()
	{
		return this.head.sibling == this.head;
	}

	/**
	 * public void insert(int value)
	 * <p>
	 * Insert value into the heap. The method inserts the given value by melding
	 * this with a single node heap with {@param value} as the node's value.
	 * Thus it runs in O(log(n)). We learned in class that if we start with an
	 * empty binomial tree and commit n insert operations then the amortized
	 * time is O(1).
	 *
	 * @param value
	 *            the value to be inserted.
	 */
	public void insert(int value)
	{
		// If value is already in the heap.
		if (this.nodes.get(value) != null)
		{
			return;
		}

		// Insert the value to the heap by melding with a single node heap with
		// the value.
		BinomialHeap h = new BinomialHeap(value);
		this.meld(h);
	}

	/**
	 * public void deleteMin()
	 * <p>
	 *
	 * Delete the minimum value.
	 *
	 * The method goes through the root list to find the node with the minimal
	 * value which is a root to a complete binomial tree and deletes it. Then it
	 * creates a new heap with his O(log(n)) children as the complete binomial
	 * trees and melds them to get a new almost valid binomial heap with all
	 * nodes but the node of the minimum. Then it updates the minimum field
	 * contain the new minimal value and make the heap valid.
	 *
	 * Going through the root list to find the node with the minimal value takes
	 * O(log(n)). Then the method creates a new heap with his O(log(n)) children
	 * as the complete binomial trees in O(log(n)), because our heap also has
	 * O(log(n)) long root list the melding takes O(log(n)). Then the method
	 * calls {@link #updateMin() which runs in O(log(n)) resulting in a total
	 * time complexity of O(log(n)).
	 */
	public void deleteMin()
	{
		if (this.empty())
		{
			return;
		}

		HeapNode minNode = this.nodes.get(this.min);

		HeapNode prev = null;
		HeapNode first = this.head.getSibling();
		HeapNode current = first;

		// Find the complete root which is minNode (minimums are always complete
		// roots)
		// and its previous node.
		while (current != minNode)
		{
			prev = current;
			current = current.getSibling();
		}

		if (prev == null)
		{
			// The root is the first complete root.

			if (current.getSibling() == current)
			{
				this.head.setSibling(this.head);
			} else
			{
				// Make the head "skip" the minNode in the root list.
				this.head.setSibling(current.getSibling());
				// Go to the last root of the root list.
				while (current.getSibling() != first)
				{
					current = current.getSibling();
				}
				// Make the last root of the root list "skip" the minNode.
				// By doing so we maintain the circularity property of the root
				// list.
				current.setSibling(first.getSibling());
			}
		} else
		{
			// Simply skip minNode in the root list.
			prev.setSibling(current.getSibling());
		}

		this.nodes.remove(this.min);
		if (minNode.getChild() != null)
		{
			// Create a new heap with minNode's children as the complete
			// binomial trees.
			HeapNode newNodes = minNode.getChild().getSibling();
			BinomialHeap newHeap = new BinomialHeap(newNodes);
			// Meld it into the heap so the children will be in the heap even
			// though
			// their father has been deleted.
			this.meld(newHeap);
		}
		updateMin();
	}

	/**
	 * public int findMin()
	 *
	 * Return the minimum value (the value in the min field). If the heap is
	 * empty the method may return shtuyot. runs in O(1).
	 */
	public int findMin()
	{
		return this.min;
	}

	/**
	 * Go through the root list, find and update the minimum value. The minimum
	 * value must be on the root list because of the heap property.
	 *
	 * Going through the root list makes the method run in O(log(n)).
	 */
	private void updateMin()
	{
		if (this.empty())
		{
			this.min = -1;
			return;
		}

		HeapNode first = this.head.getSibling();
		int min = first.getValue();
		HeapNode current = first.getSibling();
		while (current != first)
		{
			if (current.getValue() < min)
			{
				min = current.getValue();
			}
			current = current.getSibling();
		}
		this.min = min;
	}

	/**
	 * public void meld (BinomialHeap heap2)
	 * <p>
	 * Meld the heap with heap2. In hebrew melding means uniting the heaps to
	 * get an bigger heap with their nodes. the method changes this to the union
	 * of the heaps. heap2 may be violated.
	 *
	 * If one of the heaps is empty the minimum is set to the other heap's
	 * minimum, otherwise it is set to the minimum of this and heap2's min field
	 * value.
	 *
	 * The nodes field of the heaps may be invalid as long as the union of
	 * this.nodes and heap2.nodes is the set of nodes in both heaps (nodes in
	 * the union of the heaps).
	 *
	 * Assuming both heaps size is O(n), their root lists size is O(log(n)) so
	 * calling {@link #mergeRootLists(BinomialHeap, BinomialHeap)} takes
	 * O(log(n)) time. The main loop in the method goes through the merged root
	 * list which will also be of size O(log(n)). So the resulting running time
	 * of meld is O(log(n)).
	 *
	 * Meld the current heap with heap2. In hebrew melding means uniting the
	 * heaps to get a bigger heap with their nodes. The method changes the
	 * current heap to be the union of the heaps. heap2 may be violated.
	 *
	 * If one of the heaps is empty the minimum is set to the other heap’s
	 * minimum. Otherwise it is set to be the minimum of the current heap and
	 * heap2’s min field value.
	 *
	 * When calling {@link #meld(BinomialHeap)} the nodes field of the current
	 * heap or heap2 may be invalid as long as the union of this.nodes and
	 * heap2.nodes is the set of nodes in both heaps (it is a map of the nodes
	 * in the union of the heaps).
	 *
	 * The method's algorithm: In order to unify the heaps we first merge the
	 * root list of the current heap and heap2 to get a monotonically increasing
	 * (in terms of ranks) non cyclic list of their complete binomial trees.
	 * That list may contain up to 2 binomial trees of the same rank. We update
	 * the nodes map and the min field to contain valid value.
	 *
	 * Then we go through the merged root list. 1) If we have 2 differently
	 * ranked subsequent trees we are good and we continue. 2) If we have 3
	 * subsequent same ranked trees we continue (may happen if we had 2 pairs of
	 * subsequent same ranked trees with subsequent rank: for example 2 trees of
	 * rank 5 and 2 trees of rank 6 - by uniting the trees with rank 5 we get 3
	 * trees with rank 6). After continuing the next case applies. 3) If we have
	 * exactly 2 subsequent same ranked binomial trees we unify them to get a
	 * tree with a rank higher by 1. We make the tree with the bigger root the
	 * child so we maintain the heap property. When we reach the last node in
	 * the merged root list we end that process.
	 *
	 * After that process we get a valid root list (The unique ranks of it are
	 * the determined by the binary representation of the size of the unified
	 * heap). We connect the last node in the merged root list to its first node
	 * to get a cyclic root list and finish.
	 *
	 * Assuming both heaps size is O(n), their root lists size is O(log(n)) so
	 * calling {@link #mergeRootLists(BinomialHeap, BinomialHeap)} takes
	 * O(log(n)) time. The main loop in the method goes through the merged root
	 * list which will also be of size O(log(n)) and adding a child takes O(1).
	 * So the resulting running time of meld is O(log(n)). We ignore the O(n)
	 * time taken by uniting the nodes maps of the current heap and heap2.
	 *
	 * @param heap2
	 *            The heap that will be unified into the current heap.
	 */
	public void meld(BinomialHeap heap2)
	{
		// Merge the root lists.
		HeapNode first = mergeRootLists(this, heap2);
		if (first == null)
		{ // Both heaps are empty
			return;
		}

		// Update nodes to include the second heap's nodes.
		this.nodes.putAll(heap2.nodes);

		// Update the minimum
		if (!this.empty())
		{
			if (!heap2.empty())
			{
				this.min = Math.min(this.min, heap2.min);
			}
		} else
		{
			this.min = heap2.min;
		}

		// Now is the fun part :)

		HeapNode prev = null;
		HeapNode current = first;
		HeapNode next = first.getSibling();

		// We go through the merged list.
		while (next != null)
		{
			if (current.getRank() != next.getRank()
					|| next.getSibling() != null && current.getRank() == next.getSibling().getRank())
			{
				// If current's and next's ranks are different or we have 3
				// equally ranked
				// subsequent binomial trees, move next in the merged list.
				prev = current;
				current = current.getSibling();
			} else
			{
				// If we have 2 subsequent same ranked binomial trees we unify
				// them to a tree with
				// a rank higher by 1. We make the tree with the bigger root the
				// child so we
				// maintain the heap property.
				if (current.getValue() < next.getValue())
				{
					// Unifying next into current.
					current.setSibling(next.getSibling());
					current.addChild(next);
				} else
				{
					// Unifying current into next.
					if (prev == null)
					{
						first = next;
					} else
					{
						prev.setSibling(next);
					}
					next.addChild(current);
					current = next;
				}
			}
			next = current.getSibling();
		}

		// current is the last child in the merged list after unifying.
		// We make the merged root list cyclic so it will be a valid root list.
		current.setSibling(first);
		this.head.setSibling(first);
	}

	/**
	 * Merge the root lists of the given heaps to a monotonically increasing in
	 * terms of ranks list. There may be up to 2 subsequent complete roots with
	 * the same rank in that list. NOTE: The given heaps’ root list might be
	 * violated.
	 *
	 * Assuming both heaps size is O(n) their root lists size is O(log(n)). The
	 * method runs through the root lists and thus takes O(log(n)) time.
	 *
	 * @param h1
	 * @param h2
	 * @return The merged root list.
	 */
	private HeapNode mergeRootLists(BinomialHeap h1, BinomialHeap h2)
	{
		if (h1.empty() && h2.empty())
		{
			return null;
		}

		// Convert the root lists of h1 and h2 to non cyclic lists.
		HeapNode l1 = null, l2 = null;
		// Converting h1's root list.
		if (!h1.empty())
		{
			l1 = h1.head.getSibling();
			HeapNode current = l1;
			// Find the last root in the root list.
			while (current.getSibling() != l1)
			{
				current = current.getSibling();
			}
			// Cut his tail!
			current.setSibling(null);
		}
		// We do the same for h2.
		if (!h2.empty())
		{
			l2 = h2.head.getSibling();
			HeapNode current = l2;
			while (current.getSibling() != l2)
			{
				current = current.getSibling();
			}
			current.setSibling(null);
		}

		HeapNode first;
		HeapNode current1 = l1;
		HeapNode current2 = l2;

		// We set the first complete root in the merged root list
		// to be the one with the lowest rank. That root must be
		// In the beginning of h1's or h2's root list.
		if (!h1.empty())
		{
			if (!h2.empty())
			{
				if (l1.getRank() <= l2.getRank())
				{
					first = l1;
					current1 = current1.getSibling();
				} else
				{
					first = l2;
					current2 = current2.getSibling();
				}
			} else
			{
				first = l1;
				current1 = current1.getSibling();
			}
		} else
		{
			first = l2;
			current2 = current2.getSibling();
		}

		HeapNode current = first;
		// We add one complete root at the time from the root lists to the
		// merged
		// list until we finish going through both root lists.
		while (current1 != null || current2 != null)
		{
			// If we are not yet in the end in both of the root lists we
			// add the complete root with the lowest rank to the merged list.
			// Else we add a root from the list we have'nt finished yet.
			if (current1 != null)
			{
				if (current2 != null && current2.getRank() < current1.getRank())
				{
					current.setSibling(current2);
					current2 = current2.getSibling();
				} else
				{
					current.setSibling(current1);
					current1 = current1.getSibling();
				}
			} else
			{
				current.setSibling(current2);
				current2 = current2.getSibling();
			}
			current = current.getSibling();
		}

		return first;
	}

	/**
	 * Returns the number of elements in the heap. We maintain the nodes' map to
	 * contain a mapping of the current nodes only, so we simply return its
	 * size. runs in O(1).
	 */
	public int size()
	{
		return this.nodes.size();
	}

	/**
	 * Return the minimum rank of a complete binomial tree in the heap. The
	 * minimally ranked complete binomial tree is always the first one. runs in
	 * O(1).
	 */
	public int minTreeRank()
	{
		return this.head.getSibling().getRank();
	}

	/**
	 * Returns an array containing the binary representation of the heap. We
	 * create a boolean array of the size of the highest ranked binomial tree.
	 * Then we go through the root list and put true values in the indices
	 * pointed by the ranks of the complete binomial trees in it because the
	 * ranks of the complete binomial trees are exactly the binary
	 * representation of the heap.
	 *
	 * Running through the root list takes O(log(n)) time so the method’s time
	 * complexity is O(log(n)).
	 */
	public boolean[] binaryRep()
	{
		if (this.empty())
		{
			return new boolean[0];
		}
		HeapNode n = this.head.getSibling();
		while (n.getSibling() != this.head.getSibling())
			n = n.getSibling();
		boolean[] arr = new boolean[n.getRank() + 1];
		n = this.head;
		do
		{
			n = n.getSibling();
			arr[n.getRank()] = true;
		} while (n.getSibling() != this.head.getSibling());
		return arr;
	}

	/**
	 * Deletes previous elements in the heap. Inserts the array's values to the
	 * heap. When inserting n values to an empty binomial heap each insert
	 * operation takes O(1) amortized so the total cost is O(n).
	 */
	public void arrayToHeap(int[] array)
	{
		// Resetting the current heap.
		this.head = new HeapNode(-42);
		this.nodes = new HashMap<Integer, HeapNode>();
		this.min = -1;

		for (int i : array)
			this.insert(i);
	}

	/**
	 * public boolean isValid()
	 * <p>
	 * Returns true if and only if the heap is valid.
	 *
	 * The method goes through the root list: Checks that each complete root is
	 * an orphan (parent is null) valid binomial tree using
	 * {@link HeapNode#isValidRoot()}. Checks if the ranks of the complete
	 * binary trees fit the heap's size Checks the same rank doesn't show twice
	 * in the root list.
	 *
	 * Then the method checks minNode to be in the root list. If everything was
	 * ok the function returns true, else false.
	 *
	 * In the main loop the method runs through the root list and calls
	 * {@link HeapNode#isValidRoot()} on each complete root. Each call to
	 * {@link HeapNode#isValidRoot()} on a complete root takes time linear to
	 * the number of nodes in the binomial tree rooted by it, so the method
	 * (isValid) takes time linear to the number of nodes in the heap. We also
	 * go through the root list when validating min in O(log(n)). The total cost
	 * of the method is O(n).
	 *
	 */
	public boolean isValid()
	{
		// If the heap is empty.
		if (this.head.getSibling() == head)
		{
			if (head.getParent() == null && head.getSibling() == head && head.getChild() == null && this.min == -1
					&& nodes.size() == 0)
			{
				return true;
			} else
			{
				return false;
			}
		}

		HeapNode first = this.head.getSibling();
		HeapNode current = first;
		Set<Integer> seenRanks = new HashSet<Integer>();
		// Check that each complete root is an orphan valid binary tree and
		// makes sure the ranks of the complete binary trees fit the heap's size
		// and that the same rank doesn't show twice.
		int size = 0;
		do
		{
			// A binomial tree of rank k has 2^k nodes.
			size += Math.pow(2, current.getRank());
			if (seenRanks.contains(current.getRank()) || current.getParent() != null || !current.isValidRoot())
			{
				return false;
			}
			seenRanks.add(current.getRank());

			current = current.getSibling();
		} while (current != first);

		// Validate size.
		if (size != nodes.size())
		{
			return false;
		}

		current = first;
		HeapNode minNode = this.nodes.get(this.min);
		// Check minNode to be in the root list.
		do
		{
			if (current == minNode)
			{
				return true;
			}

			current = current.getSibling();
		} while (current != first);

		return false;
	}

	/**
	 * public void delete(int value)
	 * <p>
	 * Deletes the element with the given value from the heap, if such an
	 * element exists. If the heap doesn't contain an element with the given
	 * value, don't change the heap.
	 *
	 * The method first decreases the key of the node with the given value (if
	 * needed) so it will be the minimum node and then uses deleteMin to delete
	 * it.
	 *
	 * Calling {@link #deleteMin()} and {@link #decreaseKey(int, int)} which
	 * both run in O(log(n)) make the method run in O(log(n)) too.
	 *
	 * @param value
	 *            The value to be deleted or ignored if not in the heap.
	 */
	public void delete(int value)
	{
		if (!this.nodes.containsKey(value))
		{
			return;
		}

		// We decrease the key of the node with the given value (if needed)
		// so it will be the minimum and then use deleteMin to delete it.
		if (value == this.min)
		{
			this.deleteMin();
		} else
		{
			this.decreaseKey(value, -1);
			this.deleteMin();
		}
	}

	/**
	 * public void decreaseKey(int oldValue, int newValue)
	 * <p>
	 * If the heap doesn't contain an element with value {@param oldValue},
	 * don't change the heap. Otherwise decrease the value of the element whose
	 * value is oldValue to be newValue. Assumes {@param newValue} <=
	 * {@param oldValue}. We update the min field if necessary.
	 *
	 * The method "bubbles up the node" with {@param oldValue} until it doesn't
	 * violate the heap property. It does so by switching its value with its
	 * parent's value and updating the nodes map accordingly until the heap
	 * property is not violated by changing its value to{@param newValue}. That
	 * process takes time linear to the height of the node's tree which is the
	 * rank of that tree which is O(log(n)). Thus the method runs in O(log(n)).
	 *
	 * @param oldValue
	 *            A value of a (real/imaginary) node in the heap that we will
	 *            decrease its key/value if real.
	 * @param newValue
	 *            The new value to be assigned. must be smaller then
	 *            {@param oldValue}
	 */
	public void decreaseKey(int oldValue, int newValue)
	{
		// If there is no node with the given value in the heap or the values
		// are
		// identical we return.
		if ((!this.nodes.containsKey(oldValue)) || oldValue == newValue)
		{
			return;
		}

		HeapNode problem = this.nodes.get(oldValue);
		// We decrease the node's value, and updating the nodes map accordingly.
		this.nodes.remove(oldValue);
		problem.setValue(newValue);
		this.nodes.put(newValue, problem);

		// We go up our binomial tree until our node doesn't violate the heap
		// property, or
		// until we are the complete root and then the argument is trivially
		// true.
		while (problem.getParent() != null && problem.getValue() < problem.getParent().getValue())
		{
			// We switch our value with our parent's value.
			// Alternatively we bubble ourselves up.
			int parentValue = problem.getParent().getValue();
			problem.getParent().setValue(newValue);
			problem.setValue(parentValue);

			// We update the nodes map accordingly.
			this.nodes.remove(newValue);
			this.nodes.remove(parentValue);
			this.nodes.put(newValue, problem.getParent());
			this.nodes.put(parentValue, problem);

			problem = problem.getParent();
		}

		// Updating the min field if the new value is now the minimum.
		if (newValue < this.min)
		{
			this.min = newValue;
		}
	}

}