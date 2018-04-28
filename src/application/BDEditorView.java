package application;

import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Window;
import netscape.javascript.JSObject;


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
		
		//JSObject win  = (JSObject) this.webView.getEngine().executeScript("window"); 
		
		//win.setMember("tt", this);
		
	}
	/*
	public void go() 
    {
		System.out.println("Good Good Good");
    }
    */
	
	public WebView getWebView() 
	{
		return webView;
	}
}
