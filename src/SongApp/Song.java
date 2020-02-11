
//class for properties of a song

public class Song implements Comparable<Song>{

	//every song needs a name and artist, so that will by my constructor
	
	String name;
	String artist;
	String album;
	int year;

	public Song(){

	}

	public Song(String name, String artist){
		this.name=name;
		this.artist=artist;
	}

	//we wish to order songs based on their name first and then their artist second
	//if the song has the same artist and name CASE INSENSITIVE, then they are the same song
	
	//compareto to sort first by name and then by artist if the names are the same
	//I will use the string compare to to help me with this method
	public int compareTo(Song other){
		int nameComparison =this.name.toUpper().compareTo(other.name.toUpper()); 
		int artistComparison =this.artist.toUpper().compareTo(other.artist.toUpper())==0;

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
