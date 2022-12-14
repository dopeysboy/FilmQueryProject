package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.Rating;

public class DatabaseAccessorObject implements DatabaseAccessor {

	private static String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=US/Central";
	private static String USR = "student";
	private static String PASS = "student";

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;

		try {
			Connection conn = DriverManager.getConnection(URL, USR, PASS);
			String sql = "SELECT * FROM film JOIN film_category fc ON film.id = fc.film_id JOIN category cat ON fc.category_id = cat.id WHERE film.id = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);

			ResultSet rs = stmt.executeQuery();

			//get the film
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String description = rs.getString("description");
				int releaseYear = rs.getInt("release_year");
				int languageId = rs.getInt("language_id");
				int rentalDuration = rs.getInt("rental_duration");
				double rentalRate = rs.getDouble("rental_duration");
				int length = rs.getInt("length");
				double replacementCost = rs.getDouble("replacement_cost");
				Rating rating = Rating.valueOf(rs.getString("rating"));
				String features = rs.getString("special_features");
				String category = rs.getString("name");
				
				film = new Film(id, title, description, releaseYear, languageId, rentalDuration, rentalRate, length,
						replacementCost, rating, features, category);
			}
			
			rs.close();
			stmt.close();
			conn.close();

			if(film != null) {
				//get the actors for the film
				sql = "SELECT act.* FROM film JOIN film_actor fa ON film.id = fa.film_id JOIN actor act ON fa.actor_id = act.id WHERE film.id = ?";			
				List<Actor> cast = new ArrayList<>();
				
				
				conn = DriverManager.getConnection(URL, USR, PASS);
				stmt = conn.prepareStatement(sql);
				
				stmt.setInt(1, filmId);
				rs = stmt.executeQuery();
				
				while(rs.next()) {
					int id = rs.getInt("id");
					String firstName = rs.getString("first_name");
					String lastName = rs.getString("last_name");
					
					cast.add(new Actor(id, firstName, lastName));
				}
				
				film.setCast(cast);
				
				rs.close();
				stmt.close();
				conn.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return film;
	}
	
	@Override
	public Map<Integer, String> getLangs(){
		Map<Integer, String> returnMap = new HashMap<>();
		
		try {
			Connection conn = DriverManager.getConnection(URL, USR, PASS);
			String sql = "SELECT * FROM language";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				Integer key = rs.getInt("id");
				String value = rs.getString("name");
				returnMap.put(key, value);
			}
			
			rs.close();
			stmt.close();
			conn.close();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return returnMap;
	}
	
	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;

		try {
			Connection conn = DriverManager.getConnection(URL, USR, PASS);

			String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");

				actor = new Actor(id, firstName, lastName);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> returnList = new ArrayList<>();
		
		String sql = "SELECT act.* FROM film JOIN film_actor fa ON film.id = fa.film_id JOIN actor act ON fa.actor_id = act.id WHERE film.id = ?";
		try(Connection conn = DriverManager.getConnection(URL, USR, PASS);
			PreparedStatement stmt = conn.prepareStatement(sql);){
			
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				
				returnList.add(new Actor(id, firstName, lastName));
			}
			
			rs.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return returnList;
	}

	@Override
	public List<Film> findFilmsByKeyword(String input){
		List<Film> returnList = new ArrayList<>();
		String sql;
		String search = "%" + input + "%";
		
		Film workingFilm = null, testFilm;
		
		try(Connection conn = DriverManager.getConnection(URL, USR, PASS);){
			sql = "SELECT * FROM film JOIN film_actor fa ON film.id = fa.film_id JOIN actor act ON fa.actor_id = act.id JOIN film_category fc ON film.id = fc.film_id JOIN category cat ON fc.category_id = cat.id WHERE film.title LIKE ? OR film.description LIKE ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, search);
			stmt.setString(2, search);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("film_id");
				String title = rs.getString("title");
				String description = rs.getString("description");
				int releaseYear = rs.getInt("release_year");
				int languageId = rs.getInt("language_id");
				int rentalDuration = rs.getInt("rental_duration");
				double rentalRate = rs.getDouble("rental_duration");
				int length = rs.getInt("length");
				double replacementCost = rs.getDouble("replacement_cost");
				Rating rating = Rating.valueOf(rs.getString("rating"));
				String features = rs.getString("special_features");
				String category = rs.getString("name");
				
				int actorId = rs.getInt("actor_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				
				Actor actor = new Actor(actorId, firstName, lastName);
				
				testFilm = new Film(id, title, description, releaseYear, languageId, rentalDuration, rentalRate, length,
						replacementCost, rating, features, category);
				
				//first entry will always add a null value to the list, unsure how to fix that
				//right now
				if(workingFilm == null || !workingFilm.equals(testFilm)) {
					returnList.add(workingFilm);
					workingFilm = testFilm;
					workingFilm.addActor(actor);
				} else {
					workingFilm.addActor(actor);
				}

			}
			returnList.add(workingFilm);
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		//remove the first value (null)
		if(returnList != null) {
			returnList.remove(0);
		}
		
		return returnList;
	}
}
