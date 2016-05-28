import java.util.HashMap;
import java.util.Map;

/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers. Based on
 * exercise from previous semester.
 */
public class BinomialHeap
{
	public class BinomialNode
	{
		private int value;
		private BinomialNode parent;
		private BinomialNode sibling;
		private BinomialNode child;
		private int rank;

		public BinomialNode(int value)
		{
			this.value = value;
			this.parent = null;
			this.sibling = this;
			this.child = null;
			this.rank = 0;
		}

		public int getValue()
		{
			return value;
		}

		public void setValue(int value)
		{
			this.value = value;
		}

		public BinomialNode getParent()
		{
			return parent;
		}

		public void setParent(BinomialNode parent)
		{
			this.parent = parent;
		}

		public BinomialNode getSibling()
		{
			return sibling;
		}

		public void setSibling(BinomialNode sibling)
		{
			this.sibling = sibling;
		}

		public int getRank()
		{
			return rank;
		}

		public void setRank(int degree)
		{
			this.rank = degree;
		}

		public BinomialNode getChild()
		{
			return this.child;
		}

		/**
		 * public boolean addChild()
		 *
		 * precondition: none
		 * 
		 * The method adds child as a child of this node. The method assumes the
		 * rank of child and the rank of this are the same (if not, the
		 * structure will break)
		 * 
		 */
		public void addChild(BinomialNode child)
		{
			if (this.child == null)
			{
				this.child = child;
				child.setSibling(child);
				this.rank++;
				return;
			}
			BinomialNode oldChild = this.child;
			child.setSibling(oldChild.getSibling());
			oldChild.setSibling(child);
			this.child = child;
			this.rank++;
		}
	}

	private BinomialNode head;
	private Map<Integer, BinomialNode> nodes;
	private int min;

	public BinomialHeap()
	{
		this.head = new BinomialNode(-42);
		this.nodes = new HashMap<Integer, BinomialNode>();
		this.min = -1;
	}

	public BinomialHeap(BinomialNode node)
	{
		this.head = new BinomialNode(-42);
		this.head.sibling = node;
		this.nodes = new HashMap<Integer, BinomialNode>();
		this.nodes.put(node.getValue(), node);
		this.min = node.getValue();
	}

	/**
	 * public boolean empty()
	 *
	 * precondition: none
	 * 
	 * The method returns true if and only if the heap is empty.
	 * 
	 */
	public boolean empty()
	{
		return this.head.sibling == null;
	}

	/**
	 * public void insert(int value)
	 *
	 * Insert value into the heap
	 *
	 */
	public void insert(int value)
	{
		BinomialHeap h = new BinomialHeap(new BinomialNode(value));
		this.meld(h);
	}

	/**
	 * public void deleteMin()
	 *
	 * Delete the minimum value
	 *
	 */
	public void deleteMin()
	{
		BinomialNode minNode = this.nodes.get(this.min), temp = minNode;
		while (temp.getSibling() != minNode)
			temp = temp.getSibling();
		temp.setSibling(minNode.getSibling());
		BinomialNode newNodes = minNode.getChild().getSibling();
		BinomialHeap newHeap = new BinomialHeap(newNodes);
		this.meld(newHeap);
		this.nodes.remove(this.min);
		updateMin();
	}

	/**
	 * public int findMin()
	 *
	 * Return the minimum value
	 *
	 */
	public int findMin()
	{
		return this.min;
	}

	private void updateMin()
	{

		BinomialNode x = this.head.getSibling();
		int m = x.getValue();
		while (x.getSibling() != null)
		{
			x = x.getSibling();
			if (x.getValue() < m)
				m = x.getValue();
		}
		this.min = m;
	}

	/**
	 * public void meld (BinomialHeap heap2)
	 *
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2)
	{
		// Here we turn the circular list that connects the "forests" of each
		// heap into a non-circular one, so it'l be easier to detect their ends:
		BinomialNode temp = this.head;
		do
		{
			temp = temp.getSibling();
		} while (temp.getSibling() != this.head.getSibling());
		temp.setSibling(null);
		temp = heap2.head;
		do
		{
			temp = temp.getSibling();
		} while (temp.getSibling() != heap2.head.getSibling());
		temp.setSibling(null);

		BinomialNode h1 = this.head, h2 = heap2.head.getSibling();

		while (h2 != null && h1.getSibling() != null)
		{
			BinomialNode next2 = h2.getSibling();
			if (h1.getSibling().getRank() > h2.getRank()) // Add tree as it is
			{
				h2.setSibling(h1.getSibling());
				h1.setSibling(h2);
			}
			if (h1.getSibling().getRank() < h2.getRank()) // We passed current
															// tree's rank -
															// check next one
			{
				h1 = h1.getSibling();
			}
			if (h1.getSibling().getRank() == h2.getRank()) // We need to merge
															// the trees!
			{
				BinomialNode lowerNode, higherNode;
				if (h1.getSibling().getValue() < h2.getValue())
				{
					lowerNode = h1.getSibling();
					higherNode = h2;
				} else
				{
					lowerNode = h2;
					higherNode = h1.getSibling();
				}
				lowerNode.setSibling(h1.getSibling().getSibling());
				h1.setSibling(lowerNode);
				lowerNode.addChild(higherNode);
				while (h1.getSibling().getSibling() != null
						&& h1.getSibling().getRank() == h1.getSibling().getSibling().getRank())
				{ // The tree we created needs to be merged again
					if (h1.getSibling().getValue() < h1.getSibling().getSibling().getValue())
					{
						lowerNode = h1.getSibling();
						higherNode = h1.getSibling().getSibling();
					} else
					{
						lowerNode = h1.getSibling().getSibling();
						higherNode = h1.getSibling();
					}
					lowerNode.setSibling(h1.getSibling().getSibling().getSibling());
					h1.setSibling(lowerNode);
					lowerNode.addChild(higherNode);
				}
			}
			h2 = next2;
		}
		if (h1.getSibling() == null) // We finished because we reached the last
										// tree in this heap, there is still
										// stuff to add!
			while (h2 != null)
			{
				BinomialNode next2 = h2.getSibling();
				h2.setSibling(h1.getSibling());
				h1.setSibling(h2);
				h2 = next2;
			}
		for (int key : heap2.nodes.keySet()) // Update nodes map:
		{
			this.nodes.put(key, heap2.nodes.get(key));
		}
		// Finally, we turn the "forest" back into a circular list:
		temp = this.head;
		do
		{
			temp = temp.getSibling();
		} while (temp.getSibling() != null);
		temp.setSibling(this.head.getSibling());
	}

	/**
	 * public int size()
	 *
	 * Return the number of elements in the heap
	 * 
	 */
	public int size()
	{
		return this.nodes.size();
	}

	/**
	 * public int minTreeRank()
	 *
	 * Return the minimum rank of a tree in the heap.
	 * 
	 */
	public int minTreeRank()
	{
		return this.head.getSibling().getRank();
	}

	/**
	 * public boolean[] binaryRep()
	 *
	 * Return an array containing the binary representation of the heap.
	 * 
	 */
	public boolean[] binaryRep()
	{
		if (this.empty())
		{
			return new boolean[0];
		}
		BinomialNode n = this.head.getSibling();
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
	 * public void arrayToHeap()
	 *
	 * Insert the array to the heap. Delete previous elemnts in the heap.
	 * 
	 */
	public void arrayToHeap(int[] array)
	{
		return; // TODO Implement this
	}

	/**
	 * public boolean isValid()
	 *
	 * Returns true if and only if the heap is valid.
	 * 
	 */
	public boolean isValid()
	{
		return false; // TODO Implement this
	}

	/**
	 * public void delete(int value)
	 *
	 * Delete the element with the given value from the heap, if such an element
	 * exists. If the heap doen't contain an element with the given value, don't
	 * change the heap.
	 *
	 */
	public void delete(int value)
	{
		if (!this.nodes.containsKey(value))
			return;
		if (value == this.min)
		{
			this.deleteMin();
			return;
		}
		this.decreaseKey(value, -1);
		this.deleteMin();
	}

	/**
	 * public void decreaseKey(int oldValue, int newValue)
	 *
	 * If the heap doen't contain an element with value oldValue, don't change
	 * the heap. Otherwise decrease the value of the element whose value is
	 * oldValue to be newValue. Assume newValue <= oldValue.
	 */
	public void decreaseKey(int oldValue, int newValue)
	{
		if (!this.nodes.containsKey(oldValue) || oldValue == newValue)
			return;
		if (!this.nodes.containsKey(oldValue))
			return; // TODO Implement non-trivial cases, probably using a helper
					// method that
					// "Bubbles up problems". Update the minimum value!
	}

}