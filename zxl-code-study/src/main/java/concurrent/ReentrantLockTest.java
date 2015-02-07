package concurrent;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zuoxiaolong
 *
 */
public class ReentrantLockTest {
    
    private ReentrantLock lock = new ReentrantLock();

    private List<String> someFields1;
    private List<String> someFields2;
    
    public void add(String someText) {
        //some code
    	lock.lock();
    	try {
    		someFields1.add(someText);
            someFields2.add(someText);
		} finally {
			lock.unlock();
		}
        //some code
    }
    
    public Object[] getSomeFields() {
        //some code
        Object[] objects1 = null;
        Object[] objects2 = null;
        lock.lock();
        try {
        	objects1 = someFields1.toArray();
            objects2 = someFields2.toArray();
		} finally {
			lock.unlock();
		}
        Object[] objects = new Object[someFields1.size() + someFields2.size()];
        System.arraycopy(objects1, 0, objects, 0, objects1.length);
        System.arraycopy(objects2, 0, objects, objects1.length, objects2.length);
        return objects;
    }
    
}
