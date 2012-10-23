package com.android.calculator2;

import android.view.View;
import android.view.ViewGroup;

public class Utils {

	public static void setupButtons(ViewGroup parent,
			CalculatorInterface calculator) {
		if (parent.getChildCount() > 0) {
			for (int i = 0; i < parent.getChildCount(); ++i) {
				View view = parent.getChildAt(i);
				if (view instanceof ColorButton) {
					((ColorButton) view).setCalculator(calculator);
				} else if (view instanceof ViewGroup) {
					Utils.setupButtons((ViewGroup) view, calculator);
				}
			}
		}
	}

}
