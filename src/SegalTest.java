import java.util.Random;

public class SegalTest
{
	public static void main(String[] args)
	{

		int[] vals = createValues(100);
		BinomialHeap heap1 = new BinomialHeap();
		// System.out.println(heap1);

		for (int v : vals)
		{
			heap1.insert(v);
			// System.out.println(heap1);

			if (!heap1.isValid())
			{
				System.out.println("ERROR INSERT");
			}

			// System.out.println("SUCCESS INSERT");

			// System.out.println("\n");
		}

		// Arrays.sort(vals);

		System.out.println(heap1.nodes);
		for (int v : vals)
		{
			if (!heap1.nodes.containsKey(v - 7) && v - 7 > 0)
				heap1.decreaseKey(v, v - 7);
			// System.out.println(v + " " + (v-7));
			// System.out.println(heap1+ "\n");
			if (!heap1.isValid())
			{
				System.out.println("ERROR DELETEMIN");
			}
		}
		System.out.println(heap1.nodes);

		Integer[] vals2 = heap1.nodes.keySet().toArray(new Integer[vals.length]);
		for (int v : vals2)
		{
			heap1.delete(v);
			// System.out.println(heap1);

			// System.out.println(v + " " + (v-7));
			// System.out.println(heap1+ "\n");
			if (!heap1.isValid())
			{
				System.out.println("ERROR DELETE");
			}
		}

		System.out.println(heap1.empty());

		// System.out.println(heap1.map);
		System.out.println(heap1);
	}

	private static int[] createValues(int n)
	{
		int[] values = new int[n];
		int maxValue = n * 10;
		Random randomGenerator = new Random();

		for (int i = 0; i < n; ++i)
		{
			while (true)
			{
				int j, randInt = randomGenerator.nextInt(maxValue) + 10;

				for (j = 0; j < i && randInt != values[j]; ++j)
					;
				if (j < i)
				{
					continue;
				}
				values[i] = randInt;
				break;
			}
		}

		return values;
	}
}