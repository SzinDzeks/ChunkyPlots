package me.szindzeks.ChunkyPlots.manager;

import com.sun.istack.internal.NotNull;
import me.szindzeks.ChunkyPlots.basic.VisitPoint;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.List;

public class VisitManager {
	private List<VisitPoint> visitPoints = new ArrayList<>();

	public VisitManager(){ loadVisitPoints(); }
	public void finalize(){ saveVisitPoints(); }

	@NotNull
	public void createVisitPoint(VisitPoint visitPoint){
		visitPoints.add(visitPoint);
	}
	@NotNull
	public void deleteVisitPoint(VisitPoint visitPoint){
		visitPoints.remove(visitPoint);
	}
	@NotNull
	public VisitPoint getVisitPoint(String name){
		for(VisitPoint visitPoint:visitPoints){
			if(visitPoint.getName().equalsIgnoreCase(name)) return visitPoint;
		}
		return null;
	}
	public List<VisitPoint> getVisitPoints() { return visitPoints; }

	private void loadVisitPoints(){
		//TODO: Loading visit points from file
	}

	private void saveVisitPoints(){
		//TODO: Saving visit points to file
	}
}
