package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.*;

/**
 * This class represents the backend for managing all the operations associated
 * with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {

	// List of all the food items.
	private List<FoodItem> foodItemList;
	private List<FoodItem> filteredFoodItem1;
	private List<FoodItem> filteredByName1;
	private boolean file;//use to throw exception

	// Map of nutrients and their corresponding index
	private HashMap<String, BPTree<Double, FoodItem>> indexes;

	/**
	 * Public constructor
	 */
	public FoodData() {
		//initialize variables
		foodItemList = new ArrayList<FoodItem>();
		filteredFoodItem1 = new ArrayList<FoodItem>();
		indexes = new HashMap<String, BPTree<Double, FoodItem>>();
		filteredByName1 = new ArrayList<FoodItem>();
		file = false;
		//construct the hashMap, put BPTree to the hashMap;
		indexes.put("calories",new BPTree<Double, FoodItem>(3));
		indexes.put("carbohydrate",new BPTree<Double, FoodItem>(3));
		indexes.put("fat",new BPTree<Double, FoodItem>(3));
		indexes.put("fiber",new BPTree<Double, FoodItem>(3));
		indexes.put("protein",new BPTree<Double, FoodItem>(3));
	}

	/**
	 * Loads the data from .csv file and store the nutrients value in BPTree for range Searching
	 * We construct the same number of BPTree as the number of the nutrients in each Item.
	 * This class firstly read line by line from input file through scanner. 
	 * 
	 * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
	 */
	@Override
	public void loadFoodItems(String filePath) {
	    file = false;//initialize file to check if there is an exception
		try {
			File file = new File(filePath);
			Scanner scnr = new Scanner(file);
			String receiver = null;//receive line content from scanner
			int i = 0;//counter
			String[] fullLine = null;//store the correct information of line
			String[] lineContent = null;//store the information in line
			//this loop gets all line information, store them in foodItem, and store foodItem in foodItemList
			while (scnr.hasNextLine()) {
				lineContent = null;//clear previous information
				FoodItem item = null;//clear previous information
				receiver = scnr.nextLine();//gets line content
				if (receiver != null) {
					lineContent = receiver.split(",");//gets information
				    if(lineContent.length != 12) {//invalid line continue
				    	continue;
				    }
				    if(lineContent.length > 12) {
				    	continue;
				    }
				    if(lineContent.length == 12){//if it is a valid line, record that, to construct the BPTree
						fullLine = lineContent = receiver.split(",");
					}
					item = new FoodItem(lineContent[0], lineContent[1]);//construct new foodItem
					//add the nutrient information to this item
					for (i = 2; i < lineContent.length; i = i + 2) {
						item.addNutrient(lineContent[i].toLowerCase(), Double.parseDouble(lineContent[i + 1]));
					}
				}
				foodItemList.add(item);//add foodItem to foodItemList
			}
			if(foodItemList.size() != 0) {
			//this for-loop put the foodItem and nutrient value to corresponding BPTree
			for (FoodItem foodItem : foodItemList) {
				i = fullLine.length;
				for (int j = 0; j < (fullLine.length - 2) / 2; ++j) {
					indexes.get(fullLine[i - 2].toLowerCase())
							.insert(foodItem.getNutrientValue(fullLine[i - 2].toLowerCase()), foodItem);
					
					i = i - 2;
					if (i < 2) {
						break;
					}
				}
			}
			}
			if(scnr != null) {
			scnr.close();}
		} catch (FileNotFoundException e) {
			file = true;//set exception mark to true, to pop up window
			e.printStackTrace();
		}
		
	}

	/**T
	 * Gets all the food items that have name containing the substring.
	 * 
	 * @see skeleton.FoodDataADT#filterByName(java.lang.String)
	 */
	@Override
	public List<FoodItem> filterByName(String substring) {
		if (substring == null || substring == "") {//if it is null, return original foodItemList
			filteredByName1 = new ArrayList<FoodItem>(foodItemList);
		} else {
			filteredByName1.clear();//clear previous list
			//this for-loop check each item's name, if contains specific substring, add it to list
			for (FoodItem foodItem : foodItemList) {
				if (foodItem.getName().toLowerCase().contains(substring.toLowerCase())
						&& !filteredByName1.contains(foodItem)) {
					filteredByName1.add(foodItem);
				}
			}
		}
		return filteredByName1;
	}

	/**
	 * Gets all the food items that fulfill ALL the provided rules,stored them in a list to return.
	 * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
	 */
	@Override
	public List<FoodItem> filterByNutrients(List<String> rules) {
		List<FoodItem> filteredNutrient = new ArrayList<FoodItem>();//create list to stored specific foodItem
		filteredNutrient.addAll(foodItemList);
		if (rules.size() == 0 || rules == null) {//if rules are null, return original list
			filteredFoodItem1 = new ArrayList<FoodItem>(foodItemList);
			return filteredFoodItem1;
		} else {
			//initialize variable
			String rule = null;
			int length = rules.size();
			int i = 0;			
			List<FoodItem> getter = new ArrayList<FoodItem>();//stored the returned list from rangeSearch
			//this for-loop iterate by the size of rules.
			for (i = 0; i < length; ++i) {
				rule = rules.get(i);
				String[] receiver = rule.split(" ");//split rule
				//gets rule information
				String name = receiver[0].toLowerCase();
				String comparator = receiver[1];
				String value = receiver[2];
				//use BPTree to gets the correct foodItem
				getter = indexes.get(name).rangeSearch(Double.parseDouble(value), comparator);
				filteredNutrient.retainAll(getter);       
			}
			filteredFoodItem1 = filteredNutrient;
			return filteredNutrient;
		}
	}

	/*
	 * This method add foodItem to foodItemList, and to the BPTree.
	 * 
	 * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
	 */
	@Override
	public void addFoodItem(FoodItem foodItem) {
		foodItemList.add(foodItem);
		indexes.get("calories").insert(foodItem.getNutrientValue("calories"),foodItem);
		indexes.get("carbohydrate").insert(foodItem.getNutrientValue("carbohydrate"),foodItem);
		indexes.get("fat").insert(foodItem.getNutrientValue("fat"),foodItem);
		indexes.get("protein").insert(foodItem.getNutrientValue("protein"),foodItem);
		indexes.get("fiber").insert(foodItem.getNutrientValue("fiber"),foodItem);
	}

	/*
	 * This method gets all foodItem from sorted list
	 * 
	 * @see skeleton.FoodDataADT#getAllFoodItems()
	 */
	@Override
	public List<FoodItem> getAllFoodItems() {
		foodItemList.sort((h1, h2) -> h1.getName().toLowerCase().compareTo(h2.getName().toLowerCase()));//sort list
		return foodItemList;
	}
/**
 * This method save the sorted foodItemList to a target destination.
 */
	@Override
	public void saveFoodItems(String filename) {
	    file = false;
		foodItemList.sort((h1, h2) -> h1.getName().toLowerCase().compareTo(h2.getName().toLowerCase()));//sort list
		try {
			//use fileWriter and printWriter to construct output file
			FileWriter fileWriter = new FileWriter(filename);
			PrintWriter printWriter = new PrintWriter(filename);
			//this for loop save foodItemList to a new file
			for (FoodItem foodItem : foodItemList) {
				printWriter.println(foodItem.getID() + "," + foodItem.getName() + "," + "calories" + ","
						+ foodItem.getNutrientValue("calories") + "," + "fat" + "," + foodItem.getNutrientValue("fat")
						+ "," + "carbohydrate" + "," + foodItem.getNutrientValue("carbohydrate") + "," + "fiber" + ","
						+ foodItem.getNutrientValue("fiber") + "," + "protein" + ","
						+ foodItem.getNutrientValue("protein"));
			}
			printWriter.flush();
			fileWriter.flush();
		} catch (IOException e) {
			file = true;
		}

	}
/**
 * getter for exception marker
 * @return
 */
	public boolean fileNotFound() {
		return file;
	}
	
	public static void main(String[] args) {
		FoodData xs = new FoodData();
		xs.loadFoodItems("foodItems.csv");
		for (int i = 0; i <= 100000; i++) {
			FoodItem sb = new FoodItem("xiaosan", ""+i);
			sb.addNutrient("calories", 1);
			sb.addNutrient("fat", 1);
			sb.addNutrient("carbohydrate", 1);
			sb.addNutrient("fiber", 1);
			sb.addNutrient("protein", 1);
			xs.addFoodItem(sb);
		}
		xs.saveFoodItems("xs.csv");
	}
}
