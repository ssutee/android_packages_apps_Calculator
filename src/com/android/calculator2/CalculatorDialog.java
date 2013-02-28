package com.android.calculator2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.calculator2.CalculatorDisplay.Scroll;

import java.math.BigDecimal;

public class CalculatorDialog extends AlertDialog implements
		CalculatorInterface {

	protected static final String TAG = "CalculatorDialog";

	private OnNumberEnterListener onNumberEnterListener;

	EventListener mListener = new EventListener();
	private int mWidth, mHeight;
	private Persist mPersist;
	private History mHistory;
	private CalculatorDisplay mDisplay;
	private Logic mLogic;
	private PanelSwitcher mPanelSwitcher;

	public CalculatorDialog(Context context, int displayWidth,
			int displayHeight, String done, String cancel, String number) {
		super(context);

		mWidth = displayWidth;
		mHeight = displayHeight;

		ViewGroup view = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.main, null);
		view.setBackgroundColor(Color.BLACK);

		mPersist = new Persist(context);
		mHistory = mPersist.history;

		mDisplay = (CalculatorDisplay) view.findViewById(R.id.display);

		mLogic = new Logic(context, mHistory, mDisplay,
				(Button) view.findViewById(R.id.equal));
		HistoryAdapter historyAdapter = new HistoryAdapter(context, mHistory,
				mLogic);
		mHistory.setObserver(historyAdapter);

		mPanelSwitcher = (PanelSwitcher) view.findViewById(R.id.panelswitch);
		mPanelSwitcher.setCurrentIndex(0);

		mListener.setHandler(mLogic, mPanelSwitcher);

		mDisplay.setOnKeyListener(mListener);
		mDisplay.setCalculator(this);

		if (number != null) {
			mDisplay.setText(number, Scroll.NONE);
		}

		Utils.setupButtons(view, this);

		setView(view, 0, 0, 0, 0);

		Button btnEnter = (Button) view.findViewById(R.id.btnDone);
		btnEnter.setText(done);
		btnEnter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onNumberEnterListener != null) {
					double number = 0;
					try {
                        String input = mLogic.onEnter();
                        Log.i(TAG, input);
                        number = new BigDecimal(input.replace(Logic.MINUS, '-')).doubleValue();
                        Log.i(TAG, new BigDecimal(number).toPlainString());
                    } catch (NumberFormatException e) {
						e.printStackTrace();
					}
					onNumberEnterListener.onNumberEnter(CalculatorDialog.this,
							number);
				}
				dismiss();
			}
		});

		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				mHistory.clear();
			}
		});
	}

	public void setOnNumberEnterListener(
			OnNumberEnterListener onNumberEnterListener) {
		this.onNumberEnterListener = onNumberEnterListener;
	}

	@Override
	public void adjustFontSize(TextView view) {
		float fontPixelSize = view.getTextSize();
		int h = Math.min(mWidth, mHeight);
		float ratio = (float) h / Constants.HVGA_WIDTH_PIXELS;
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontPixelSize * ratio);
	}

	@Override
	public android.view.View.OnClickListener getOnClickListener() {
		return mListener;
	}

	public interface OnNumberEnterListener {
		void onNumberEnter(CalculatorDialog dialog, double number);
	}
}
