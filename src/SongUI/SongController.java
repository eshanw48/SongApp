import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.util.Callback;
import javafx.scene.control.ListCell;



import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
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

	//private list of songs for the app that syncs with the listview
	private ObservableList<Song> songList;

	//start has the purpose of initializing the listview with songList (from txtfile) and selecting the first song automatically, if the song list is not empty
	public void start(Stage mainStage) throws Exception{
		//populating the observable list from an array list obtained from reading the file
		songList = FXCollections.observableArrayList();
		File songData = new File("mySongs.txt");
		//this will make the txt file if there is no such file at the specified path
		songData.createNewFile();
		BufferedReader songReader = new BufferedReader(new FileReader(songData));	
		String toAdd=songReader.readLine();
		while (toAdd!=null){
			//text is formatted "name;artist;album;year;" in our mySongs.txt
			//if no album : "name;artist;;year;"
			//if no year: "name;artist;album;;"
			//if no album or year: "name;artist;;;"
			//songs are in alphabetical order
			//removing white space at the beginning and end if any 
			toAdd=toAdd.trim();	
			Song inList = new Song();
			int lastSemicolon=0;
			int semicolonCount=0;
			for (int i=0;i<toAdd.length();i++){
				if (toAdd.charAt(i)==';'){
					if (semicolonCount==0){
						inList.setTitle(toAdd.substring(0,i));	
						lastSemicolon=i;
						semicolonCount++;
					} else if (semicolonCount==1){
						inList.setArtist(toAdd.substring(lastSemicolon+1,i));
						lastSemicolon=i;
						semicolonCount++;
					} else if (semicolonCount==2){
						inList.setAlbum(toAdd.substring(lastSemicolon+1,i));
						lastSemicolon=i;
						semicolonCount++;
					} else {
						inList.setYear(toAdd.substring(lastSemicolon+1,i));
						lastSemicolon=i;
						semicolonCount++;
					}	
				}
			}
			songList.add(inList);
			//now our song object is populated with data in alphabetical order of the list


			
		}
		//closing the stream
		songReader.close();

		//feeding the data to our listview
		listView.setItems(songList);

		//setting our listView to display only the name of the song and the artist in the format:
		// "name | artist"
		// using an anonymous class below

		listView.setCellFactory(param -> new ListCell<Song>() {
			@Override
			protected void updateItem(Song item, boolean empty) {
				super.updateItem(item,empty);

				if (empty || item ==null || item.getTitle()== null || item.getArtist()==null){
					setText(null);
				} else {
					setText(item.getTitle()+" | "+item.getArtist());
				}
			}
		});

		//initializing the listview to select the first song if the observable list is not empty
		//this also sets the textfields to the first song in the details
		if (!songList.isEmpty()){
			listView.getSelectionModel().select(0);
			songName.setText(songList.get(0).getTitle());
			artistName.setText(songList.get(0).getArtist());
			albumName.setText(songList.get(0).getAlbum());
			songYear.setText(songList.get(0).getYear());

		}


		//setting listener to the listview to track changes in selection
		//(i.e: The textfields of the details should change if the user selects another song from the listview)
		listView.getSelectionModel().selectedIndexProperty().addListener(
				(obs,oldVal,newVal) -> updateSelection(mainStage));
						
	}

	
	@FXML
	public void deleteSong(ActionEvent action){
		//idea is to delete the song that is selected in the list view by the user
		//if the list is empty, then error is given to the user in form of popup
		//warns the user before deleting the song
	}

	@FXML
	public void editSong(ActionEvent action){
		//idea is the make the textboxes editable and make hidden buttons below the ui window visible
		//these buttons will be "save edit" and "cancel edit"
		//if the user tries to do anything besides editing these textboxes or clicking "save edit" or "cancel edit", then the user will get a warning
	}

	@FXML
	public void addSong(ActionEvent action){
		//idea is to make the listview point to nothing
		//then make the textboxes editable
		//then make the buttons in the details section of the UI visible
		//these buttons will be "confirm addition" and "cancel addition"
		//if the user tries to do anything different than editing the song data or hitting those buttons, they will get a warning from a popup
		//we will use the helper method addSongHelper above to help us with the addition
	}

	//helper method to add songs to our observable list
	private boolean addSongHelper(Song toAdd) throws Exception{
		//need to iterate through the song observable list to see if the name and artist match case insensitive 		
		//NEED TO ADD IN ALPHABETICAL ORDER
		//IDEA: since our txt file is already sorted, we can just use binary search and figure out where to insert our item
		//We also need to edit the txt file after adding
				
		//BINARY SEARCH: 
		int low=0;
		int high=songList.size()-1;
		int mid;
		boolean lowLast=false;
		while(low<=high){
			mid=(high+low)/2;
			int comparison = toAdd.compareTo(songList.get(mid));
			if (comparison==0){
				//then we found a duplicate and we do not add
				return false;
			} else if (comparison<0){
				high=mid-1;
				lowLast=false;
			} else {
				low=mid+1;
				lowLast=true;
			}
		}
		//now the index high-1 is where we should add
		int addPosition = 0;
		if (lowLast=true){
			addPosition=low-1;
		} else {
			addPosition=high+1;
		}

		if (songList.size()==0){
			addPosition=0;
		}
		//then we did not find a match and we can add the song to the observable list
		//we need to add the song in alphabetical order of names

		//this will insert the item at the right position, and shift elements in the list down one position
		songList.add(addPosition,toAdd);

		//updating the txt file 
		File songData = new File("mySongs.txt");
		BufferedWriter songWriter = new BufferedWriter(new FileWriter(songData));
		//we know our txt file exists because we added it on our start 
		//we will overwrite all the data with our new list now
		for (int i=0;i<songList.size();i++){
			songWriter.write(songList.get(i).getTitle()+";"+songList.get(i).getArtist()+";"+songList.get(i).getAlbum()+";"+songList.get(i).getYear()+";"+"\n"); 
		}
		songWriter.close();
		//now our txt file is updated	

		
		
		return true;
		
	}

	//method to update the textboxes of details if the user selects another song
	private void updateSelection(Stage mainStage){
		songName.setText(listView.getSelectionModel().getSelectedItem().getTitle());			
		artistName.setText(listView.getSelectionModel().getSelectedItem().getArtist());
		albumName.setText(listView.getSelectionModel().getSelectedItem().getAlbum());
		songYear.setText(listView.getSelectionModel().getSelectedItem().getYear());
	} 





}
