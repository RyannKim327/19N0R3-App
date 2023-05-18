package a;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class http extends AsyncTask {
	Context ctx;
	public http(Context a){
		ctx = a;
	}
	@Override
	protected Object doInBackground(Object[] p1) {
		String x = "";
		String link = "https://poem.writers.repl.co/lists";
		try {
			URL u = new URL(link);
			URLConnection c = u.openConnection();
			BufferedReader b = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String y;
			while((y = b.readLine()) != null){
				x += y;
				break;
			}
			return x;
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public void onPreExecute() {
		super.onPreExecute();
	}
	@Override
	public void onPostExecute(Object result) {
		super.onPostExecute(result);
		if(result != null){
			try{
				JSONObject obj = new JSONObject(result.toString());
				SharedPreferences p = ctx.getSharedPreferences("MPOP", ctx.MODE_PRIVATE);
				if(p.getString("data", "{poems:[]}") != result.toString()){
					p.edit().putString("data", result.toString()).commit();
					Toast.makeText(ctx, "Success", 1).show();
					Intent i = new Intent("mpop.revii.CHANGE");
					i.putExtra("data", result.toString());
					ctx.sendBroadcast(i);
				}
			}catch(JSONException e){
				Toast.makeText(ctx, "Cant acceptable", 1).show();
			}
		}else{
			Toast.makeText(ctx, "Cant acceptable", 1).show();
		}
	}
}
