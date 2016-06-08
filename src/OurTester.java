
public class OurTester
{
	static int LINK_COUNT = 0;

	public static void main(String[] args)
	{
		for (int i = 1000; i <= 3000; i += 1000)
		{
			LINK_COUNT = 0;
			BinomialHeap h = new BinomialHeap();
			for (int j = 0; j < i; j++)
			{
				h.insert(j);
			}
			System.out.println(LINK_COUNT);
			for (boolean k : h.binaryRep())
			{
				if (k)
					System.out.print(1);
				else
					System.out.print(0);
			}
			System.out.println("|");
			h.deleteMin();
			for (boolean k : h.binaryRep())
			{
				if (k)
					System.out.print(1);
				else
					System.out.print(0);
			}
			System.out.println("|");
		}
	}

}
