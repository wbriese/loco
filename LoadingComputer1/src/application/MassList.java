package application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

public class MassList {
	
	private static ObservableList<Mass> massList;
	
	private MassList () {}
	
	private static void createInitialMassList() {
				
		// Create an extractor for DoubleProperties mass,x,y,z,		
				Callback<Mass, Observable[]> extractor = (Mass m) -> {
							// Wrap the parameter in an Observable[] and return it
							return new Observable[]{m.massProperty(),m.xProperty(),m.yProperty(),m.zProperty()};
						};
		
		massList = FXCollections.observableArrayList(extractor);
		massList.addAll(importWeights());
		massList.addAll(importTanks());
		
				
	};
	
	private static Collection<Tank> importTanks() {
		
		List <Tank> tankList = new ArrayList<>();
		
		// load tanks
		ShipData shipDataObject =ShipData.getInstance();
		shipDataObject.getTankList().forEach(jsonTank->tankList.add(createTank(jsonTank)));
		
		//fill tanks with loadcase data
		LoadCaseData loadCaseData=LoadCaseData.getInstance();
		
		loadCaseData.getTankList().forEach(tankFillInfo->findAndFillTank(tankFillInfo,tankList));
		
		return tankList;
						
	}

	private static void findAndFillTank(Map<String, Object> tankFillInfo, List<Tank> tankList) {
		String identifier=(String) tankFillInfo.get("id");
		double density=((BigDecimal)tankFillInfo.get("density")).doubleValue();
		double mass = ((BigDecimal)tankFillInfo.get("mass")).doubleValue();
		
		//find Tank
		Tank tankToFill=tankList.stream().filter(tank->tank.getMassIdentifier().equals(identifier)).findFirst().get();
		
		//fill Tank
		tankToFill.setDensity(density);
		tankToFill.setMass(mass);
	}

	private static Tank createTank(Map<String, Object> jsonTank) {
		
				
		String id=(String) jsonTank.get("id");
		String svg=(String) jsonTank.get("svg");
		BigDecimal xa = (BigDecimal) jsonTank.get("xa");
		BigDecimal xf = (BigDecimal) jsonTank.get("xf");
		
		MassCategory category= switch ((String)jsonTank.get("type")) {
			case "fuel"-> MassCategory.HFOTANK;
			case "ballast"-> MassCategory.BALLASTTANK;
			case "luboil" -> MassCategory.LUBTANK;
			case "holdwater"->MassCategory.HOLDWATER;			
			default -> null;
		};
		
			
		BigDecimal mass=new BigDecimal(0);
		BigDecimal density=new BigDecimal(1.0);
		Collection<Map<String,BigDecimal>> tankTable= (Collection<Map<String,BigDecimal>>)jsonTank.get("table");
		
		return new Tank(id,svg,category,mass,density,tankTable, xa,xf);
	}

	private static Collection<Mass> importWeights() {
		ArrayList<Mass> weightList = new ArrayList<Mass>();
//		weightList.add(new Mass("Lightship", MassCategory.LIGHTSHIP, 6986.2,61.07,0.77,10.08,0.0));
//		weightList.add(new Mass("Cargo", MassCategory.CARGO, 7588.0, 72.798,-0.603,11.639,68.7));
//		weightList.add(new Mass("Stores", MassCategory.STORE, 604.3, 77.4,0.51,8.91,0.0));
		
		return weightList;
	}

	/* Returns an observable list of masses */
	public static ObservableList<Mass> getInstance() {
		if (massList==null) createInitialMassList(); 
		return massList;
			
	}
	
	public static void bindHydrostatic(Hydrostatic hydro) {
		//update Hydrostatics if values change
				massList.addListener(hydro::onChanged);
	}
	
	/* Returns Mass Id TableColumn */	
	public static TableColumn<Mass, Integer> getIdColumn() {
		TableColumn<Mass, Integer> massIdCol = new TableColumn<>("Id");
		massIdCol.setCellValueFactory(new PropertyValueFactory<>("massId"));
		return massIdCol;
	}
	
	/* Returns Identifier TableColumn */ 
	public static TableColumn<Mass, String> getMassIdentifierColumn() {
		TableColumn<Mass, String> massIdentifierCol = new TableColumn<>("Description");
		massIdentifierCol.setCellValueFactory(new PropertyValueFactory<>("massIdentifier"));
		return massIdentifierCol;
	}
	
	/* Returns Mass TableColumn */ 
	public static TableColumn<Mass, Number> getMassColumn() {
		TableColumn<Mass, Number> massCol = new TableColumn<>("mass");
		//massCol.setCellValueFactory(item -> item.getValue().massProperty());
		massCol.setCellValueFactory(new PropertyValueFactory<>("mass"));
		return massCol;
	}
	
	/* Returns x TableColumn */ 
	public static TableColumn<Mass, Double> getMassXColumn() {
		TableColumn<Mass, Double> massXCol = new TableColumn<>("x");
		massXCol.setCellValueFactory(new PropertyValueFactory<>("x"));
		return massXCol;
	}
	
	/* Returns y TableColumn */ 
	public static TableColumn<Mass, Double> getMassYColumn() {
		TableColumn<Mass, Double> massYCol = new TableColumn<>("y");
		massYCol.setCellValueFactory(new PropertyValueFactory<>("y"));
		return massYCol;
	}
	
	/* Returns z TableColumn */ 
	public static TableColumn<Mass, Double> getMassZColumn() {
		TableColumn<Mass, Double> massZCol = new TableColumn<>("z");
		massZCol.setCellValueFactory(new PropertyValueFactory<>("z"));
		return massZCol;
	}
	
	
	
	
	
}
