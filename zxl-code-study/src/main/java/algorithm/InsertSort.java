package algorithm;

/**
 * @author zuoxiaolong
 *
 */
public abstract class InsertSort {

	public static void sort(int[] numbers){
		for (int i = 1; i < numbers.length; i++) {
			int currentNumber = numbers[i];
			int j = i - 1;
			while (j >= 0 && numbers[j] > currentNumber) {
				numbers[j + 1] = numbers[j];
				j--;
			}
			numbers[j + 1] = currentNumber;
		}
	}
	
}
