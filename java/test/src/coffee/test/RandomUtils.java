package coffee.test;

import java.util.Random;

public class RandomUtils {
	public static void main(String args[]) {
		Random rnd = new Random();
		int[] nums = new int[2];
		for (int i = 0; i < 2; i++) {
			int p = rnd.nextInt(1);
			if (nums[p] != 0)
				i--;
			else
				nums[p] = i;
		}
		for (int j = 0; j < 50; j++) {
			int a ;
			 a = (int)(Math.random()*6);
			 System.out.println(a);

		}

	}

}
