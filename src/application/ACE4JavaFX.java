package application;
	
import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	private BDEditorView editorView;
	
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	
	private String curPath = this.getClass().getResource("/").getPath();
	private String editorUrl = "file://" + curPath + "resources/ace-builds-master/editor3.html";
	
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
	    MenuItem saveMenuItem = new MenuItem("Save");
	    MenuItem exitMenuItem = new MenuItem("Exit");
	    
	    fileMenu.getItems().addAll(newMenuItem, saveMenuItem, new SeparatorMenuItem(), exitMenuItem);
	    
	    Menu OptionsMenu = new Menu("Options");
	    
	    Menu langMenu = new Menu("Langue");
	    Menu themeMenu = new Menu("Theme");
	    Menu fontSizeMenu = new Menu("Font Size");
	    
	    ToggleGroup tGroup01 = new ToggleGroup();
	    ToggleGroup tGroup02 = new ToggleGroup();
	    
	    RadioMenuItem item01 = new RadioMenuItem("12px");
	    RadioMenuItem item02 = new RadioMenuItem("14px");
	    RadioMenuItem item03 = new RadioMenuItem("16px");
	    
	    item01.setToggleGroup(tGroup01);
	    item02.setToggleGroup(tGroup01);
	    item03.setToggleGroup(tGroup01);
	    item02.setSelected(true);

	    fontSizeMenu.getItems().addAll(item01, item02, item03);
	    OptionsMenu.getItems().addAll(langMenu, themeMenu, fontSizeMenu);
	    
	    menuBar.getMenus().addAll(fileMenu, OptionsMenu);
	    
	    exitMenuItem.setOnAction(menuHandler);
	    
	    item01.setOnAction(menuHandler);
	    item02.setOnAction(menuHandler);
	    item03.setOnAction(menuHandler);
	    
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
            	
            	item.setOnAction(menuHandler);
            	
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
			else if(name.equals("12px"))
			{
				editorView.webView.getEngine().executeScript("editor.setFontSize(12);");
			}
			else if(name.equals("14px"))
			{
				editorView.webView.getEngine().executeScript("editor.setFontSize(14);");
			}
			else if(name.equals("16px"))
			{
				editorView.webView.getEngine().executeScript("editor.setFontSize(16);");
			}
			else
			{
				// Set theme.
				editorView.webView.getEngine().executeScript("editor.setTheme(\"ace/theme/"+ name +"\");");
			}
		}
		
	};
}
