package de.marcelpociot.tokencompletetextview;

import java.util.HashMap;
import java.util.List;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.titanium.proxy.TiViewProxy;

import android.app.Activity;

@Kroll.proxy(creatableInModule = TokentextviewModule.class)

public class AutocompleteViewProxy extends TiViewProxy {

	public AutocompleteViewProxy() {
		super();
	}
	
	@Override
	public TiUIView createView(Activity activity) 
	{
		return new de.marcelpociot.tokencompletetextview.AutocompleteView(this);
	}
	
	@Override
	public void handleCreationArgs(KrollModule createdInModule, Object[] args) {
		super.handleCreationArgs(createdInModule, args);
	}
	

	@Kroll.method
	public Object[] getSelectedObjects()
	{
		AutocompleteView acView = (AutocompleteView)view;
		return acView.getSelectedObjects();
	}
}
