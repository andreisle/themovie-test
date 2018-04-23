package com.themovie.test.database.model;

public class Movie {

	Long id;
	String title;
	String release_date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRelease_date() {
		return release_date;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Movie))
			return false;

		Movie other = (Movie) obj;

		return this.id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	public int getYear() {
		return Integer.valueOf(release_date.substring(0, 4));
	}

	public int getMonth() {
		return Integer.valueOf(release_date.substring(5, 7));
	}

}
