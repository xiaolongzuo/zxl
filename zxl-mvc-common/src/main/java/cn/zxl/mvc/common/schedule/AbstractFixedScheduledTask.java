package cn.zxl.mvc.common.schedule;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

public abstract class AbstractFixedScheduledTask implements ApplicationContextAware {

    private Trigger trigger;
    
    public final void handle() {
        ExecutionTimeMap.getInstance().set(trigger, executeTask());
    }

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		trigger = applicationContext.getBean(getTriggerBeanName(),Trigger.class);
		if (trigger == null) {
			throw new RuntimeException();
		}
	}

	public abstract Date executeTask();
	
	public abstract String getTriggerBeanName();
	
	public static class FixedTrigger implements Trigger{
		
		private static final int DEFAULT_DELAY = 10000;
		
		private int startDelay = DEFAULT_DELAY;
		
		private int repeatInterval = DEFAULT_DELAY;
		
		private final AtomicBoolean started = new AtomicBoolean(false);
		
		public void setStartDelay(int startDelay) {
			this.startDelay = startDelay;
		}

		public void setRepeatInterval(int repeatInterval) {
			this.repeatInterval = repeatInterval;
		}

		public Date nextExecutionTime(TriggerContext triggerContext) {
			if (!started.getAndSet(true)) {
				return new Date(System.currentTimeMillis() + startDelay);
			} else {
				Date nextExecutionTime = ExecutionTimeMap.getInstance().get(this);
				return nextExecutionTime == ExecutionTimeMap.NULL_DATE ? new Date(System.currentTimeMillis() + repeatInterval) : nextExecutionTime;
			}
		}
		
	}

	public static class ExecutionTimeMap {
		
		private static ExecutionTimeMap instance = new ExecutionTimeMap();
		
		private ExecutionTimeMap(){}
		
		public static ExecutionTimeMap getInstance(){
			return instance;
		}
		
		private final Map<Trigger, Date> map = new ConcurrentHashMap<Trigger, Date>();
		
		public static final Date NULL_DATE = new Date();
		
		public void set(Trigger trigger , Date nextExecutionTime){
			if (nextExecutionTime == null) {
				map.put(trigger, NULL_DATE);
			} else {
				map.put(trigger, nextExecutionTime);
			}
		}
		
		public Date get(Trigger trigger){
			return map.get(trigger);
		}
		
	}
	
}
