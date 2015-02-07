package algorithm;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * @author zuoxiaolong
 *
 */
public class SortTests {

	public static void main(String[] args) {
		testAllSortIsCorrect();
		testComputeTime("MergeParallelSort", 40000, 5);
		testComputeTime("MergeSort", 40000, 5);
		testComputeTime("InsertSort", 400, 5);
	}
	
	public static void testAllSortIsCorrect() {
		File classpath = new File(SortTests.class.getResource("").getFile());
		File[] classesFiles = classpath.listFiles();
		for (int i = 0; i < classesFiles.length; i++) {
			if (classesFiles[i].getName().endsWith("Sort.class")) {
				System.out.println("---测试" + classesFiles[i].getName() + "是否有效---");
				testSortIsCorrect(classesFiles[i].getName().split("\\.")[0]);
			}
		}
	}
	
	public static void testSortIsCorrect(String className){
		for (int i = 1; i < 50; i++) {
			int[] numbers = getRandomIntegerArray(1000 * i);
			invoke(numbers, className);
			for (int j = 1; j < numbers.length; j++) {
				if (numbers[j] < numbers[j-1]) {
					throw new RuntimeException(className + " sort is error because " + numbers[j] + "<" + numbers[j-1]);
				}
			}
		}	
		System.out.println("---" + className + "经测试有效---");
	}
	
	public static void testComputeTime(String className,int initNumber,int times,Object... arguments) {
		long[] timeArray = new long[times];
		for (int i = initNumber,j = 0; j < times; i = i * 10,j++) {
			timeArray[j] = computeTime(i, className, arguments);
		}
		System.out.print(className + "时间增加比例：");
		for (int i = 1; i < timeArray.length ; i++) {
			System.out.print((float)timeArray[i]/timeArray[i - 1]);
			if (i < timeArray.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();
	}
	
	public static long computeTime(int length,String className,Object... arguments){
		int[] numbers = getRandomIntegerArray(length);
		long start = System.currentTimeMillis();
		System.out.print("开始计算长度为"+numbers.length+"方法为"+className+"参数为[");
		for (int i = 0; i < arguments.length; i++) {
			System.out.print(arguments[i]);
			if (i < arguments.length - 1) {
				System.out.print(",");
			}
		}
		System.out.print("]，时间为");
		invoke(numbers, className, arguments);
		long time = System.currentTimeMillis()-start;
		System.out.println(time + "ms");
		return time;
	}
	
	public static int[] getRandomIntegerArray(int length){
		int[] numbers = new int[length];
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = new Random().nextInt(length);
		}
		return numbers;
	}
	
	public static void invoke(int[] numbers,String className,Object... arguments){
		try {
			Class<?> clazz = Class.forName("algorithm." + className);
			Class<?>[] parameterTypes = new Class<?>[arguments.length + 1];
			parameterTypes[0] = int[].class;
			for (int i = 0; i < arguments.length; i++) {
				parameterTypes[i + 1] = arguments[i].getClass();
			}
			Method method = clazz.getDeclaredMethod("sort", parameterTypes);
			Object[] parameters = new Object[parameterTypes.length];
			parameters[0] = numbers;
			for (int i = 0; i < arguments.length; i++) {
				parameters[i + 1] = arguments[i];
			}
			method.invoke(null, parameters);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
