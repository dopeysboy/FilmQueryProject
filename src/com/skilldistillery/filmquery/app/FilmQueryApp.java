package com.skilldistillery.filmquery.app;

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
	private Scanner kb = new Scanner(System.in);

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
		app.launch();
	}

	private void launch() {
		while (true) {
			startUserInterface();
		}
	}

	private void startUserInterface() {
		boolean goodInput = false;

		String input;

		while (!goodInput) {
			System.out.println("What would you like to do?");
			System.out.println("\t1. Search a film by ID");
			System.out.println("\t2. Look up a film by keyword");
			System.out.println("\t3. Exit");

			input = kb.nextLine();

			switch (input) {
			case "1":
				filmIDSearch();
				goodInput = true;
				break;
			case "2":
				filmKeywordSearch();
				goodInput = true;
				break;
			case "3":
				System.out.println("Goodbye!");
				System.exit(0);
				break;
			default:
				System.err.println("Please enter a number 1-3!");
				break;
			}
		}
	}

	private void filmIDSearch() {
		boolean goodInput = false;
		int usrInput = -2;

		while (!goodInput) {
			System.out.print("What is the ID of the film you are looking for? ");
			String temp = kb.nextLine();

			try {
				usrInput = Integer.parseInt(temp);
				goodInput = true;
			} catch (NumberFormatException e) {
				System.err.println("Please enter a number!");
			}
		}

		Film film = db.findFilmById(usrInput);

		printFilms(film);
	}

	private void filmKeywordSearch() {
		System.out.print("What is the phrase you would like to search for? ");
		String usrInput = kb.nextLine();
		
		List<Film> searchResults = db.findFilmsByKeyword(usrInput);
		
		printFilms(searchResults);
	}
	
	private void printFilms(Film film) {
		if(film == null) {
			System.out.println("Your search did not return anything!");
		} else {
			simplePrintFilm(film);
		}
	}
	
	private void printFilms(List<Film> films) {
		if(films.size() == 0) {
			System.out.println("No results found!");
		} else {
			for(Film f : films) {
				simplePrintFilm(f);
			}
		}
	}
	
	private void simplePrintFilm(Film film) {
		List<Actor> cast = film.getCast();
		StringBuilder sb = new StringBuilder();
		
		sb.append(film.getTitle()).append("\n\t-");
		sb.append("Film ID: ").append(film.getId()).append("\n\t-");
		sb.append(film.getReleaseYear()).append("\n\t-");
		sb.append(film.getRating() == null ? "N/A" : film.getRating()).append("\n\t-");
		sb.append(film.getDescription() == null ? "N/A" : film.getDescription()).append("\n\t-");
		sb.append(langMap.get(film.getLanguageId())).append("\n\t-");
		sb.append("Cast:\n");
		
		for(Actor a : cast) {
			sb.append("\t\t*").append(a).append("\n");
		}
		
		System.out.println(sb);
	}
	
	private void fullPrintFilm(Film film) {
		System.out.println(film);
	}
}
