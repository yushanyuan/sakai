package cn.net.sikai.cc;

import java.util.HashMap;
import java.util.Map;

public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 //{Oct%2021%20meeting%20presentation%20first%20half%20480p.mp4=http://localhost:8080/access/content/group/47d0b65f-d9af-46dd-a609-d4c55737432f/meleteoldrec/Oct%2021%20meeting%20presentation%20first%20half%20480p.mp4, Oct 21 meeting presentation first half 480p.mp4=http://localhost:8080/access/content/group/47d0b65f-d9af-46dd-a609-d4c55737432f/meleteoldrec/Oct 21 meeting presentation first half 480p.mp4, Best_Practices_Guide_full-2.9.4_final.pdf=http://localhost:8080/access/content/group/47d0b65f-d9af-46dd-a609-d4c55737432f/meleteoldrec/Best_Practices_Guide_full-2.9.4_final.pdf, WhatsNewIn2.9.4July152014.pdf=http://localhost:8080/access/content/group/47d0b65f-d9af-46dd-a609-d4c55737432f/meleteoldrec/WhatsNewIn2.9.4July152014.pdf, IMG_8860.JPG=http://localhost:8080/access/content/group/47d0b65f-d9af-46dd-a609-d4c55737432f/meleteoldrec/IMG_8860.JPG}
		Map<String, String> map = new HashMap<String,String>();
			 //Oct%2021%20meeting%20presentation%20second%20half%20480p.mp4
		
		map.put("Oct%2021%20meeting%20presentation%20first%20half%20480p.mp4", "http://localhost:8080/access/content/group/47d0b65f-d9af-46dd-a609-d4c55737432f/meleteoldrec/Oct%2021%20meeting%20presentation%20first%20half%20480p.mp4");
		System.out.println(map.get("Oct%2021%20meeting%20presentation%20second%20half%20480p.mp4"));
	}

}
