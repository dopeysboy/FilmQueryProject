package com.skilldistillery.filmquery.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public interface DatabaseAccessor {

	public Film findFilmById(int filmId);
	
	public Actor findActorById(int actorId);

	public List<Actor> findActorsByFilmId(int filmId);
	
	public List<Film> findFilmsByKeyword(String input); 

	public Map<Integer, String> getLangs(); 
}
