package com.android.calculator2;

import android.view.View.OnClickListener;
import android.widget.TextView;

public interface CalculatorInterface {
	public void adjustFontSize(TextView view);

	public OnClickListener getOnClickListener();
}
