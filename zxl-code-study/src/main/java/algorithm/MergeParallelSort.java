package algorithm;

import java.util.concurrent.CountDownLatch;

/**
 * @author zuoxiaolong
 *
 */
public abstract class MergeParallelSort {
	
	private static final int maxAsynDepth = (int)(Math.log(Runtime.getRuntime().availableProcessors())/Math.log(2));
	
	public static void sort(int[] numbers) {
		sort(numbers, maxAsynDepth);
	}
	
	public static void sort(int[] numbers,Integer asynDepth) {
		sortParallel(numbers, 0, numbers.length, asynDepth > maxAsynDepth ? maxAsynDepth : asynDepth, 1);
	}

	public static void sortParallel(final int[] numbers,final int pos,final int end,final int asynDepth,final int depth){
		if ((end - pos) > 1) {
			final CountDownLatch mergeSignal = new CountDownLatch(2);
			final int offset = (end + pos) / 2;
			Thread thread1 = new SortThread(depth, asynDepth, numbers, mergeSignal, pos, offset);
			Thread thread2 = new SortThread(depth, asynDepth, numbers, mergeSignal, offset, end);
			thread1.start();
			thread2.start();
			try {
				mergeSignal.await();
			} catch (InterruptedException e) {}
			MergeSort.merge(numbers, pos, offset, end);
		}
	}
	
	static class SortThread extends Thread {

		private int depth;
		
		private int asynDepth;
		
		private int[] numbers;
		
		private CountDownLatch mergeSignal;
		
		private int pos;
		
		private int end;
		
		/**
		 * @param depth
		 * @param asynDepth
		 * @param numbers
		 * @param mergeSignal
		 * @param pos
		 * @param end
		 */
		public SortThread(int depth, int asynDepth, int[] numbers, CountDownLatch mergeSignal, int pos, int end) {
			super();
			this.depth = depth;
			this.asynDepth = asynDepth;
			this.numbers = numbers;
			this.mergeSignal = mergeSignal;
			this.pos = pos;
			this.end = end;
		}

		@Override
		public void run() {
			if (depth < asynDepth) {
				sortParallel(numbers,pos,end,asynDepth,(depth + 1));
			} else {
				MergeSort.sort(numbers, pos, end);
			}
			mergeSignal.countDown();
		}
		
	}
	
}
