package com.skilldistillery.filmquery.entities;

public enum Rating {
	G("G"), PG("PG"), PG13("PG13"), R("R"), NC17("NC17");
	
	String ratingName;
	
	private Rating(String ratingName) {
		this.ratingName = ratingName;
	}
	
	@Override
	public String toString() {
		return this.ratingName;
	}
}
