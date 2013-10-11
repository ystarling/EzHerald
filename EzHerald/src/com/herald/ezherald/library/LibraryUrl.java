package com.herald.ezherald.library;

public class LibraryUrl {
	
	String LIBRARY_SEARCH_URL="http://herald.seu.edu.cn/herald_web_service/library/search_book/";
	String LIBRARY_SEARCH_DETAIL="http://herald.seu.edu.cn/herald_web_service/library/book_detail/";
	String LIBRARY_MINE_BOOKS="http://herald.seu.edu.cn/herald_web_service/library/rendered_books/";
	
	public String getLIBRARY_MINE_BOOKS() {
		return LIBRARY_MINE_BOOKS;
	}

	public void setLIBRARY_MINE_BOOKS(String lIBRARY_MINE_BOOKS) {
		LIBRARY_MINE_BOOKS = lIBRARY_MINE_BOOKS;
	}

	public String getLIBRARY_SEARCH_DETAIL() {
		return LIBRARY_SEARCH_DETAIL;
	}

	public void setLIBRARY_SEARCH_DETAIL(String lIBRARY_SEARCH_DETAIL) {
		LIBRARY_SEARCH_DETAIL = lIBRARY_SEARCH_DETAIL;
	}

	public String getLIBRARY_SEARCH_URL() {
		return LIBRARY_SEARCH_URL;
	}

	public void setLIBRARY_SEARCH_URL(String lIBRARY_SEARCH_URL) {
		LIBRARY_SEARCH_URL = lIBRARY_SEARCH_URL;
	}
	
	
}
