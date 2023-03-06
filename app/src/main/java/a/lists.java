package a;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import mpop.revii.itsmypoem.R;
import android.view.LayoutInflater;
import org.json.JSONObject;
import org.json.JSONException;

public class lists extends ArrayAdapter<JSONObject>{
	Context x;
	public lists(Context ctx, ArrayList<String> set){
		super(ctx, R.layout.lists_view, set);
		x = ctx;
	}
	public View getView(int pos, View v, ViewGroup vg){
		v = LayoutInflater.from(x).inflate(R.layout.lists_view, vg, false);
		TextView title = v.findViewById(R.id.title);
		TextView author = v.findViewById(R.id.author);
		try {
			JSONObject o = getItem(pos);
			title.setText(o.getString("title"));
			author.setText(o.getString("author"));
		} catch (JSONException e) {}
		return v;
	}
}
