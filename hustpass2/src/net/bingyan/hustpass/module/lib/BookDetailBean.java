package net.bingyan.hustpass.module.lib;

import java.util.List;

public class BookDetailBean {
	String title;
	String[] editor;
	String publisher;
	String[] isbn;
	String coverimg;
	List<BookShelf> detail;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getEditor() {
		return editor;
	}

	public void setEditor(String[] editor) {
		this.editor = editor;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String[] getIsbn() {
		return isbn;
	}

	public void setIsbn(String[] isbn) {
		this.isbn = isbn;
	}

	public String getCoverimg() {
		return coverimg;
	}

	public void setCoverimg(String coverimg) {
		this.coverimg = coverimg;
	}

	public List<BookShelf> getDetail() {
		return detail;
	}

	public void setDetail(List<BookShelf> detail) {
		this.detail = detail;
	}

	public class BookShelf {
		String book_site;
		String book_num;
		String book_status;

		public String getBook_site() {
			return book_site;
		}

		public void setBook_site(String book_site) {
			this.book_site = book_site;
		}

		public String getBook_num() {
			return book_num;
		}

		public void setBook_num(String book_num) {
			this.book_num = book_num;
		}

		public String getBook_status() {
			return book_status;
		}

		public void setBook_status(String book_status) {
			this.book_status = book_status;
		}

	}
}
