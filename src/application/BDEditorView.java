package application;

import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Window;

public class BDEditorView extends BorderPane 
{	
	protected Window root		= null;
	protected String url		= null;
	protected WebView webView 	= new WebView();
	
	public BDEditorView(Window ownerWindow, String homePageUrl) 
	{
		this.root = ownerWindow;
		this.url = homePageUrl;
		this.webView.getEngine().load(homePageUrl);
		this.setCenter(webView);
	}
	
	public WebView getWebView() 
	{
		return webView;
	}
}
