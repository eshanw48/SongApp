	
//class for properties of a song

public class Song implements Comparable<Song>{

//every song needs a name and artist, so that will by my constructor
	private String title;
	private String artist;
	private String album;
	private String year;
	

	public Song(){
	
	}

	public Song(String title, String artist, String album, String year)
	{
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
	
	}


	//Below are our getter methods
	
	public String getTitle() {
		return this.title;
	}
	
	public String getArtist() {
		return this.artist;
	}

	public String getAlbum() {
		return this.album;
	}
	
	public String getYear() {
		return this.year;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public void setYear(String year) {
		this.year = year;
	}



	//we wish to order songs based on their name first and then their artist second
	//if the song has the same artist and name CASE INSENSITIVE, then they are the same song
	
	//compareto to sort first by name and then by artist if the names are the same
	//I will use the string compare to to help me with this method
	public int compareTo(Song other){
		int nameComparison =this.title.toUpperCase().compareTo(other.title.toUpperCase()); 
		int artistComparison =this.artist.toUpperCase().compareTo(other.artist.toUpperCase());

		if (nameComparison ==0){
			if (artistComparison==0){
				return 0;
			} else if (artistComparison>0){
				return 1;
			} else {
				return -1;
			}
		} else if (nameComparison>0){
			return 1;
		} else {
			return -1;
		}

	}

}


