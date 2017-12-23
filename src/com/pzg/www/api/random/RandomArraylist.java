package com.pzg.www.api.random;

import java.util.ArrayList;
import java.util.Random;

public class RandomArraylist {
	public static int getRandomInt(int min, int max) {
	    Random random = new Random();

	    return random.nextInt((max - min) + 1) + min;
	}

	public static ArrayList<Integer> getRandomNonRepeatingIntegers(int size, int min,
	        int max) {
	    ArrayList<Integer> numbers = new ArrayList<Integer>();

	    while (numbers.size() < size) {
	        int random = getRandomInt(min, max);

	        if (!numbers.contains(random)) {
	            numbers.add(random);
	        }
	    }

	    return numbers;
	}
}