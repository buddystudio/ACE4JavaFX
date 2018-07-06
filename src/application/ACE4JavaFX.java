package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.BorderPane;

public class ACE4JavaFX extends Application 
{
	protected BDEditorView editorView;
	protected BDEditorCtrl editorCtrl;
	
	private String curPath = this.getClass().getResource("/").getPath();
	private String editorUrl = "file://" + curPath + "resources/ace-builds-master/editor.html";
	private String acePath = "/resources/ace-builds-master/src-noconflict";
	
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage)
	{
		BorderPane root  = new BorderPane();

		editorView = new BDEditorView(stage, editorUrl);
		editorCtrl = new BDEditorCtrl(editorView);

		MenuBar menuBar = new MenuBar();
	    
	    this.addMenu(menuBar);
	    
	    root.setTop(menuBar);
	    root.setCenter(editorView);
	    
	    Scene scene = new Scene(root);
		
		stage.setTitle("ACE4JavaFX");
		stage.setScene(scene);
		stage.show();
	}
	
	public void addMenu(MenuBar menuBar)
	{
		// File menu - new, save, exit
	    Menu fileMenu = new Menu("File");
	    
	    MenuItem newMenuItem  = new MenuItem("New");
	    MenuItem openMenuItem = new MenuItem("Open");
	    MenuItem saveMenuItem = new MenuItem("Save");
	    MenuItem exitMenuItem = new MenuItem("Exit");
	    
	    fileMenu.getItems().addAll(newMenuItem, openMenuItem ,saveMenuItem, new SeparatorMenuItem(), exitMenuItem);
	    
	    Menu editMenu = new Menu("Edit");
	    
	    MenuItem clearItem 	= new MenuItem("Clear");
	    MenuItem undoItem 	= new MenuItem("Undo");
	    MenuItem redoItem 	= new MenuItem("Redo");
	    MenuItem copyItem 	= new MenuItem("Copy");
	    MenuItem cutItem  	= new MenuItem("Cut");
	    MenuItem pasteItem 	= new MenuItem("Paste");
	    MenuItem searchItem = new MenuItem("Search");
	    MenuItem testItem	= new MenuItem("Test");
	    
	    editMenu.getItems().addAll(clearItem, undoItem, redoItem, copyItem, cutItem, pasteItem, searchItem, testItem);
	    
	    clearItem.setOnAction(editHandler);
	    undoItem.setOnAction(editHandler);
	    redoItem.setOnAction(editHandler);
	    copyItem.setOnAction(editHandler);
	    cutItem.setOnAction(editHandler);
	    pasteItem.setOnAction(editHandler);
	    searchItem.setOnAction(editHandler);
	    testItem.setOnAction(editHandler);
	    
	    Menu optionsMenu 	= new Menu("Options");
	    Menu langMenu 		= new Menu("Langue");
	    Menu themeMenu 		= new Menu("Theme");
	    Menu fontSizeMenu 	= new Menu("Font Size");
	    
	    ToggleGroup tGroup01 = new ToggleGroup();
	    ToggleGroup tGroup02 = new ToggleGroup();
	    ToggleGroup tGroup03 = new ToggleGroup();
	    
	    for(int i = 10; i < 31; i++)
	    {
	    	RadioMenuItem item = new RadioMenuItem(i + "px");
	    	
	    	item.setToggleGroup(tGroup01);
	    	
	    	fontSizeMenu.getItems().add(item);
	    	
	    	if(i == 14)
	    	{
	    		item.setSelected(true);
	    	}
	    	
	    	item.setOnAction(fontSizeHandler);
	    }

	    optionsMenu.getItems().addAll(langMenu, themeMenu, fontSizeMenu);
	    
	    menuBar.getMenus().addAll(fileMenu, editMenu, optionsMenu);
	    
	    newMenuItem.setOnAction(menuHandler);
	    openMenuItem.setOnAction(menuHandler);
	    saveMenuItem.setOnAction(menuHandler);
	    exitMenuItem.setOnAction(menuHandler);
	    
	    //File dirFile = new File("E:/Projects/ACE4JavaFX/bin/resources/ace-builds-master/src-noconflict");
	    File dirFile = new File(this.getClass().getResource("/").getPath() + this.acePath);
	    
	    if (!dirFile.exists()) 
	    {  
            System.out.println("do not exit");
            
            return ;  
        }
	    
	    String[] fileList = dirFile.list();
	    
	    for (int i = 0; i < fileList.length; i++) 
	    {
	    	File file = new File(dirFile.getPath(), fileList[i]);  
            
	    	String name = file.getName();

            if(name.substring(0, 6).equals("theme-"))
            {
            	System.out.println(name); 
            	System.out.println(name.substring(6, name.length() - 3));
            	
            	RadioMenuItem item = new RadioMenuItem(name.substring(6, name.length() - 3));
            	
            	item.setToggleGroup(tGroup02);
            	
            	themeMenu.getItems().add(item);
            	
            	item.setOnAction(themeHandler);
            	
            }
            else if(name.substring(0, 5).equals("mode-"))
            {
            	System.out.println(name); 
            	System.out.println(name.substring(5, name.length() - 3));
            	
            	RadioMenuItem item = new RadioMenuItem(name.substring(5, name.length() - 3));
            	
            	item.setToggleGroup(tGroup03);
            	
            	langMenu.getItems().add(item);
            	
            	item.setOnAction(langueHandler);
            }
	    }
	}
	
	private EventHandler<ActionEvent> menuHandler = new EventHandler<ActionEvent>()
	{
		@Override
		public void handle(ActionEvent arg0) 
		{
			String name = ((MenuItem)arg0.getTarget()).getText();
			
			if(name.equals("Exit"))
			{
				Platform.exit();
			}
			else if(name.equals("New"))
			{
				// Clean code.
				editorView.webView.getEngine().executeScript("editor.setValue('');");
			}
			else if(name.equals("Open"))
			{
				File file =  null;
				
				FileChooser fileChooser = new FileChooser();
				
				// Set file Filter.
				initFileChooser(fileChooser);
				
				// Show open file dialog
				file = fileChooser.showOpenDialog(null);
				
				if(file == null)
				{
					return;
				}
				
				try 
				{
					String code = BDCodeReader.readFileByLines(file.getPath());
					
					code = code.replaceAll("\"","\\\\\"");
					
					// Clean code.
					editorView.webView.getEngine().executeScript("editor.setValue('');");
					
					// Set Code.
					editorView.webView.getEngine().executeScript("editor.insert(\"" + code +"\");");
				} 
				catch (UnsupportedEncodingException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (FileNotFoundException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(name.equals("Save"))
			{
				File file =  null;
				
				FileChooser fileChooser = new FileChooser();
				
				// Set file Filter.
				initFileChooser(fileChooser);
				
				try
				{
					// Show open file dialog
					file = fileChooser.showSaveDialog(null);
				}
				catch(Exception ex){}

				if(file == null)
				{
					return;
				}
				
				String code = editorCtrl.getCode();
				
				// Write file
				try 
				{
					BDCodeWriter.fileWriter(file.getPath(), code);
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	private void initFileChooser(FileChooser fileChooser)
	{
		// Set file Filter.
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arduino  (*.ino)", "*.ino"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("C++  (*.cpp)", "*.cpp"));;
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("C  (*.c)", "*.c"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Header Files  (*.h)", "*.h"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java  (*.java)", "*.java"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Script  (*.js)", "*.js"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Python Code  (*.py)", "*.py"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text  (*.txt)", "*.txt"));
	}
	
	private EventHandler<ActionEvent> editHandler = new EventHandler<ActionEvent>()
	{
		@Override
		public void handle(ActionEvent arg0) 
		{
			String name = ((MenuItem)arg0.getTarget()).getText();
			
			if(name.endsWith("Undo"))
			{
				// Undo.
				editorCtrl.undo();
			}
			else if(name.equals("Redo"))
			{
				// Redo.
				editorCtrl.redo();
			}
			else if(name.equals("Copy"))
			{
				// Copy
				editorCtrl.copy();
			}
			else if(name.equals("Cut"))
			{
				// Cut
				editorCtrl.cut();
			}
			else if(name.equals("Paste"))
			{
				// Paste
				editorCtrl.paste();
			}
			else if(name.equals("Search"))
			{
				// Search
				editorCtrl.search();
			}
			else if(name.equals("Clear"))
			{
				// Search
				editorCtrl.setCode("");
			}
			else if(name.equals("Test"))
			{
				String code = "";
                
                // 获取词位
                int tabCount = editorCtrl.getCurColumn() - 1;
                
                code += "delay(1000);";
                
                code += "\\n";
                
                // 添加缩进
                for(int i = 0; i < tabCount; i++)
                {
                    //code += "\\t";
                	code += " ";
                }
                
                code += "delay(1000);";
                
				editorCtrl.insert(code);
			
				//System.out.println(editorCtrl.getCurRow());
				//System.out.println(editorCtrl.getCurColumn());
			}
		}	
	};
	
	private EventHandler<ActionEvent> langueHandler = new EventHandler<ActionEvent>()
	{
		@Override
		public void handle(ActionEvent arg0) 
		{
			String mode = ((MenuItem)arg0.getTarget()).getText();
			
			editorCtrl.setMode(mode);
		}	
	};
	
	private EventHandler<ActionEvent> themeHandler = new EventHandler<ActionEvent>()
	{
		@Override
		public void handle(ActionEvent arg0) 
		{
			String theme = ((MenuItem)arg0.getTarget()).getText();
			
			editorCtrl.setTheme(theme);
		}
	};
	
	private EventHandler<ActionEvent> fontSizeHandler = new EventHandler<ActionEvent>()
	{
		@Override
		public void handle(ActionEvent arg0) 
		{
			String size = ((MenuItem)arg0.getTarget()).getText().substring(0, 2);
			
			// Set font size. 
			editorCtrl.setFontSize(Integer.parseInt(size));
		}
	};
}
