import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;;

/**
 * BinomialHeap
 * <p>
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
		 * <p>
		 * precondition: none
		 * <p>
		 * The method adds child as a child of this node. The method assumes the
		 * rank of child and the rank of this are the same (if not, the
		 * structure will break)
		 */
		public void addChild(HeapNode child)
		{
			assert child != null;
			assert this.rank == child.rank;

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
			this.child.setParent(this);
			this.rank++;
		}

		public boolean isValidRoot()
		{
			if (this.getChild() == null)
				return this.getRank() == 0;
			HeapNode first = this.getChild().getSibling();
			HeapNode current = first;
			for (int i = 0; i < this.getRank(); i++)
			{
				if (current.getRank() != i || current.getValue() <= this.getValue() || !current.isValidRoot())
				{
					return false;
				}
				if (current == first && i != 0)
				{
					return false;
				}
				current = current.getSibling();
			}

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

	public BinomialHeap(int value)
	{
		HeapNode node = new HeapNode(value);
		this.head = new HeapNode(-42);
		this.head.sibling = node;
		this.nodes = new HashMap<Integer, HeapNode>();
		this.nodes.put(node.getValue(), node);
		this.min = node.getValue();
	}

	public BinomialHeap(HeapNode node, HashMap<Integer, HeapNode> nodes)
	{
		this.head = new HeapNode(-42);
		this.head.setSibling(node);
		this.nodes = new HashMap<Integer, HeapNode>();
		this.nodes.put(node.getValue(), node);// TODO

		updateMin();
	}

	/**
	 * public boolean empty()
	 * <p>
	 * precondition: none
	 * <p>
	 * The method returns true if and only if the heap is empty.
	 */
	public boolean empty()
	{
		return this.head.sibling == this.head;
	}

	/**
	 * public void insert(int value)
	 * <p>
	 * Insert value into the heap
	 */
	public void insert(int value)
	{
		assert isValid();

		if (this.nodes.get(value) != null)
		{
			return;
		}
		BinomialHeap h = new BinomialHeap(value);
		this.meld(h);

		assert isValid();
	}

	/**
	 * public void deleteMin()
	 * <p>
	 * Delete the minimum value
	 */
	public void deleteMin()
	{
		assert isValid();
		assert this.min != -1 && this.nodes.get(this.min) != null || this.empty();

		if (this.empty())
		{
			return;
		}

		HeapNode minNode = this.nodes.get(this.min);

		HeapNode prev = null;
		HeapNode first = this.head.getSibling();
		HeapNode current = first;
		while (current != minNode)
		{
			prev = current;
			current = current.getSibling();
		}
		if (prev == null)
		{
			if (current.getSibling() == current)
			{
				this.head.setSibling(this.head);
			} else
			{
				this.head.setSibling(current.getSibling());
				while (current.getSibling() != first)
				{
					current = current.getSibling();
				}
				current.setSibling(first.getSibling());
			}
		} else
		{
			prev.setSibling(current.getSibling());
		}

		this.nodes.remove(this.min);
		if (minNode.getChild() != null)
		{
			HeapNode newNodes = minNode.getChild().getSibling();
			BinomialHeap newHeap = new BinomialHeap(newNodes, null);
			this.meld(newHeap);
		}
		updateMin();

		assert isValid();
	}

	/**
	 * public int findMin()
	 * <p>
	 * Return the minimum value
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
	 * Meld the heap with heap2
	 */
	public void meldmaf(BinomialHeap heap2)
	{
		assert isValid();
		assert heap2.isValid();

		if (heap2.empty())
			return;
		if (this.empty())
		{
			this.head = heap2.head;
			this.nodes = heap2.nodes;
			this.min = heap2.min;
			return;
		}
		// System.out.println("Takbir!"); // TODO remove
		HeapNode node1 = this.decapitate().getSibling(), node2 = heap2.decapitate().getSibling(),
				newHead = new HeapNode(-42), newLast = newHead, carry = null;
		// System.out.println("allah uackbar!"); // TODO remove
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
					carry = mergeTrees(node2, carry);
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
					continue;
				}
				if (remaining.getRank() > carry.getRank())
				{
					newLast.setSibling(carry);
					newLast = newLast.getSibling();
					carry = null;
					continue;
				}
				if (remaining.getRank() == carry.getRank())
				{
					HeapNode temp = remaining.getSibling();
					carry = mergeTrees(carry, remaining);
					remaining = temp;
					continue;
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
		this.min = Math.min(this.min, heap2.min);

		assert isValid();
	}

	/**
	 * public void meld (BinomialHeap heap2)
	 * <p>
	 * Meld the heap with heap2
	 */
	public void meld(BinomialHeap heap2)
	{
		HeapNode first = better(this, heap2);
		if (first == null)
		{ // Both heaps are empty
			return;
		}

		HeapNode prev = null;
		HeapNode current = first;
		HeapNode next = first.getSibling();

		while (next != null)
		{
			if (current.getRank() != next.getRank()
					|| next.getSibling() != null && current.getRank() == next.getSibling().getRank())
			{
				prev = current;
				current = current.getSibling();
			} else
			{
				if (current.getValue() < next.getValue())
				{
					current.setSibling(next.getSibling());
					current.addChild(next);
				} else
				{
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

		current.setSibling(first);
		this.head.setSibling(first);
		this.nodes.putAll(heap2.nodes);

		if (this.min != -1)
		{
			if (heap2.min != -1)
			{
				this.min = Math.min(this.min, heap2.min);
			}
		} else
		{
			this.min = heap2.min;
		}
	}

	private void binomialHeapMerge(BinomialHeap h1, BinomialHeap h2)
	{
		HeapNode merged = null;
		if (h1.empty())
		{
			if (h2.empty())
			{
				// return null;
			} else
			{
				merged = h2.head.getSibling();
			}
		} else if (h2.empty())
		{
			merged = h1.head.getSibling();
		} else
		{
			HeapNode first1 = h1.head.getSibling();
			HeapNode first2 = h2.head.getSibling();

			HeapNode current1, current2;

			if (first1.getRank() <= first2.getRank())
			{
				merged = first1;
				current1 = first1.getSibling();
				current2 = first2;
			} else
			{
				merged = first2;
				current1 = first1;
				current2 = first2.getSibling();
			}

			HeapNode current = merged;
			do
			{
				if (current1.getRank() <= current2.getRank())
				{
					merged.setSibling(current1);
				}
			} while (current1 != first1 || current2 != first2);
		}
	}

	private HeapNode better(BinomialHeap h1, BinomialHeap h2)
	{
		if (h1.empty() && h2.empty())
		{
			return null;
		}

		HeapNode l1 = null, l2 = null;
		if (!h1.empty())
		{
			l1 = h1.head.getSibling();
			HeapNode current = l1;
			while (current.getSibling() != l1)
			{
				current = current.getSibling();
			}
			current.setSibling(null);
		}
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
		while (current1 != null || current2 != null)
		{
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
	 * private static HeapNode mergeTrees(HeapNode root1, HeapNode root2)
	 * <p>
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
	private HeapNode decapitate()
	{
		HeapNode temp = this.head;
		if (!this.empty())
		{
			do
			{
				temp = temp.getSibling();
			} while (temp.getSibling() != this.head.getSibling());
		}
		temp.setSibling(null);
		return this.head;
	}

	/**
	 * public int size()
	 * <p>
	 * Return the number of elements in the heap
	 */
	public int size()
	{
		return this.nodes.size();
	}

	/**
	 * public int minTreeRank()
	 * <p>
	 * Return the minimum rank of a tree in the heap.
	 */
	public int minTreeRank()
	{
		return this.head.getSibling().getRank();
	}

	/**
	 * public boolean[] binaryRep()
	 * <p>
	 * Return an array containing the binary representation of the heap.
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
	 * <p>
	 * Insert the array to the heap. Delete previous elements in the heap.
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
	 */
	public boolean isValid()
	{
		// If the heap is empty.
		if (this.head.getSibling() == head)
		{
			if (head.getParent() == null && head.getSibling() == head && head.getChild() == null && this.min == -1)
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
		do
		{
			if (seenRanks.contains(current.getRank()) || !current.isValidRoot())
				return false;
			seenRanks.add(current.getRank());

			current = current.getSibling();
		} while (current != first);

		current = first;
		HeapNode minNode = this.nodes.get(this.min);
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
	 * Delete the element with the given value from the heap, if such an element
	 * exists. If the heap doen't contain an element with the given value, don't
	 * change the heap.
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
	 * <p>
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