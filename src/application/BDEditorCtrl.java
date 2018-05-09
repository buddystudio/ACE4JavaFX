package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
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

		// Load complete.
		this.webView.getEngine().getLoadWorker().stateProperty().addListener(  
	        	new ChangeListener<State>() 
	            {  
	            	@Override public void changed(ObservableValue ov, State oldState, State newState) 
	                {  
	            		if (newState == Worker.State.SUCCEEDED) 
	                    {
	                        
	                        // ***** Test code *****
	                        
	                        //setFontSize(30);
	            			
	                        //insert("Hello World!");
	                        //find("void");
	                        //repalceAll("void", "abcd");
	                        
	                        System.out.println(getCode());
	                        System.out.println("Code length is: " + getLength());
	                        
	                        // ***** End test *****
	                        
	                        //webView.getEngine().executeScript("editor.session.setMode(\"ace/mode/c_cpp\");");
	                        
	                        // Set code font size.
	                        //webView.getEngine().executeScript("editor.setFontSize(14);");
	                        
	                        // Insert Code.
	                        //webView.getEngine().executeScript("editor.insert(\"" + code2 +"\");");
	                        
	                        //int cursor = (int) webView.getEngine().executeScript("editor.selection.getCursor()");
	                        
	                        //System.out.println(cursor);
	                        
	                        //webView.getEngine().executeScript("editor.gotoLine(3);");
	                        
	                        
	                        
	                        // Find
	                        //webView.getEngine().executeScript("editor.find('int',{backwards: false,wrap: false,caseSensitive: false,wholeWord: false,regExp: false});");
	                        
	                        // ������һ������һ��
	                        //webView.getEngine().executeScript("editor.findNext();");
	                        //webView.getEngine().executeScript("editor.findPrevious();");
	                        
	                        // �滻������
	                        //webView.getEngine().executeScript("editor.find('int');");
	                        //webView.getEngine().executeScript("editor.replace('float');");
	                        
	                        // ȫ���滻
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
	
	// Set code font size.
	public void setFontSize(int size)
	{
		// Set code font size.
        webView.getEngine().executeScript("editor.setFontSize(" + size + ");");
	}
	
	// Set mode.
	public void setMode(String mode)
	{
		// Set code mode. 
		webView.getEngine().executeScript("editor.session.setMode(\"ace/mode/"+ mode +"\");");	
	}
	
	// Set theme.
	public void setTheme(String theme)
	{
		// Set theme.
		webView.getEngine().executeScript("editor.setTheme(\"ace/theme/"+ theme +"\");");
	}
	
	// Undo
	public void undo()
	{
		webView.getEngine().executeScript("editor.undo();");
	}
	
	// Redo
	public void redo()
	{
		webView.getEngine().executeScript("editor.redo();");
	}
	
	// Copy.
	public void copy()
	{
		String text = (String)webView.getEngine().executeScript("editor.session.getTextRange(editor.getSelectionRange());");
		
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent cc = new ClipboardContent();
		
		cc.putString(text);
		clipboard.setContent(cc);
	}
	
	// Cut.
	public void cut()
	{
		String text = (String)webView.getEngine().executeScript("editor.session.getTextRange(editor.getSelectionRange());");
		
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent cc = new ClipboardContent();
		
		cc.putString(text);
		clipboard.setContent(cc);
		
		webView.getEngine().executeScript("editor.insert(\"\");");
	}
	
	// Paste.
	public void paste()
	{
		Clipboard clipboard = Clipboard.getSystemClipboard();
		String text = clipboard.getContent(DataFormat.PLAIN_TEXT).toString();
		
		webView.getEngine().executeScript("editor.insert(\"" + text +"\");");
	}
	
	// Insert the code.
	public void insert(String code)
	{
		webView.getEngine().executeScript("editor.insert(\"" + code +"\");");		
	}
	
	// Find.
	public void find(String keyword)
	{
		webView.getEngine().executeScript("editor.find('" + keyword + "',{backwards: false,wrap: false,caseSensitive: false,wholeWord: false,regExp: false});");
	}
	
	// Find next.
	public void findNext()
	{
		webView.getEngine().executeScript("editor.findNext();");
	}
	
	// Find previous
	public void findPrevious()
	{
		webView.getEngine().executeScript("editor.findPrevious();");
	}
	
	// Replace.
	public void repalce(String keyword_A, String keyword_B)
	{
		webView.getEngine().executeScript("editor.find('" + keyword_A + "');");
        webView.getEngine().executeScript("editor.replace('" + keyword_B + "');");	
	}
	
	// Replace.
	public void repalceAll(String keyword_A, String keyword_B)
	{
		webView.getEngine().executeScript("editor.find('" + keyword_A + "');");
        webView.getEngine().executeScript("editor.replaceAll('" + keyword_B + "');");
	}
	
	// Go to line.
	public void gotoLine(int num)
	{
		webView.getEngine().executeScript("editor.gotoLine(" + num + ");");
	}
	
	// Get source code from the editor.
	public String getCode()
	{
		// Get code.
		String code = (String) webView.getEngine().executeScript("editor.getValue()");
        
		return code;
	}
	
	public int getLength()
	{
		int length = (int)webView.getEngine().executeScript("editor.session.getLength();");
        
		return length;
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
