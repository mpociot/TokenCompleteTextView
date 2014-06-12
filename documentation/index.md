# TokenCompleteTextView [![Titanium](http://www-static.appcelerator.com/badges/titanium-git-badge-sq.png)](http://www.appcelerator.com/titanium/)

## About

This module is a Android Gmail style token auto-complete text field. It allows you to use custom Views for the generated tokens and list results.

It wraps the native [TokenAutoComplete](https://github.com/splitwise/TokenAutoComplete/) module.

![Focused TokenAutoCompleteTextView example](https://raw.github.com/splitwise/TokenAutoComplete/gh-pages/images/focused.png)

### Usage

	var tokencompletetextview = require('de.marcelpociot.tokencompletetextview');
	var textView = tokencompletetextview.createAutocompleteView({
		onCreateObjectView: function( e )
		{
			var view = Ti.UI.createView({
				touchEnabled: false,
				width: "100%",
			});
			view.add( Ti.UI.createLabel({
				touchEnabled: false,
				color: "white",
				text: e.name
			}) );			
			return view;
		},
		onCreateTokenView: function( e )
		{
			var view = Ti.UI.createView({
				backgroundColor: "black",
				borderRadius: 5,
			});
			var label = Ti.UI.createLabel({
				color: "white",
				text: 	e.name,
				height: 20
			});
			view.add( label );
			return view;
		},
		searchableAttribute: "name",
		data: [
			{
				name: "Marcel",
				email: "m.pociot@gmail.com"
			},
			{
				name: "Maichael",
				email: "m.pociot@gmail.com"
			}
		],
		width: 400,
		color: "black",
		prefix: "To: "
	});

## Parameters

#### onCreateObjectView

Type: `Function`  

This function gets called to create the drop down view, upon showing the autocomplete list.

You must return a View.

As a parameter it receives the data object it tries to show.

### onCreateTokenView

Type: `Function`

This function gets called to create the token view, upon selecting an entry in the autocomplete list or manually pressing ",".

You must return a View.

### searchableAttribute

Type: `String`

Defines which attribute of the custom data objects is searchable. Only one attribute can be searchable.

### data

Type: `Array`

The elements that are searchable through the autocomplete view. You can define custom objects.

### prefix

Type: `String`

You can specify a prefix string, that gets appended to the TextField.

## Functions

### getSelectedObjects

Return Type: `Array`

This function returns an array containing all objects the user added as tokens.

If the user manually added text only the `searchableAttribute` is set with the text the user entered. To determine if it's a custom object or not, these objects contain a special attribute `_custom`.


## Author

I'm a web enthusiast located in Germany and in charge of http://www.titaniumcontrols.com

Follow me on twitter: @marcelpociot / @TitaniumCTRLs


## License

Apache 2
