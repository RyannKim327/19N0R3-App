package a;
import android.app.Activity;
import android.content.ClipboardManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class exe extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_DeviceDefault_Light);
		ScrollView base = new ScrollView(this);
		final TextView tv = new TextView(this);

		tv.setText(getIntent().getStringExtra("error"));
		tv.setTextSize(15);
		tv.setTypeface(Typeface.SERIF, Typeface.BOLD_ITALIC);

		tv.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).setText(tv.getText().toString());
					Toast.makeText(exe.this,"Added to clipboard", 1).show();
				}
			});

		base.addView(tv);
		setContentView(base);
	}
	
}

