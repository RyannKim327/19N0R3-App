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

public class a extends Activity {
	SharedPreferences pref;
	ArrayAdapter<String> array;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new err(this));
		pref = getSharedPreferences("MPOP", MODE_PRIVATE);
		LinearLayout layout = new LinearLayout(this);
		final EditText filter = new EditText(this);
		final ListView list = new ListView(this);
		
		try{
			final JSONArray lists = obj().getJSONArray("poems");
			array = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
			
			for(int i = 0; i < lists.length(); i++){
				JSONObject obj = lists.getJSONObject(i);
				array.add(obj.getString("title"));
			
			}
		
			layout.setOrientation(LinearLayout.VERTICAL);
		
			filter.setSingleLine();
			filter.setHint("Search here");
			filter.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					array.getFilter().filter(filter.getText().toString());
				}
				@Override
				public void afterTextChanged(Editable p1) {}
			});
		
			list.setAdapter(array);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
						try {
							JSONObject o = lists.getJSONObject(p3);
							AlertDialog.Builder b = new AlertDialog.Builder(a.this);
							b.setTitle(o.getString("title"));
							b.setMessage(o.getString("content"));
							b.setPositiveButton("Close", null);
							b.setCancelable(false);
							b.show();
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
						final JSONArray lists = obj().getJSONArray("poems");
						array = new ArrayAdapter<String>(a.this, android.R.layout.simple_list_item_1);

						for(int i = 0; i < lists.length(); i++){
							JSONObject obj = lists.getJSONObject(i);
							array.add(obj.getString("title"));

						}

						list.setAdapter(array);
						list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
								@Override
								public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
									try {
										JSONObject o = lists.getJSONObject(p3);
										AlertDialog.Builder b = new AlertDialog.Builder(a.this);
										b.setTitle(o.getString("title"));
										b.setMessage(o.getString("content"));
										b.setPositiveButton("Close", null);
										b.setCancelable(false);
										b.show();
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
