//todo: when initializing from the txt file mySongs.txt, if the line of text was a duplicate, we should remove it


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
		//checking to see if file exists
		songList = FXCollections.observableArrayList();
		BufferedReader songReader = new BufferedReader(new FileReader("/SongList/mySongs.txt"));	
		String toAdd=songReader.readLine();
		while (toAdd!=null){
			//text is formatted "name;artist;album;year;" in our mySongs.txt
			//removing white space at the beginning and end if any 
			toAdd=toAdd.trim();	

			//need to create song object and add it to the list
			//every entry has at least name and artist: "name;artist;"

			
		}
		//closing the stream
		songReader.close();

		//feeding the data to our listview
		listView.setItems(songList);
		
	}

	//helper method to add songs to our observable list
	private boolean addSong(Song toAdd){
		//need to iterate through the song observable list to see if the name and artist match case insensitive 		
		for (int i=0;i<songList.size();i++){
			if (toAdd.name.toUpper().equals(songList.get(i).name.toUpper()) && toAdd.author.toUpper().equals(songList.get(i).author.toUpper())){
				//then we have a match and we do not add
				return false;
			}			
		}
		//then we did not find a match and we can add the song to the observable list
		//we need to add the song in alphabetical order of names
		songList.add(toAdd);
		return true;
		
	}






}
