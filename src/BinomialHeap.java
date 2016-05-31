import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;;

/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers. Based on
 * exercise from previous semester.
 */
public class BinomialHeap
{
	public class HeapNode
	{
		private int value;
		private HeapNode parent;
		private HeapNode sibling;
		private HeapNode child;
		private int rank;

		public HeapNode(int value)
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

		public HeapNode getParent()
		{
			return parent;
		}

		public void setParent(HeapNode parent)
		{
			this.parent = parent;
		}

		public HeapNode getSibling()
		{
			return sibling;
		}

		public void setSibling(HeapNode sibling)
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

		public HeapNode getChild()
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
		public void addChild(HeapNode child)
		{
			if (this.child == null)
			{
				this.child = child;
				child.setSibling(child);
				this.rank++;
				return;
			}
			HeapNode oldChild = this.child;
			child.setSibling(oldChild.getSibling());
			oldChild.setSibling(child);
			this.child = child;
			this.rank++;
		}

		public boolean isValidRoot()
		{
			if (this.getChild() == null)
				return this.getRank() == 0;
			HeapNode child = this.getChild().getSibling();
			boolean[] seenRanks = new boolean[this.getRank()];
			do
			{
				if (!child.isValidRoot() || child.getRank() >= this.getRank() || seenRanks[child.getRank()]
						|| child.getValue() <= this.getValue())
					return false;
				seenRanks[child.getRank()] = true;
			} while (child != this.getChild());
			for (boolean seen : seenRanks)
				if (!seen)
					return false;
			return true;
		}
	}

	private HeapNode head;
	private Map<Integer, HeapNode> nodes;
	private int min;

	public BinomialHeap()
	{
		this.head = new HeapNode(-42);
		this.nodes = new HashMap<Integer, HeapNode>();
		this.min = -1;
	}

	public BinomialHeap(HeapNode node)
	{
		this.head = new HeapNode(-42);
		this.head.sibling = node;
		this.nodes = new HashMap<Integer, HeapNode>();
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
		BinomialHeap h = new BinomialHeap(new HeapNode(value));
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
		HeapNode minNode = this.nodes.get(this.min), temp = minNode;
		while (temp.getSibling() != minNode)
			temp = temp.getSibling();
		temp.setSibling(minNode.getSibling());
		HeapNode newNodes = minNode.getChild().getSibling();
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

		HeapNode x = this.head.getSibling();
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
		HeapNode temp = this.head;
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

		HeapNode h1 = this.head, h2 = heap2.head.getSibling();

		while (h2 != null && h1.getSibling() != null)
		{
			HeapNode next2 = h2.getSibling();
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
				HeapNode lowerNode, higherNode;
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
				HeapNode next2 = h2.getSibling();
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
	 * public void arrayToHeap()
	 *
	 * Insert the array to the heap. Delete previous elements in the heap.
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
		HeapNode node = this.head;
		Set<Integer> seenRanks = new HashSet<Integer>();
		while (node.getSibling() != null)
		{
			node = node.getSibling();
			if (seenRanks.contains(node.getRank()) || !node.isValidRoot())
				return false;
			seenRanks.add(node.getRank());
		}
		return true;
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
		if ((!this.nodes.containsKey(oldValue)) || oldValue == newValue)
			return;
		HeapNode problem = this.nodes.get(oldValue);
		this.nodes.remove(oldValue);
		problem.setValue(newValue);
		this.nodes.put(newValue, problem);
		while (problem.getParent() != null && problem.getValue() <= problem.getParent().getValue())
		{
			int parentValue = problem.getParent().getValue();
			problem.getParent().setValue(newValue);
			problem.setValue(parentValue);
			this.nodes.remove(newValue);
			this.nodes.remove(parentValue);
			this.nodes.put(newValue, problem.getParent());
			this.nodes.put(parentValue, problem);
			problem = problem.getParent();
		}
	}

}