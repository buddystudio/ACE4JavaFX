package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
	                        
	                        //System.out.println(getCode());
	                        //System.out.println("Code length is: " + getLength());
	                        
	                        // ***** End test *****
	                        
	                        //webView.getEngine().executeScript("editor.session.setMode(\"ace/mode/c_cpp\");");
	                        
	                        // Set code font size.
	                        //webView.getEngine().executeScript("editor.setFontSize(14);");
	                        
	                        // Insert Code.
	                        //webView.getEngine().executeScript("editor.insert(\"" + code2 +"\");");
	                        
	                        //webView.getEngine().executeScript("editor.gotoLine(3);");
	                        
	                        // Find
	                        //webView.getEngine().executeScript("editor.find('int',{backwards: false,wrap: false,caseSensitive: false,wholeWord: false,regExp: false});");
	                        
	                        // Find Next and Find Previous.
	                        //webView.getEngine().executeScript("editor.findNext();");
	                        //webView.getEngine().executeScript("editor.findPrevious();");
	                        
	                        // Replace.
	                        //webView.getEngine().executeScript("editor.find('int');");
	                        //webView.getEngine().executeScript("editor.replace('float');");
	                        
	                        // ReplaceAll.
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
		
		// Disable mouse events.
		//this.webView.setMouseTransparent(true);
		
		//this.webView.setPickOnBounds(true);
		
		this.webView.setOnDragOver(new EventHandler<DragEvent>() 
		{
			@Override
			public void handle(DragEvent event) 
			{
				//if (event.getGestureSource() != m_imageView) 
				{
					event.acceptTransferModes(TransferMode.ANY);
				}
			}
		});
		
		this.webView.setOnDragDropped(new EventHandler<DragEvent>() 
		{
			@SuppressWarnings("unused")
			@Override
			public void handle(DragEvent event) 
			{
				Dragboard dragboard = event.getDragboard();
				
				List<File> files = dragboard.getFiles();
				
				if(files.size() > 0)
				{
					File file = files.get(0);
					
					//System.out.println(file.getPath());
					
					if(file == null)
					{
						return;
					}
					
					try 
					{
						String code = BDCodeReader.readFileByLines(file.getPath());
						
						code = code.replaceAll("\"","\\\\\"");
						
						// Clean code.
						webView.getEngine().executeScript("editor.setValue('');");
						
						// Set Code.
						webView.getEngine().executeScript("editor.insert(\"" + code +"\");");
					} 
					catch (UnsupportedEncodingException e) 
					{
						e.printStackTrace();
					} 
					catch (FileNotFoundException e) 
					{
						e.printStackTrace();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
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
	
	public void setTabSize(int size)
	{
		// Set font size.
		webView.getEngine().executeScript("editor.getSession().setTabSize(" + size + ");");
	}
	
	public void setReadOnly(boolean flag)
	{
		// 
		webView.getEngine().executeScript("editor.setReadOnly(" + flag + ");");
	}
	
	public void setCode(String code)
	{
		// 设置代码
		webView.getEngine().executeScript("editor.setValue(\"" + code + "\");");
	}
	
	public void resize()
	{
		webView.getEngine().executeScript("editor.resize();");
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
	
	// Search
	public void search()
	{
		// 涓巆trl+f鍔熻兘涓�鑷�
		webView.getEngine().executeScript("editor.execCommand('find');");
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
	
	public void focus()
	{
		// 鑾峰彇鐒︾偣
		webView.getEngine().executeScript("editor.focus();");
	}
	
	// Go to line.
	public void gotoLine(int num)
	{
		webView.getEngine().executeScript("editor.gotoLine(" + num + ");");
	}
	
	public void moveCursorTo(int rows, int columns)
	{
		// 绉诲姩鍏夋爣鑷崇r琛岋紝绗琧鍒� 
		webView.getEngine().executeScript("editor.moveCursorTo(" + rows + ", " + columns + ");");
	}
	
	// Get source code from the editor.
	public String getCode()
	{
		// Get code.
		String code = (String) webView.getEngine().executeScript("editor.getValue()");
        
		return code;
	}
	
	public JSObject getCursor()
	{	
		JSObject win  = (JSObject) webView.getEngine().executeScript("editor.selection.getCursor();");
        
		return win;
	}
	
	public int getCurRow()
	{
		JSObject win = (JSObject) webView.getEngine().executeScript("editor.selection.getCursor();");
		
		return (int)win.getMember("row") + 1;
	}
	
	public int getCurColumn()
	{
		JSObject win = (JSObject) webView.getEngine().executeScript("editor.selection.getCursor();");
		
		return (int)win.getMember("column") + 1;
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
