package concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zuoxiaolong
 *
 */
public class SemaphoreTest {

	public static void main(String[] args) throws InterruptedException {
		final Semaphore semaphore = new Semaphore(10);
		final AtomicInteger number = new AtomicInteger();
		for (int i = 0; i < 100; i++) {
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
					try {
						semaphore.acquire();
						semaphore.release();
						number.incrementAndGet();
					} catch (InterruptedException e) {}
				}
			};
			Thread thread = new Thread(runnable);
			thread.start();
		}
		Thread.sleep(10000);
		System.out.println("共" + number.get() + "个线程获得到信号");
		System.exit(0);
	}
	
}
