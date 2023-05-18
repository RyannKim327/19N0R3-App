package a;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import mpop.revii.itsmypoem.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class a extends Activity {
	
	SharedPreferences pref;
	lists array;
	ArrayList l;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new err(this));
		
		setTheme(R.style.MPOPThemeDefault);
		getActionBar().setBackgroundDrawable(new ColorDrawable(R.color.mainBackground));
		getActionBar().setTitle("Write Poetry");
		getActionBar().setSubtitle("Developed by RyannKim327");
		// getWindow().setStatusBarColor(R.color.mainBackground);
		
		pref = getSharedPreferences("MPOP", MODE_PRIVATE);
		LinearLayout layout = new LinearLayout(this);
		final EditText filter = new EditText(this);
		final ListView list = new ListView(this);
		
		//layout.setBackgroundColor(R.color.mainBackground);
		
		try{
			final JSONArray lists = obj().getJSONArray("poems");
			l = new ArrayList<String>();
			array = new lists(this, l);
			
			for(int i = 0; i < lists.length(); i++){
				JSONObject obj = lists.getJSONObject(i);
				l.add(obj);
			}
		
			layout.setOrientation(LinearLayout.VERTICAL);
		
			filter.setSingleLine();
			filter.setHint("Search here");
			filter.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				@Override
				public void afterTextChanged(Editable p1) {
					String s = filter.getText().toString().toLowerCase();
					l.clear();
					array.clear();
					array.notifyDataSetChanged();
					array.notifyDataSetInvalidated();
					try{
						if(s.isEmpty()){
							for(int i = 0; i < lists.length(); i++){
								JSONObject obj = lists.getJSONObject(i);
								l.add(obj);
							}
						}else{
							for(int i = 0; i < lists.length(); i++){
								JSONObject obj = lists.getJSONObject(i);
								if(obj.getString("title").toLowerCase().contains(s) || obj.getString("author").toLowerCase().contains(s)){
									l.add(obj);
								}
							}
						}
						array.notifyDataSetChanged();
						array.notifyDataSetInvalidated();
						list.setAdapter(array);
					}catch(JSONException e){}
				}
			});
		
			list.setAdapter(array);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
						dialog(p1, p3);
					}
				});
				
		} catch (JSONException e) {}
		
		layout.addView(filter);
		layout.addView(list);
		
		setContentView(layout);
		try{
			http h = new http(this);
			h.execute();
		}catch(Exception e){}
		
		registerReceiver(new BroadcastReceiver(){
				@Override
				public void onReceive(Context p1, Intent p2) {
					String data = p2.getStringExtra("data");
					try{
						l.clear();
						array.clear();
						final JSONArray lists = obj().getJSONArray("poems");
						array = new lists(a.this, l);
						
						for(int i = 0; i < lists.length(); i++){
							JSONObject obj = lists.getJSONObject(i);
							l.add(obj);
						}
						array.notifyDataSetChanged();
						list.setAdapter(array);
						list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
								@Override
								public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
									dialog(p1, p3);
								}
							});
					} catch (JSONException e) {}
				}
			}, new IntentFilter("mpop.revii.CHANGE"));
		
	}
	JSONObject obj(){
		try {
			JSONObject obj = new JSONObject(pref.getString("data", "{poems:[]}"));
			return obj;
		} catch (JSONException e) {
			return null;
		}
	}
	boolean check_application(String packagename){
		try{
			createPackageContext(packagename, 0);
			return true;
		}catch(PackageManager.NameNotFoundException e){
			return false;
		}
	}
	void dialog(AdapterView<?> p1, int p3){
		try {
			String x = p1.getItemAtPosition(p3).toString();
			final JSONObject o = new JSONObject(x);
			AlertDialog.Builder b = new AlertDialog.Builder(a.this);
			LinearLayout title = new LinearLayout(a.this);
			ScrollView msg = new ScrollView(a.this);
			TextView title1 = new TextView(a.this);
			TextView subtitle = new TextView(a.this);
			TextView message = new TextView(a.this);

			title.setOrientation(LinearLayout.VERTICAL);
			title.setPadding(15, 0, 15, 0);

			title1.setText(o.getString("title"));
			subtitle.setText(o.getString("author"));

			title1.setTypeface(Typeface.SERIF, Typeface.BOLD_ITALIC);
			subtitle.setTypeface(Typeface.SERIF, Typeface.ITALIC);

			title1.setTextSize(25);
			subtitle.setTextSize(title1.getTextSize() / 3f);

			subtitle.setPadding(25, 0, 0, 0);

			title.addView(title1);
			title.addView(subtitle);

			message.setText(o.getString("content"));
			message.setGravity(Gravity.CENTER);
			message.setTextSize(19);
			message.setPadding(5, 0, 5, 0);

			msg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 25));
			msg.addView(message);

			b.setView(msg);
			b.setPositiveButton("Close", null);
			if(check_application("mpop.revii.ignoreph")){
				b.setNeutralButton("Update Poem", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2) {
							try {
								Intent intent = new Intent(Intent.ACTION_SEND);
								intent.putExtra("title", o.getString("title"));
								intent.putExtra("author", o.getString("author"));
								intent.putExtra("content", o.getString("content"));
								intent.putExtra("id", o.getInt("id"));
								intent.setType("text/plain");
								intent.setPackage("mpop.revii.ignoreph");
								Intent i2 = Intent.createChooser(intent, "Please Choose Writer App");
								startActivity(i2);
							} catch (JSONException e) {}
						}
					});
			}
			b.setCancelable(false);
			//b.show();
			b.setCustomTitle(title);
			AlertDialog d = b.create();
			float f = 15;
			ShapeDrawable s = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
			//s.getPaint().setColor(R.color.mainBackground);
			d.getWindow().setBackgroundDrawable(s);
			d.show();
		} catch (JSONException e) {}
	}
}
