// open a single window
var win = Ti.UI.createWindow({
	backgroundColor:'white'
});
var label = Ti.UI.createLabel();
win.add(label);
win.open();

var tokencompletetextview = require('de.marcelpociot.tokencompletetextview');
var textView = tokencompletetextview.createAutocompleteView({
	//data: ["a","b","c","d"],
	objectView: Ti.UI.createView({ backgroundColor: "red", height:70})
});

win.add( textView );
