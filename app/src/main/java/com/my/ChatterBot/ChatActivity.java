package com.my.ChatterBot;

import android.app.Activity;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.EditText;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import com.google.gson.Gson;
import android.content.ClipData;
import android.content.ClipboardManager;
import com.google.gson.reflect.TypeToken;
import java.text.DecimalFormat;

public class ChatActivity extends Activity {
	
	
	private HashMap<String, Object> temp = new HashMap<>();
	private double counter = 0;
	private double position = 0;
	private String temporary = "";
	private String textview = "";
	
	private ArrayList<HashMap<String, Object>> messages = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> chatterbot = new ArrayList<>();
	
	private LinearLayout linear1;
	private Button train;
	private ListView chatterbot_view;
	private LinearLayout linear4;
	private ListView messages_view;
	private HorizontalScrollView hscroll1;
	private LinearLayout linear2;
	private LinearLayout linear5;
	private LinearLayout linear6;
	private EditText trigger;
	private EditText response;
	private Button add;
	private Button paste;
	private Button copy;
	private LinearLayout linear7;
	private Button send;
	private EditText message;
	
	private Calendar cal = Calendar.getInstance();
	private SharedPreferences save;
	private Calendar ping = Calendar.getInstance();
	private Calendar ping2 = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.chat);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		train = (Button) findViewById(R.id.train);
		chatterbot_view = (ListView) findViewById(R.id.chatterbot_view);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		messages_view = (ListView) findViewById(R.id.messages_view);
		hscroll1 = (HorizontalScrollView) findViewById(R.id.hscroll1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		trigger = (EditText) findViewById(R.id.trigger);
		response = (EditText) findViewById(R.id.response);
		add = (Button) findViewById(R.id.add);
		paste = (Button) findViewById(R.id.paste);
		copy = (Button) findViewById(R.id.copy);
		linear7 = (LinearLayout) findViewById(R.id.linear7);
		send = (Button) findViewById(R.id.send);
		message = (EditText) findViewById(R.id.message);
		save = getSharedPreferences("save", Activity.MODE_PRIVATE);
		
		train.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (train.getText().toString().contains("enter")) {
					messages_view.setVisibility(View.GONE);
					linear2.setVisibility(View.GONE);
					chatterbot_view.setVisibility(View.VISIBLE);
					linear4.setVisibility(View.VISIBLE);
					train.setText("exit training mode");
					chatterbot_view.setAdapter(new Chatterbot_viewAdapter(chatterbot));
					((BaseAdapter)chatterbot_view.getAdapter()).notifyDataSetChanged();
					linear1.setBackgroundColor(0xFF424242);
					trigger.requestFocus();
				}
				else {
					messages_view.setVisibility(View.VISIBLE);
					linear2.setVisibility(View.VISIBLE);
					chatterbot_view.setVisibility(View.GONE);
					linear4.setVisibility(View.GONE);
					train.setText("enter training mode");
					linear1.setBackgroundColor(0xFFFAFAFA);
					message.requestFocus();
				}
			}
		});
		
		chatterbot_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				
			}
		});
		
		chatterbot_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				chatterbot.remove((int)(_position));
				((BaseAdapter)chatterbot_view.getAdapter()).notifyDataSetChanged();
				return true;
			}
		});
		
		messages_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				messages.remove((int)(_position));
				((BaseAdapter)messages_view.getAdapter()).notifyDataSetChanged();
				return true;
			}
		});
		
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!response.getText().toString().equals("")) {
					temp = new HashMap<>();
					temp.put("trigger", trigger.getText().toString());
					temp.put("response", response.getText().toString());
					chatterbot.add(temp);
					((BaseAdapter)chatterbot_view.getAdapter()).notifyDataSetChanged();
					chatterbot_view.smoothScrollToPosition((int)(chatterbot.size()));
					trigger.setText("");
					response.setText("");
				}
			}
		});
		
		paste.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!trigger.getText().toString().equals("")) {
					chatterbot = new Gson().fromJson(trigger.getText().toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					((BaseAdapter)chatterbot_view.getAdapter()).notifyDataSetChanged();
				}
			}
		});
		
		copy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", new Gson().toJson(chatterbot)));
			}
		});
		
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				ping = Calendar.getInstance();
				cal = Calendar.getInstance();
				_messageSend_("you", message.getText().toString(), cal);
				_chatterLink(message.getText().toString());
				((BaseAdapter)messages_view.getAdapter()).notifyDataSetChanged();
				messages_view.smoothScrollToPosition((int)(messages.size()));
				message.setText("");
			}
		});
	}
	private void initializeLogic() {
		chatterbot_view.setVisibility(View.GONE);
		linear4.setVisibility(View.GONE);
		temp = new HashMap<>();
		temp.put("trigger", "hi");
		temp.put("response", "hello there!");
		chatterbot.add(temp);
		temp = new HashMap<>();
		temp.put("trigger", "bye");
		temp.put("response", "see ya :)");
		chatterbot.add(temp);
		if (!save.getString("chatterbot", "").equals("")) {
			messages = new Gson().fromJson(save.getString("messages", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			chatterbot = new Gson().fromJson(save.getString("chatterbot", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		}
		messages_view.setAdapter(new Messages_viewAdapter(messages));
		((BaseAdapter)messages_view.getAdapter()).notifyDataSetChanged();
		messages_view.smoothScrollToPosition((int)(messages.size()));
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		save.edit().putString("messages", new Gson().toJson(messages)).commit();
		save.edit().putString("chatterbot", new Gson().toJson(chatterbot)).commit();
	}
	private void _chatterLink (final String _trigger) {
		while(true) {
			counter = 0;
			for(int _repeat10 = 0; _repeat10 < (int)(chatterbot.size()); _repeat10++) {
				if (_trigger.toLowerCase().contains(chatterbot.get((int)counter).get("trigger").toString())) {
					_messageSend_("ChatterBot", chatterbot.get((int)counter).get("response").toString(), cal);
					break;
				}
				else {
					
				}
				counter++;
			}
			if (_trigger.length() > 2) {
				if (_trigger.toLowerCase().substring((int)(0), (int)(3)).equals("say")) {
					_messageSend_("ChatterBot", _trigger.substring((int)(3), (int)(_trigger.length())).trim(), cal);
					break;
				}
				if (_trigger.length() > 3) {
					if (_trigger.toLowerCase().substring((int)(0), (int)(4)).equals("help")) {
						counter = 0;
						temporary = "My Functions are:\nping - bot latency\nsay - make me say anything you want\nhelp - show this help message\n\nMy commands are:\n";
						for(int _repeat54 = 0; _repeat54 < (int)(chatterbot.size()); _repeat54++) {
							temporary = temporary.concat(chatterbot.get((int)counter).get("trigger").toString().concat("\n"));
							counter++;
						}
						_messageSend_("ChatterBot", temporary.concat("long press a message to delete"), cal);
						break;
					}
					if (_trigger.toLowerCase().substring((int)(0), (int)(4)).equals("ping")) {
						ping2 = Calendar.getInstance();
						_messageSend_("ChatterBot", "Pong!\n".concat(String.valueOf((long)(ping2.getTimeInMillis() - ping.getTimeInMillis())).concat(" millisecond(s) latency")), cal);
						break;
					}
				}
			}
			break;
		}
	}
	
	
	private void _messageSend_ (final String _sender, final String _message, final Calendar _time) {
		if (!_message.equals("")) {
			cal = Calendar.getInstance();
			temp = new HashMap<>();
			temp.put("sender", _sender);
			temp.put("message", _message);
			temp.put("time", new SimpleDateFormat("HH:mm:ss dd:MM:YY").format(cal.getTime()));
			messages.add(temp);
			messages_view.smoothScrollToPosition((int)(messages.size()));
		}
	}
	
	
	private void _borderwithround (final View _v, final double _col1, final double _col2, final double _col3, final double _width, final String _color, final double _r) {
		try{
			int[] colors = {Color.rgb((int) _col1,(int) _col2,(int)_col3),Color.rgb((int) _col1,(int) _col2,(int)_col3)};
			android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.BR_TL, colors);
			gd.setCornerRadius((float)_r); 
			gd.setStroke((int)_width, (Color.parseColor(_color)));
			_v.setBackground(gd);
			
		}catch(Exception e) { showMessage(e.toString());}
	}
	
	
	private void _triggers () {
		
	}
	
	
	private void _addTextViewPro2 (final String _name, final String _text, final String _color) {
		//create string textview
		textview = _name;
		final TextView textview = new TextView(ChatActivity.this);
		LinearLayout mylinear = (LinearLayout)findViewById(R.id.linear7);
		textview.setText(_text);
		textview.setTextColor(Color.parseColor(_color));
		textview.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
		int fID; do { fID = textview.generateViewId(); } while (findViewById(fID) != null); textview.setId(fID);
		//linear7 is the LinearLayout where you want add the textview
		linear7.addView((TextView)textview);
		String setID = Integer.toString(fID); 
		//create string setID
		//this is for get ID later you can create array
	}
	
	
	public class Chatterbot_viewAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Chatterbot_viewAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.training_item, null);
			}
			
			final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
			final TextView textview1 = (TextView) _v.findViewById(R.id.textview1);
			final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
			final EditText trigger = (EditText) _v.findViewById(R.id.trigger);
			final EditText response = (EditText) _v.findViewById(R.id.response);
			
			trigger.setText(_data.get((int)_position).get("trigger").toString());
			response.setText(_data.get((int)_position).get("response").toString());
			trigger.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					position = _position;
				}
			});
			response.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					position = _position;
				}
			});
			textview1.setText(String.valueOf((long)(_position)));
			
			return _v;
		}
	}
	
	public class Messages_viewAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Messages_viewAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.chat_item, null);
			}
			
			final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
			final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
			final LinearLayout linear3 = (LinearLayout) _v.findViewById(R.id.linear3);
			final TextView cbmessage = (TextView) _v.findViewById(R.id.cbmessage);
			final ImageView imageview2 = (ImageView) _v.findViewById(R.id.imageview2);
			final TextView textview3 = (TextView) _v.findViewById(R.id.textview3);
			final TextView cbtime = (TextView) _v.findViewById(R.id.cbtime);
			final LinearLayout linear4 = (LinearLayout) _v.findViewById(R.id.linear4);
			final TextView message = (TextView) _v.findViewById(R.id.message);
			final TextView time = (TextView) _v.findViewById(R.id.time);
			final TextView textview5 = (TextView) _v.findViewById(R.id.textview5);
			final ImageView imageview1 = (ImageView) _v.findViewById(R.id.imageview1);
			
			if (_data.get((int)_position).get("sender").toString().equals("you")) {
				message.setText(_data.get((int)_position).get("message").toString());
				time.setText(_data.get((int)_position).get("time").toString());
				linear2.setVisibility(View.GONE);
				linear1.setVisibility(View.VISIBLE);
				android.graphics.drawable.GradientDrawable d = new android.graphics.drawable.GradientDrawable();
				d.setStroke(5, Color.RED);
				d.setCornerRadii(new float[]{25.0f,25.0f,5.0f,5.0f,50.0f,50.0f,25.0f,25.0f});
				message.setBackgroundDrawable(d);
			}
			else {
				cbmessage.setText(_data.get((int)_position).get("message").toString());
				cbtime.setText(_data.get((int)_position).get("time").toString());
				linear1.setVisibility(View.GONE);
				linear2.setVisibility(View.VISIBLE);
				android.graphics.drawable.GradientDrawable d = new android.graphics.drawable.GradientDrawable();
				d.setStroke(5, Color.BLUE);
				d.setCornerRadii(new float[]{5.0f,5.0f,25.0f,25.0f,25.0f,25.0f,50.0f,50.0f});
				cbmessage.setBackgroundDrawable(d);
			}
			
			return _v;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
