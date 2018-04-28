package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;
import netscape.javascript.JSObject;

public class BDEditorCtrl 
{
	private WebView webView;
	
	public BDEditorCtrl(BDEditorView editor) 
	{
		this.webView = editor.webView;
		
		JSObject win  = (JSObject) webView.getEngine().executeScript("window");
		
		win.setMember("test", this);

		this.webView.getEngine().getLoadWorker().stateProperty().addListener(  
	        	new ChangeListener<State>() 
	            {  
	            	@Override public void changed(ObservableValue ov, State oldState, State newState) 
	                {  
	            		if (newState == Worker.State.SUCCEEDED) 
	                    {
	            			// Get code.
	            			String code = (String) webView.getEngine().executeScript("editor.getValue()");
	                        System.out.println(code);
	                        
	                        String code2 = "Testing.";
	                        
	                        // Set code font size.
	                        webView.getEngine().executeScript("editor.setFontSize(14);");
	                        
	                        // Insert Code.
	                        //webView.getEngine().executeScript("editor.insert(\"" + code2 +"\");");
	                        
	                        //int cursor = (int) webView.getEngine().executeScript("editor.selection.getCursor()");
	                        
	                        //System.out.println(cursor);
	                        
	                        //webView.getEngine().executeScript("editor.gotoLine(3);");
	                        
	                        int length = (int)webView.getEngine().executeScript("editor.session.getLength();");
	                        
	                        System.out.println("Code length is: " + length);
	                        
	                        // Find
	                        //webView.getEngine().executeScript("editor.find('int',{backwards: false,wrap: false,caseSensitive: false,wholeWord: false,regExp: false});");
	                        
	                        // 查找下一个和上一个
	                        //webView.getEngine().executeScript("editor.findNext();");
	                        //webView.getEngine().executeScript("editor.findPrevious();");
	                        
	                        // 替换单个字
	                        //webView.getEngine().executeScript("editor.find('int');");
	                        //webView.getEngine().executeScript("editor.replace('float');");
	                        
	                        // 全部替换
	                        //webView.getEngine().executeScript("editor.find('int');");
	                        //webView.getEngine().executeScript("editor.replaceAll('float');");
	                        
	                        //addJSHandlers(editor.root);

	                        webView.getEngine().executeScript("editor.getSession().on('change', function(e) {test.onChange();});");
	                        webView.getEngine().executeScript("editor.getSession().selection.on('changeCursor', function(e) {test.onChangeCursor();});");
	                        webView.getEngine().executeScript("editor.getSession().selection.on('changeSelection', function(e) {test.onChangeSelection();});");

	                    }  
	                    else if (newState == Worker.State.FAILED)
	                    {
	                    }
	                    else
	                    {
	                    }
	                }  
	            });
	}
	
	public void onChange()
    {
		System.out.println("on change...");
    }
	
	public void onChangeCursor()
    {
		System.out.println("on change cursor...");
    }
	
	public void onChangeSelection()
    {
		System.out.println("on change selection...");
    }
	
	private void addJSHandlers(Window ownerWindow) 
	{
		webView.getEngine().setPromptHandler(JSHandlers.getPromptHandler());
		webView.getEngine().setCreatePopupHandler(JSHandlers.getPopupHandler());
		webView.getEngine().setOnAlert(JSHandlers::alertHandler);
		webView.getEngine().setConfirmHandler(JSHandlers.getConfirmHandler());

		if (ownerWindow instanceof Stage) 
		{
			Stage stage = (Stage) ownerWindow;
			
			// Sync the title of the stage with the title of the loaded web page
			webView.getEngine().titleProperty().addListener(
			(prop, oldTitle, newTitle) -> stage.setTitle(newTitle));
		}
		
		((JSObject) webView.getEngine().executeScript("window")).setMember("java", new Object() 
		{
	        public void paste() 
	        {
	            String content = (String) Clipboard.getSystemClipboard().getContent(DataFormat.PLAIN_TEXT);
	            
	            if (content != null) 
	            {
	            	webView.getEngine().executeScript("editor.onPaste(\"" + content.replace("\n", "\\n") + "\");");
	            }
	        }
	    });
	    
	}

}
