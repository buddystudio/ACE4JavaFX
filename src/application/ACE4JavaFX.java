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
import javafx.scene.layout.BorderPane;

public class ACE4JavaFX extends Application 
{
	protected BDEditorView editorView;
	
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	
	private String curPath = this.getClass().getResource("/").getPath();
	private String editorUrl = "file://" + curPath + "resources/ace-builds-master/editor.html";
	
	@Override
	public void start(Stage stage) 
	{
		BorderPane root  = new BorderPane();

		editorView = new BDEditorView(stage, editorUrl);
		
		new BDEditorCtrl(editorView);

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
	    
	    MenuItem newMenuItem = new MenuItem("New");
	    MenuItem openMenuItem = new MenuItem("Open");
	    MenuItem saveMenuItem = new MenuItem("Save");
	    MenuItem exitMenuItem = new MenuItem("Exit");
	    
	    fileMenu.getItems().addAll(newMenuItem, openMenuItem ,saveMenuItem, new SeparatorMenuItem(), exitMenuItem);
	    
	    Menu OptionsMenu = new Menu("Options");
	    
	    Menu langMenu = new Menu("Langue");
	    Menu themeMenu = new Menu("Theme");
	    Menu fontSizeMenu = new Menu("Font Size");
	    
	    ToggleGroup tGroup01 = new ToggleGroup();
	    ToggleGroup tGroup02 = new ToggleGroup();
	    ToggleGroup tGroup03 = new ToggleGroup();
	    
	    for(int i = 10; i < 21; i++)
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

	    //fontSizeMenu.getItems().addAll(item01, item02, item03);
	    OptionsMenu.getItems().addAll(langMenu, themeMenu, fontSizeMenu);
	    
	    menuBar.getMenus().addAll(fileMenu, OptionsMenu);
	    
	    newMenuItem.setOnAction(menuHandler);
	    openMenuItem.setOnAction(menuHandler);
	    exitMenuItem.setOnAction(menuHandler);
	    
	    //File dirFile = new File("E:/Projects/ACE4JavaFX/bin/resources/ace-builds-master/src-noconflict");
	    File dirFile = new File(this.getClass().getResource("/").getPath() + "/resources/ace-builds-master/src-noconflict");
	    
	    //System.out.println(this.getClass().getResource("/").getPath());
	    
	    if (!dirFile.exists()) 
	    {  
            System.out.println("do not exit");
            
            return ;  
        }
	    
	    String[] fileList = dirFile.list();
	    //System.out.println(fileList.length); 
	    
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
		//BDEditorView view = 
		
		
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
				File file;
				
				FileChooser fileChooser = new FileChooser();
				
				// Set file Filter.
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arduino  (*.ino)", "*.ino"));
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("C++  (*.cpp)", "*.cpp"));;
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("C  (*.c)", "*.c"));
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Header Files  (*.h)", "*.h"));
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java  (*.java)", "*.java"));
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Script  (*.js)", "*.js"));
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Python Code  (*.py)", "*.py"));
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text  (*.txt)", "*.txt"));
				
				// Show open file dialog
				file = fileChooser.showOpenDialog(null);
				
				if(file == null)
				{
					return;
				}
				
				try 
				{
					String code = BDCodeReader.readFileByLines(file.getPath());
					
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
				
			}
		}
		
	};
	
	private EventHandler<ActionEvent> langueHandler = new EventHandler<ActionEvent>()
	{
		@Override
		public void handle(ActionEvent arg0) 
		{
			String name = ((MenuItem)arg0.getTarget()).getText();
			
			// Set code mode. 
			editorView.webView.getEngine().executeScript("editor.session.setMode(\"ace/mode/"+ name +"\");");	
		}	
	};
	
	private EventHandler<ActionEvent> themeHandler = new EventHandler<ActionEvent>()
	{
		@Override
		public void handle(ActionEvent arg0) 
		{
			String name = ((MenuItem)arg0.getTarget()).getText();
			
			// Set theme.
			editorView.webView.getEngine().executeScript("editor.setTheme(\"ace/theme/"+ name +"\");");
		}	
	};
	
	private EventHandler<ActionEvent> fontSizeHandler = new EventHandler<ActionEvent>()
	{
		@Override
		public void handle(ActionEvent arg0) 
		{
			String name = ((MenuItem)arg0.getTarget()).getText();
			
			// Set font size. 
			editorView.webView.getEngine().executeScript("editor.setFontSize(" + name.substring(0, 2) + ");");
		}
		
	};
}
