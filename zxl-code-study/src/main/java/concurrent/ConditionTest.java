package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zuoxiaolong
 *
 */
public class ConditionTest {

	private static ReentrantLock lock = new ReentrantLock();
	
	public static void main(String[] args) throws InterruptedException {
		final Condition condition1 = lock.newCondition();
		final Condition condition2 = lock.newCondition();
		Thread thread1 = new Thread(new Runnable() {
			public void run() {
				lock.lock();
				try {
					System.out.println("等待condition1被通知！");
					condition1.await();
					System.out.println("condition1已被通知，马上开始通知condition2！");
					condition2.signal();
					System.out.println("通知condition2完毕！");
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} finally {
					lock.unlock();
				}
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				lock.lock();
				try {
					System.out.println("马上开始通知condition1！");
					condition1.signal();
					System.out.println("通知condition1完毕，等待condition2被通知！");
					condition2.await();
					System.out.println("condition2已被通知！");
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} finally {
					lock.unlock();
				}
			}
		});
		thread1.start();
		Thread.sleep(1000);
		thread2.start();
	}

}
