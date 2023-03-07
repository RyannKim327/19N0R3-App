package a;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.Color;
import android.widget.Toast;
import android.graphics.drawable.ColorDrawable;
import mpop.revii.itsmypoem.R;
import android.widget.TextView;
import java.util.ArrayList;

public class a extends Activity {
	SharedPreferences pref;
	lists array;
	ArrayList l;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new err(this));
		
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c58940")));
		getActionBar().setTitle("Write Poetry");
		getActionBar().setSubtitle("Developed by RyannKim327");
		getWindow().setStatusBarColor(Color.parseColor("#c58940"));
		
		pref = getSharedPreferences("MPOP", MODE_PRIVATE);
		LinearLayout layout = new LinearLayout(this);
		final EditText filter = new EditText(this);
		final ListView list = new ListView(this);
		
		layout.setBackgroundColor(Color.parseColor("#faf881"));
		
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
						try {
							String x = p1.getItemAtPosition(p3).toString();
							JSONObject o = new JSONObject(x);
							AlertDialog.Builder b = new AlertDialog.Builder(a.this);
							b.setTitle(o.getString("title"));
							b.setMessage(o.getString("content"));
							b.setPositiveButton("Close", null);
							b.setCancelable(false);
							//b.show();
							AlertDialog d = b.create();
							float f = 15;
							ShapeDrawable s = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
							s.getPaint().setColor(Color.parseColor("#f1c28a"));
							d.getWindow().setBackgroundDrawable(s);
							d.show();
						} catch (JSONException e) {}
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
									try {
										JSONObject o = lists.getJSONObject(p3);
										AlertDialog.Builder b = new AlertDialog.Builder(a.this);
										b.setTitle(o.getString("title") + "\nBy: " + o.getString("author"));
										b.setMessage(o.getString("content"));
										b.setPositiveButton("Close", null);
										b.setCancelable(false);
										//b.show();
										AlertDialog d = b.create();
										float f = 15;
										ShapeDrawable s = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
										s.getPaint().setColor(Color.parseColor("#f1c28a"));
										d.getWindow().setBackgroundDrawable(s);
										d.show();
									} catch (JSONException e) {}
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
}
