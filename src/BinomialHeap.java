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

		public void setRank(int rank)
		{
			this.rank = rank;
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
		return this.head.sibling == this.head;
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
		if (temp == minNode)
			this.head.setSibling(this.head);
		else
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
		if (this.empty())
		{
			this.min = -1;
			return;
		}
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
		if (heap2.empty())
			return;
		if (this.empty())
		{
			this.head = heap2.head;
			this.nodes = heap2.nodes;
			this.min = heap2.min;
			return;
		}
		HeapNode node1 = this.decapitate().head.getSibling(), node2 = heap2.decapitate().head.getSibling(),
				newHead = new HeapNode(-42), newLast = newHead, carry = null;
		while (node1 != null && node2 != null)
		{
			if (node1.getRank() < node2.getRank())
			{
				if (carry != null && carry.getRank() < node1.getRank())
				// carry is smallest, add to new and reset.
				{
					newLast.setSibling(carry);
					newLast = newLast.getSibling();
					carry = null;
				}
				if (carry == null || carry.getRank() > node1.getRank())
				// node1 is smallest, add to new and advance.
				{
					HeapNode temp = node1.getSibling();
					newLast.setSibling(node1);
					newLast = newLast.getSibling();
					node1 = temp;
				}
				if (carry != null && carry.getRank() == node1.getRank())
				// node1 and carry are of equal ranks, merge and make new carry,
				// then advance.
				{
					HeapNode temp = node1.getSibling();
					carry = mergeTrees(node1, carry);
					node1 = temp;
				}
				continue;
			}
			if (node1.getRank() > node2.getRank())
			{
				if (carry != null && carry.getRank() < node2.getRank())
				// carry is smallest, add to new and reset.
				{
					newLast.setSibling(carry);
					newLast = newLast.getSibling();
					carry = null;
				}
				if (carry == null || carry.getRank() > node2.getRank())
				// node2 is smallest, add to new and advance.
				{
					HeapNode temp = node2.getSibling();
					newLast.setSibling(node2);
					newLast = newLast.getSibling();
					node2 = temp;
				}
				if (carry != null && carry.getRank() == node2.getRank())
				// node2 and carry are of equal rank, merge and make new carry,
				// then advance.
				{
					HeapNode temp = node2.getSibling();
					carry = mergeTrees(node2, carry);
					node2 = temp;
				}
				continue;
			}
			if (node1.getRank() == node2.getRank())
			{
				if (carry != null && carry.getRank() < node1.getRank())
				// carry is smallest, add to new and reset (will merge in next
				// iteration).
				{
					newLast.setSibling(carry);
					newLast = newLast.getSibling();
					carry = null;
				}
				if (carry != null && carry.getRank() == node1.getRank())
				// all 3 are of equal rank, add node1 to new and merge the 2
				// others, setting them to be the new carry, and finally
				// advance.
				{
					HeapNode temp = node1.getSibling();
					newLast.setSibling(node1);
					node1 = temp;
					temp = node2.getSibling();
					carry = mergeTrees(node2, temp);
					node2 = temp;
				}
				if (carry == null)
				// carry doesn't worry us (it's impossible that it's rank will
				// be higher than node1 and node2's, merge node1, node2, add the
				// result to new then advance.
				{
					HeapNode temp1 = node1.getSibling(), temp2 = node2.getSibling();
					carry = mergeTrees(node1, node2);
					node1 = temp1;
					node2 = temp2;
				}
				continue;
			}
		}
		if (node1 == null && node2 == null)
		{
			if (carry != null)
			{
				newLast.setSibling(carry);
				newLast = newLast.getSibling();
				carry = null;
			}
		} else
		{
			HeapNode remaining;
			if (node1 != null)
				remaining = node1;
			else
				remaining = node2;
			while (remaining != null && carry != null)
			{
				if (remaining.getRank() < carry.getRank())
				{
					HeapNode temp = remaining.getSibling();
					newLast.setSibling(remaining);
					newLast = newLast.getSibling();
					remaining = temp;
				}
				if (remaining.getRank() > carry.getRank())
				{
					newLast.setSibling(carry);
					newLast = newLast.getSibling();
					carry = null;
				}
				if (remaining.getRank() == carry.getRank())
				{
					HeapNode temp = remaining.getSibling();
					carry = mergeTrees(carry, remaining);
					remaining = temp;
				}
			}
			if (carry != null)
			{
				newLast.setSibling(carry);
				newLast = newLast.getSibling();
			}
			if (remaining != null)
			{
				newLast.setSibling(remaining);
				while (newLast.getSibling() != null)
					newLast = newLast.getSibling();
			}
		}
		newLast.setSibling(newHead.getSibling());
		this.head = newHead;
		for (Integer key : heap2.nodes.keySet())
			this.nodes.put(key, heap2.nodes.get(key));

	}

	/**
	 * private static HeapNode mergeTrees(HeapNode root1, HeapNode root2)
	 * 
	 * merges the 2 binomial trees who's roots are root1, root2 into 1 tree and
	 * returns it's root. Assuming root1, root2 are of equal rank and diffrent
	 * values.
	 */
	private static HeapNode mergeTrees(HeapNode root1, HeapNode root2)
	{
		if (root1.getValue() < root2.getValue())
		{
			root1.addChild(root2);
			return root1;
		} else
		{
			root2.addChild(root1);
			return root2;
		}
	}

	/**
	 * private static HeapNode decapitate(BinomialHeap heap) breaks the loop at
	 * the list of trees that makes up the heap and returns the head
	 */
	private BinomialHeap decapitate()
	{
		return this;
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