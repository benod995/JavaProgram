import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.BiConsumer;
public class Main {

	public static void main(String[] args)throws IOException{
		File file = new File("logs.csv");
		FileReader fr = new FileReader(file);
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(fr);//(isr);
		// Reader r = br;
		String Headers[] = {"AlarmId","HistoryAction",
				"AlarmKey","Node","SubMethod","AlarmGroup","SubAlarmGroup","AlarmType","Reported","Severity"};

		String line;
		// skip the first line
		line = br.readLine();
		int lineno=0;
		Alarm a;
		ArrayList<Alarm> alarms = new ArrayList<Alarm>();
		HashMap<String,ArrayList<Alarm>> nodes = new HashMap<String,ArrayList<Alarm>>();	
		while ((line = br.readLine())!=null ){
			a = new Alarm();
			String type="";
			String val ="";
			String line1=line+",";
			lineno++;

			// Only process the first 10 for now
			if (lineno>1000) break;
			for (int i=0;i<Headers.length;i++){
				try {
					int index =0;
					if (line1.charAt(0)=='"'){
						index = line1.indexOf('"',1);
						val = line1.substring(0, index+1);
						line1 = line1.substring(index+2, line1.length());

					}
					else{

						index = line1.indexOf(',');
						val = line1.substring(0, index);
						line1 = line1.substring(index+1, line1.length());
					}
					//System.out.println("type "+type);
					//System.out.println("val "+val);
					//	System.out.println("name "+Headers[i]);
					//		System.out.println(" line "+line1);
					switch(i){
					case 0:
						a.setAlarmId(Long.parseLong(val));
						break;
					case 1:
						a.setHistoryAction(val);
						break;
					case 2:
						a.setAlarmKey(val);
						break;
					case 3:
						a.setNode(val);
						break;
					case 4:
						a.setSubMethod(val);
						break;
					case 5:
						a.setAlarmGroup(val);
						break;
					case 6:
						a.setSubAlarmGroup(val);
						break;
					case 7:
						a.setAlarmType(Integer.parseInt(val));
						break;
					case 8:
						a.setReported(Long.parseLong(val));
						break;
					case 9:
						a.setSeverity(Integer.parseInt(val));
						break;
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("*type "+type);
					System.out.println("*val "+val);
					System.out.println("*name "+Headers[i]);
					System.out.println("*line "+line1);
					e.printStackTrace();
				}

			}
			alarms.add(a);
			//System.out.println(" Alarm "+a);

			ArrayList<Alarm> aa = nodes.get(a.getNode());
			if (aa == null){
				// the node is not in the hashmap so add in an 
				aa = new ArrayList<Alarm>();
				nodes.put(a.getNode(),aa);
			}
			aa.add(a);
		}

		Scanner scan = new Scanner(System.in);
		System.out.println("=============================");
		System.out.println("Please enter an option");
		System.out.println("1: Output all the alarms");
		System.out.println("2: Output all the alarms for a given value of Alarmid");
		System.out.println("3: Output all alarms that have the text linkUpDown in the AlarmKey field");
		System.out.println("4: Enter a field name and a search text");
		System.out.println("5: Output all the alarms sorted by node in alphabetical order");
		System.out.println("6: List all alarms sorted in node order for a given value of SubMethod");
		int index=0;
		while (true){
			String choice = scan.nextLine();
			switch(choice){
			case "1":
				// print all alarms
				for (index=0;index<alarms.size();index++){
					a = alarms.get(index);
					System.out.println(a);
				}

				break;
			case "2":
				System.out.println("alarm id" );
				String id = scan.nextLine();
				// go through the arraylist one item at a time
				for (index=0; index<alarms.size(); index++){
					a = alarms.get(index);
					/* to do */
					if (a.getAlarmId() == Float.valueOf(id) ){
						System.out.println(a);
					}
				}

				break;
			case "3":

				for (index=0; index<alarms.size(); index++){
					a = alarms.get(index);

					if (a.getAlarmKey().contains("linkUpDown")){
						System.out.println(a);
					}
				}

				break;
			case "4":

				System.out.println("Please enter a Field Key ");
				String field = scan.nextLine();
				System.out.println("Please enter a Search Text ");
				String search = scan.nextLine();

				for (index=0; index<alarms.size(); index++){
					a = alarms.get(index);

					if(field.equalsIgnoreCase("HistoryAction") && a.getHistoryAction().contains(search)){
						a.getAlarmId();
						System.out.println(a);
					}

					else if(field.equalsIgnoreCase("AlarmKey") && a.getAlarmKey().contains(search)){
						a.getAlarmId();
						System.out.println(a);
					}

					else if(field.equalsIgnoreCase("Node") && a.getNode().contains(search)){
						a.getAlarmId();
						System.out.println(a);
					}

					else if(field.equalsIgnoreCase("SubMethod") && a.getSubMethod().contains(search)){
						a.getAlarmId();
						System.out.println(a);
					}

					else if(field.equalsIgnoreCase("AlarmGroup") && a.getAlarmGroup().contains(search)){
						a.getAlarmId();
						System.out.println(a);
					}

					else if(field.equalsIgnoreCase("SubAlarmGroup") && a.getSubAlarmGroup().contains(search)){
						a.getAlarmId();
						System.out.println(a);
					}
					else{
						System.out.println("Sorry that option is invalid, try again");
						break;
					}
				}


				break;
			case "5":
				// not sorted
				// get all the keys from the hashmap. The keySet returns an ArrayList
				// of Alarms
				ArrayList<String> l = new ArrayList<String>();
				for (String key : nodes.keySet()) {
					l.add(key);
				}

				System.out.println("These are the alarms sorted by alphabetical order of nodes:");
				Collections.sort(l, new Comparator<String>() {
					public int compare(String o1, String o2) {

						return (o1.compareTo(o2));
					}
				});
				for (int y =0;y<l.size();y++){
					System.out.println("Alarms for node "+l.get(y));
					System.out.println("");

					ArrayList<Alarm> aa = nodes.get(l.get(y));
					for (int g=0;g<aa.size();g++){
						Alarm s = aa.get(g);	

						System.out.println(s);
					}
				}


				break;
			case "6":
				// not sorted
				// get all the keys from the hashmap. The keySet returns an ArrayList
				// of Alarms
				System.out.println("Please enter a SubMetod value:");
				String value = scan.nextLine();
				ArrayList<String> l2 = new ArrayList<String>();
				a = alarms.get(index);
				for (String key : nodes.keySet()) {
					l2.add(key);
				}

				System.out.println("These are the alarms sorted by alphabetical order of nodes with given Submethod \"" + value + "\":");
				Collections.sort(l2, new Comparator<String>() {
					public int compare(String o1, String o2) {

						return (o1.compareTo(o2));
					}
				});
				for (int y =0;y<l2.size();y++){
					if (a.getSubMethod().contains(value)){
						System.out.println("Alarms for node "+l2.get(y));
						System.out.println("");

						ArrayList<Alarm> aa = nodes.get(l2.get(y));
						for (int g=0;g<aa.size();g++){
							Alarm s = aa.get(g);	

							System.out.println(s);
						}
					}
				}
				break;
			}

		}

	}
}


