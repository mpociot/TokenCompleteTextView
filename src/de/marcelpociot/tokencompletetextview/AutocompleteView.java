package de.marcelpociot.tokencompletetextview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollObject;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiActivity;
import org.appcelerator.titanium.TiActivityWindow;
import org.appcelerator.titanium.TiActivityWindows;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiBaseActivity;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiContext;
import org.appcelerator.titanium.proxy.TiWindowProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.util.TiUIHelper;
import org.appcelerator.titanium.view.TiCompositeLayout;

import com.tokenautocomplete.TokenCompleteTextView;

import ti.modules.titanium.ui.widget.TiUIText;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;

public class AutocompleteView extends TiUIText {
	
	private static final String TAG = "AutocompleteView";

	private TiViewProxy objectView;
	private Object[] data;
	
	private TextField textView;
	private String searchKey;
	
	private KrollFunction onCreateObjectView;
	private KrollFunction onCreateTokenView;
	
	ArrayAdapter<AutocompleteHashmap> adapter;
	
	Person[] people;
    ArrayAdapter<Person> peopleAdapter;
	
	public static final String PROPERTY_DATA					= "data";
	public static final String PROPERTY_OBJECT_VIEW 			= "objectView";
	public static final String PROPERTY_PREFIX					= "prefix";
	public static final String PROPERTY_SEARCH_ATTRIBUTE		= "searchableAttribute";
	public static final String PROPERTY_CREATE_OBJECT_VIEW 		= "onCreateObjectView";
	public static final String PROPERTY_CREATE_TOKEN_VIEW 		= "onCreateTokenView";
	
	public class TextField extends TokenCompleteTextView {

		public TiViewProxy objectView;
		
		public TextField(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }
		
		@Override
	    protected View getViewForObject(Object object) {
			Log.e(TAG, "Return View for Object" + object.toString() );
			LinearLayout view = new LinearLayout( getContext() );
			view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
			
			TiViewProxy tmpView = (TiViewProxy)onCreateTokenView.call( proxy.getKrollObject() , new Object[] { object } );	        
	        
	        view.addView( tmpView.getOrCreateView().getOuterView() );
	        return view;
	    }
		
		@Override
		protected void onLayout(boolean changed, int left, int top, int right, int bottom)
		{
			super.onLayout(changed, left, top, right, bottom);
			TiUIHelper.firePostLayoutEvent( proxy );
		}

	    @Override
	    protected Object defaultObject(String completionText) {
	    	Log.e("AutocompleteView", "[INFO] Received completionText: " + completionText);
	    	AutocompleteHashmap map = new AutocompleteHashmap();
	    	map.searchableParam 	= searchKey;
	    	map.put( searchKey , completionText);
	    	map.put( "_custom" , true );

	    	Log.e("AutocompleteView", "[INFO] Created new temporary map: " + map.toString() );
	        return map;
	    }
		
	}
	
	public class AutocompleteHashmap extends HashMap<String,Object> {
		
		public String searchableParam;
		
		@Override
		public String toString()
		{
			return (String) this.get( this.searchableParam );
		}
	}

	
	public AutocompleteView(TiViewProxy proxy)
	{
		super( proxy, true );
		
		textView 	= new TextField( proxy.getActivity(), null );
		textView.setGravity( Gravity.TOP | Gravity.LEFT );
		
		setNativeView( textView );
	}
	
	@Override
	public void processProperties(KrollDict d)
	{
		super.processProperties(d);
		
		if (d.containsKey(TiC.PROPERTY_COLOR)) {
			textView.setTextColor(TiConvert.toColor(d, TiC.PROPERTY_COLOR));
		}

		if (d.containsKey(TiC.PROPERTY_HINT_TEXT)) {
			textView.setHint(d.getString(TiC.PROPERTY_HINT_TEXT));
		}

		if (d.containsKey( PROPERTY_PREFIX )) {
			textView.setPrefix(d.getString(PROPERTY_PREFIX));
		}
		
		if( d.containsKey( PROPERTY_SEARCH_ATTRIBUTE ) ) {
			searchKey = d.getString(PROPERTY_SEARCH_ATTRIBUTE);
		}
		
		if( d.containsKey( PROPERTY_CREATE_TOKEN_VIEW ) ) {
			onCreateTokenView = (KrollFunction)d.get(PROPERTY_CREATE_TOKEN_VIEW);
			if( onCreateTokenView instanceof KrollFunction )
			{
				
			} else {
				Log.e(TAG, "[ERROR] Invalid type for " + PROPERTY_CREATE_TOKEN_VIEW);				
			}
		}
		
		if( d.containsKey( PROPERTY_CREATE_OBJECT_VIEW) ) {
			onCreateObjectView = (KrollFunction)d.get(PROPERTY_CREATE_OBJECT_VIEW);
			if( onCreateObjectView instanceof KrollFunction )
			{
				
			} else {
				Log.e(TAG, "[ERROR] Invalid type for " + PROPERTY_CREATE_OBJECT_VIEW);				
			}
		}
		
		if (d.containsKey(PROPERTY_OBJECT_VIEW)) {
			Object tmpView = d.get(PROPERTY_OBJECT_VIEW);
			if (tmpView != null && objectView instanceof TiViewProxy) {
				objectView = (TiViewProxy)tmpView;
				textView.objectView = (TiViewProxy)tmpView;
				Log.e(TAG, "[ERROR] Received objectView" + tmpView);
			} else {
				Log.e(TAG, "[ERROR] Invalid type for objectView");
			}
		}
		if( d.containsKey(PROPERTY_DATA)) {
			Object[] data = (Object[])d.get(PROPERTY_DATA);
			Log.e(TAG, "[INFO] Received data" + data);
			
			ArrayList<AutocompleteHashmap> list = new ArrayList<AutocompleteHashmap>();
			
			for( int i=0;i<data.length;i++)
			{
				AutocompleteHashmap map = new AutocompleteHashmap();
				map.searchableParam 	= searchKey;
				map.putAll( (HashMap<String,Object>)data[i] );
				list.add( map );
			}
			this.data = data;
			
			
			adapter = new ArrayAdapter<AutocompleteHashmap>(this.getProxy().getActivity(), android.R.layout.simple_list_item_1, list) {
				@Override
	            public View getView(int position, View convertView, ViewGroup parent) {
					
					AutocompleteHashmap map = getItem(position);
					Log.e(TAG, "Current Item: " + map);
                	TiViewProxy tmpView = (TiViewProxy)onCreateObjectView.call( proxy.getKrollObject() , new Object[] { map } );
                	
                	LinearLayout view = new LinearLayout( getContext() );
        			view.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        			view.setOrientation( LinearLayout.VERTICAL );
        			
        			view.addView( tmpView.getOrCreateView().getNativeView() );
        			
                	convertView = (View)view;
	                return convertView;
	            }
			};
			textView.setAdapter(adapter);
		}
		
	}
	
	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, KrollProxy proxy)
	{
		super.propertyChanged(key, oldValue, newValue, proxy);
		Log.d(TAG, "Property: " + key + " old: " + oldValue + " new: " + newValue, Log.DEBUG_MODE);
	}
	
	@Kroll.method
	public Object[] getSelectedObjects()
	{
		List <Object> list = textView.getObjects();
		Object[] output = list.toArray();
		for( int i=0; i< output.length; i++ )
		{
			Log.e(TAG, "Output is: " + output[i] );
		}
		return list.toArray();
	}
}
