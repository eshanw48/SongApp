/* By Vishal Patel and Eshan Wadhwa */

import java.util.Comparator;

public class SongComparator implements Comparator<Song>{

	
	@Override
	public int compare(Song song1, Song song2) {
		return song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase());
	}
}
