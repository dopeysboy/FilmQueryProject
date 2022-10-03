package com.skilldistillery.filmquery.app;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	private DatabaseAccessor db = new DatabaseAccessorObject();
	private Map<Integer, String> langMap = db.getLangs();
	
	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
		app.test();
//    app.launch();
	}

	private void test() {
		Film film = db.findFilmById(1);
		
		Iterator it = langMap.keySet().iterator();
		while(it.hasNext()) {
			Object key = it.next();
			String value = langMap.get(key);
			
			System.out.println(key + " " + value);
		}
		/*
		 * film -> language id
		 * String value = langMap.get(film.getLanguageID)
		 * 
		 * int key = film.getLanguageID
		 * String value = langmap.get(key)
		 */
	}

	private void launch() {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) {
		System.out.println("What would you like to do?");
		System.out.println("\t1. Search a film by ID");
		System.out.println("\t2. Look up a film by keyword");
		System.out.println("\t3. Exit");
	}

}
