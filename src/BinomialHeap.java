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
			this.sibling = null;
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

		public void setChild(BinomialNode child)
		{
			this.child = child;
		}
	}

	private BinomialNode head;
	private Map<Integer, BinomialNode> nodes;

	public BinomialHeap()
	{
		this.head = new BinomialNode(-1);
		this.nodes = new HashMap<Integer, BinomialNode>();
	}

	public BinomialHeap(BinomialNode node)
	{
		this.head = new BinomialNode(-5);
		this.head.sibling = node;
		this.nodes = new HashMap<Integer, BinomialNode>();
		this.nodes.put(node.getValue(), node);
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
		BinomialHeap H = new BinomialHeap(new BinomialNode(value));
		this.meld(H);
	}

	/**
	 * public void deleteMin()
	 *
	 * Delete the minimum value
	 *
	 */
	public void deleteMin()
	{
		return; // TODO Implement this using the map

	}

	/**
	 * public int findMin()
	 *
	 * Return the minimum value
	 *
	 */
	public int findMin()
	{
		return 42; // TODO Implement this
	}

	/**
	 * public void meld (BinomialHeap heap2)
	 *
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2)
	{
		return; // TODO Implement this, don't forget about the map!
	}

	/**
	 * public int size()
	 *
	 * Return the number of elements in the heap
	 * 
	 */
	public int size()
	{
		return 42; // TODO Implement this
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
		boolean[] arr = new boolean[42];
		return arr; // TODO Implement this
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
		return; // TODO Implement this
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
		return; // TODO Implement this, probably using a helper method that
				// "bubles up problems"
	}

}