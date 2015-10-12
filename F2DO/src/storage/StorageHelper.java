package storage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import org.json.JSONObject;
import objects.Task;
import org.json.XML;
public class StorageHelper {

	@SuppressWarnings("unchecked")
	public static ArrayList<JSONObject> jsonList(ArrayList<Task> taskList){
		ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
		JSONObject obj;
		for (int i = 0; i<taskList.size();i++){
			obj = new JSONObject();
			obj.put("TaskID", taskList.get(i).getTaskID());
			obj.put("TaskName", taskList.get(i).getTaskName());
			obj.put("StartDate", taskList.get(i).getStartDate());
			obj.put("EndDate", taskList.get(i).getEndDate());
			jsonList.add(obj);
		}

		return jsonList;

	}

	public void jsonListToString(ArrayList<JSONObject> jsonList){
		StringWriter out;
		JSONObject obj;
		for (int i = 0;i<jsonList.size();i++){
			out = new StringWriter();
			obj = new JSONObject();
			obj = jsonList.get(i);
			try {
				//obj.writeJSONString(out);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error printing JSON Object");
			}
			String jsonText = out.toString();
			System.out.println(jsonText);
		}
	}

	public static void convertToXML(ArrayList<JSONObject> jsonList){
		JSONObject obj;
		String xml;

		FileWriter file,file2;
		try {
			file = new FileWriter("test.json");
			file2 = new FileWriter("test.xml");


			for (int i = 0;i<jsonList.size();i++){
				obj = new JSONObject();
				obj = jsonList.get(i);
				xml = XML.toString(obj);
				file.write(obj.toString());
				file2.write(xml);
				//System.out.println(xml);

			}
			file.flush();
			file.close();
			file2.flush();
			file2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
