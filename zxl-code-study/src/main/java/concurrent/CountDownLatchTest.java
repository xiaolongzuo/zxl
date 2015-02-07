package concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @author zuoxiaolong
 *
 */
public class CountDownLatchTest {
	
	public static void main(String[] args) throws InterruptedException {
		final CountDownLatch countDownLatch = new CountDownLatch(10);
		for (int i = 0; i < 10; i++) {
			final int number = i + 1;
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
					System.out.println("执行任务[" + number + "]");
					countDownLatch.countDown();
					System.out.println("完成任务[" + number + "]");
				}
			};
			Thread thread = new Thread(runnable);
			thread.start();
		}
		System.out.println("主线程开始等待...");
		countDownLatch.await();
		System.out.println("主线程执行完毕...");
	}
	
}
