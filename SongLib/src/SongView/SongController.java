/* By Vishal Patel and Eshan Wadhwa */
package SongView;

import SongApp.Song;
import SongApp.SongComparator;




import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.util.Callback;
import javafx.scene.control.ListCell;



import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import SongApp.Song;
import SongApp.SongComparator;

import java.io.BufferedWriter;

public class SongController{


	@FXML ListView<Song> listView;
	@FXML Button deleteSongButton;
	@FXML Button editDetailsButton;
	@FXML Button addSongButton;

	//these textfields are initially uneditable, and will only become editable once the user selects "edit details" or "add new song"
	@FXML TextField songName;
	@FXML TextField artistName;
	@FXML TextField albumName;
	@FXML TextField songYear;
	
	

	//these FXML elements are hidden and part of adding songs or editing songs in our UI
	//once the user needs to use these buttons, they will be made visible
	//once the user finishes the process of adding or deleting, the buttons should become invisible again
	@FXML Button saveEditButton;
	@FXML Button cancelEditButton;
	@FXML Button confirmAddButton;
	@FXML Button cancelAddButton;
	
	
	//these will help bring up errors when the user selects something else while editing a song or adding a song respectively
	private boolean editHelper=false;
	private boolean addHelper=false;
	
	//helps with properly saving edit
	private int editIndex=0;

	//private list of songs for the app that syncs with the listview
	private ObservableList<Song> songList;

	//start has the purpose of initializing the listview with songList (from txtfile) and selecting the first song automatically, if the song list is not empty
	public void start(Stage mainStage) throws Exception{
		//setting textboxes to be uneditable at the start
		songName.setEditable(false);
		artistName.setEditable(false);
		albumName.setEditable(false);
		songYear.setEditable(false);
		 /* create an ObservableList from an ArrayList created by readFromFile*/
		songList = FXCollections.observableArrayList(readFromFile("src/songs.txt")); //change this when packages made
	      listView.setItems(songList); 
	      
	      listView.setCellFactory(new Callback<ListView<Song>, ListCell<Song>>(){
	    	  
	            @Override
	            public ListCell<Song> call(ListView<Song> p) {
	                 
	                ListCell<Song> cell = new ListCell<Song>(){
	 
	                    @Override
	                    protected void updateItem(Song s, boolean bln) {
	                        super.updateItem(s, bln);
	                        if (s != null) {
	                            setText(s.getTitle()+" : : "+s.getArtist());
	                        }
	                        else if (s == null)
	                        {
	                        	setText(null);
	                        }
	                    }
	 
	                };
	                 
	                return cell;
	            }
	        });
	      
	      // select the first item
	      if (!songList.isEmpty())
	    	  listView.getSelectionModel().select(0);
	      // show the selected song
	      showSong();

	      // set listener for the items
	      listView
	      .getSelectionModel()
	      .selectedItemProperty()
	      .addListener(
	    		  (obs, oldVal, newVal) -> 
	    		  showSong());
	      
	      mainStage.setOnCloseRequest(event -> {
	    	  
	    	 //save songs to songs.txt file
	    	  PrintWriter writer;
	    	  	try {
	    	  			File file = new File ("src/songs.txt"); //change this when packages made
	    	  			file.createNewFile();
	    	  			writer = new PrintWriter(file);
						for(Song s: songList)
				    	  {
				    		  writer.println(s.getTitle());
				    		  writer.println(s.getArtist());
				    		  writer.println(s.getAlbum());
				    		  writer.println(s.getYear());
				    		  
				    	  }
				    	 writer.close(); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    	 
	      });	
	      
	    
	}

	
	@FXML
	public void deleteSong(ActionEvent action){
		//idea is to delete the song that is selected in the list view by the user
		//if the list is empty, then error is given to the user in form of popup
		//warns the user before deleting the song
		
		if (addHelper) {
			showError("Please confirm or cancel your add!");
			return;
		}
		
		if (editHelper) {
			showError("Please confirm or cancel your edit!");
			return;
		}
		
		if (songList.isEmpty()) {
			   showError("There is nothing to delete.");
			   return;
		   }
		   
		   Song item = listView.getSelectionModel().getSelectedItem();
		   int index = listView.getSelectionModel().getSelectedIndex();
		   
		   Alert alert = 
				   new Alert(AlertType.INFORMATION);
		   alert.setTitle("Delete Item");
		   alert.setHeaderText(
				   "Delete Song");

		   String content = "Are you sure you want to delete " + item.getTitle() + "?";

		   alert.setContentText(content);

		   Optional<ButtonType> result = alert.showAndWait();
		   if (result.isPresent()) {
			   songList.remove(item);
			   //songs.remove(index);
			   
			   if (songList.isEmpty()) {
				   songName.setText("");
				   artistName.setText("");
				   albumName.setText("");
				   songYear.setText("");
			   }
			  else if(index == songList.size()-1)
			   {
				   listView.getSelectionModel().select(index--);
			   }
			   else
			   {
				   listView.getSelectionModel().select(index++);
			   }
			   showSong();
		   }
		
		
		
		
	}

	@FXML
	public void editSong(ActionEvent action){ 
		//idea is the make the textboxes editable and make hidden buttons below the ui window visible
		//these buttons will be "save edit" and "cancel edit"
		//if the user tries to do anything besides editing these textboxes or clicking "save edit" or "cancel edit", then the user will get a warning
		
		if (addHelper) {
			showError("Please confirm or cancel your add!");
			return;
		}
		
		if (editHelper) {
			showError("Please confirm or cancel your edit!");
			return;
		}
		
		if (songList.isEmpty()) {
			   showError("There is nothing to edit.");
			   return;
		   }
		
		   saveEditButton.setVisible(true);
		   cancelEditButton.setVisible(true);
		   
		   songName.setEditable(true);
		   artistName.setEditable(true);
		   albumName.setEditable(true);
		   songYear.setEditable(true);
		   
		   editIndex=listView.getSelectionModel().getSelectedIndex();
		   
		   editHelper=true;
		   
		
		   
		   
	}			   
	
	@FXML
	public void cancelEdit(ActionEvent action) {
		//idea is we make the textboxes ineditable, hide the buttons again, and then display info for whatever the list is on
		cancelEditButton.setVisible(false);
		saveEditButton.setVisible(false);
		
		songName.setEditable(false);
		artistName.setEditable(false);
		albumName.setEditable(false);
		songYear.setEditable(false);
		
		editHelper=false;
		
		//reselects the selected item to update the textfields
		showSong();
		
		
	}
	
	@FXML
	public void saveEdit(ActionEvent action) {
		int index = editIndex;
		   
		   String error = checkFields(songName.getText(), artistName.getText(),
				   albumName.getText(), songYear.getText());
		   
		   if (error != null) {
			   if (error.equals("Title and Artist cannot already exist in library.") 
					   && songName.getText().equals(listView.getSelectionModel().getSelectedItem().getTitle()) 
					   && artistName.getText().equals(listView.getSelectionModel().getSelectedItem().getArtist()))
					   {
				   			
				   Song temp = new Song(songName.getText(),artistName.getText(),albumName.getText(),songYear.getText());
				   songList.set(index,temp);
				   listView.getSelectionModel().select(index);
				   showSong();
				   return;
					   }
			  showError(error);
			   return;
		   }
		   
		   editHelper=false;
		   
		   Song temp2 = new Song(songName.getText(),artistName.getText(),albumName.getText(),songYear.getText());
		   songList.set(index,temp2);
		   listView.getSelectionModel().select(index);
			 showSong();
			 
			 cancelEditButton.setVisible(false);
			 saveEditButton.setVisible(false);
				
			 songName.setEditable(false);
			 artistName.setEditable(false);
			 albumName.setEditable(false);
			 songYear.setEditable(false);
			 
			 
	}
	@FXML
	public void addSong(ActionEvent action){
		//idea is to make the listview point to nothing
		//then make the textboxes editable
		//then make the buttons in the details section of the UI visible
		//these buttons will be "confirm addition" and "cancel addition"
		//if the user tries to do anything different than editing the song data or hitting those buttons, they will get a warning from a popup
		//we will use the helper method addSongHelper above to help us with the addition
		
		if (addHelper) {
			showError("Please confirm or cancel your add!");
			return;
		}
		
		if (editHelper) {
			showError("Please confirm or cancel your edit!");
			return;
		}
		
		//clearing textboxes so the user can add songs more easily
		songName.setText("");
		artistName.setText("");
		albumName.setText("");
		songYear.setText("");
		
		//making textboxes editable
		songName.setEditable(true);
		artistName.setEditable(true);
		albumName.setEditable(true);
		songYear.setEditable(true);
		
		confirmAddButton.setVisible(true);
		cancelAddButton.setVisible(true);
		
		addHelper=true;
		
		 
				   	
		
	}
	
	@FXML
	void confirmAdd(ActionEvent event) {
		int index = listView.getSelectionModel().getSelectedIndex();
		   
		   Song tempSong;
	  
		   
		   String error = checkFields(songName.getText(), artistName.getText(),
				   albumName.getText(), songYear.getText());
		   
		   if (error != null) {
			   showError(error);
			   return;
			   
		   }
			
		   if (songYear.getText().trim().isEmpty())
			    tempSong = new Song(songName.getText().trim(),
					   artistName.getText().trim(),
					   albumName.getText().trim(),
					   "");
			   
		   tempSong = new Song(songName.getText().trim(),
				   artistName.getText().trim(),
				   albumName.getText().trim(),
				   songYear.getText().trim());
	   
	   //return null;


		songList.add(tempSong);
		//songs.add(tempSong);
		
		FXCollections.sort(songList, new SongComparator());
		// Collections.sort(songs, new SongComparator());
		
		//if this is first song added, then select it
		if (songList.size() == 1) {
			   listView.getSelectionModel().select(0);
		}
		else
		{
			   index = 0;
			   for(Song s: songList)
			   {
				   
				   if(s == tempSong)
				   {
					  listView.getSelectionModel().select(index);
					  break;
				   }
				   
				   index++;
			   }
		}
		
		songName.setEditable(false);
		artistName.setEditable(false);
		albumName.setEditable(false);
		songYear.setEditable(false);
		
		cancelAddButton.setVisible(false);
		confirmAddButton.setVisible(false);
		
		addHelper=false;
	}
	
	@FXML
	void cancelAdd(ActionEvent event) {
		//we should set the textboxes to what they were before
		//make them uneditable
		//then make the cancel and confirm buttons disappear
		songName.setEditable(false);
		artistName.setEditable(false);
		albumName.setEditable(false);
		songYear.setEditable(false);
		
		showSong();
		
		cancelAddButton.setVisible(false);
		confirmAddButton.setVisible(false);
		
		addHelper=false;
		
	}
	
	
	
	
	private void showError(String error) {
		   Alert alert = 
				   new Alert(AlertType.INFORMATION);
		   alert.setTitle("Error");
		   alert.setHeaderText("Error");
		   String content = error;
		   alert.setContentText(content);
		   alert.showAndWait();
	   }

	private String checkFields(String title, String artist, String album, String year) {
		   if (title.trim().isEmpty())
			   return "Title cannot be empty.";
		   else if (artist.trim().isEmpty())
			   return "Artist cannot be empty.";
		   else if (!isUniqueSong(title, artist))
			   return "Title and Artist cannot already exist in library.";
		   
		   else if (!year.trim().isEmpty()) {
			   if (!year.trim().matches("[0-9]+"))
				   return "Year must only contain numbers.";
			   else if (year.trim().length() != 4)
				   return "Year must be 4 digits long.";
		   }
		   
		   return null;
	   }

	private boolean isUniqueSong(String title, String artist) {
		   for (Song s : songList) {
			   				   
			   if (s.getTitle().toLowerCase().equals(title.trim().toLowerCase()) &&
					   s.getArtist().toLowerCase().equals(artist.trim().toLowerCase()))
			   {
				   return false;
			   }
				   
		   }
		   return true;
	   }
	
	private void showSong() {
			if (editHelper) {
				return;
			}
			
			if (addHelper) {
				return;
			}
		   if (listView.getSelectionModel().getSelectedIndex() < 0)
			   return;
		   
		
		   
		   Song s = listView.getSelectionModel().getSelectedItem();
		  
		   
		   songName.setText(s.getTitle());
		   artistName.setText(s.getArtist());
		   albumName.setText(s.getAlbum());
		   songYear.setText(s.getYear());
	   }




	private ArrayList<Song> readFromFile(String filePathName)
	{
		   ArrayList <Song> songs = new ArrayList<Song>();
		   BufferedReader br;
		   Path filePath = Paths.get(filePathName);
		   try {
	
				if (!new File(filePathName).exists())
				{
				   return songs;
				}
			   br = Files.newBufferedReader(filePath);
			   String line = br.readLine();
				
			   while (line != null) { 
		              
				   String title = line;
		               
				   line = br.readLine();
				   String artist = line;
		               
				   line = br.readLine();
				   String album = line;
		               
				   line = br.readLine();
				   String year = line;
		               
				   Song temp = new Song(title, artist, album, year);
				   songs.add(temp);
		               
				   line = br.readLine(); //to the next song name, if not null
			   }
			   
			   br.close();
			  
				
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		      
		   //sort songs alphabetically by title
		   Collections.sort(songs, new SongComparator());
		   return songs;
	}
}