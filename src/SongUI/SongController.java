
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.FileReader;
import java.io.BufferedReader;



public class SongController{


	@FXML ListView<String> listView;
	@FXML Button deleteSongButton;
	@FXML Button editDetailsButton;
	@FXML Button addSongButton;
	@FXML TextField songName;
	@FXML TextField artistName;
	@FXML TextField albumName;
	@FXML TextField songYear;

	//private list of songs for the app
	private ObservableList<Song> songList;


	public void start(){
		//populating the observable list from an array list obtained from reading the file
		songList = FXCollections.observableArrayList();
		BufferedReader songReader = new BufferedReader(new FileReader("/src/SongList/mySongs.txt"));	
		String result=songReader.readLine();
		while (result!=null){
			//text is formatted "name artist album year"
			//removing white space at beginning and end if any 
			result=result.trim();
			//creating song object to store the data
		}
	}

	//helper method to add songs
	private boolean addSong(Song toAdd){
		//need to iterate through the song observable list to see if the name and artist match case insensitive 		
		for (int i=0;i<songList.size();i++){
			if (toAdd.name.toUpperCase().equals(songList.get(i).name.toUpperCase()) && toAdd.artist.toUpperCase().equals(songList.get(i).author.toUpperCase())){
				//then we have a duplicate
				return false;
			}	
		}
		//then we did not find a match and we can add the song to the observable list
		songList.add(toAdd);
		return true;
		
	}






}
