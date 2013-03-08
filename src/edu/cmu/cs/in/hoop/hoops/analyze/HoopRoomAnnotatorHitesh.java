package edu.cmu.cs.in.hoop.hoops.analyze;

import java.util.ArrayList;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.hoop.hoops.base.HoopAnalyze;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;

public class HoopRoomAnnotatorHitesh extends HoopAnalyze implements HoopInterface{

	public HoopRoomAnnotatorHitesh(){
		setClassName ("HoopRoomAnnotatorHitesh");
		setHoopDescription ("Room Annotator Hitesh");				
	}

	@Override
	public Boolean runHoop(HoopBase inHoop) {
		// TODO Auto-generated method stub
		
		//Get list of key values pairs in the current hoop -> these were passed in from the previous hoop
		ArrayList<HoopKV> inputData = inHoop.getData();
		
		//iterate over inputData 
		for (HoopKV temp : inputData){
			String s = temp.getValueAsString();
			
		}
		
		return true;
	}

	@Override
	public HoopBase copy() {
		// TODO Auto-generated method stub
		return new HoopRoomAnnotatorHitesh();
	}
	
}
