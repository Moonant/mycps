package net.bingyan.hustpass.module.lib;

import java.util.List;

public class BookListBean {
	String status;
	List<Book> result;
	int nextpage;
	int prevpage;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Book> getResult() {
		return result;
	}

	public void setResult(List<Book> result) {
		this.result = result;
	}

	public int getNextpage() {
		return nextpage;
	}

	public void setNextpage(int nextpage) {
		this.nextpage = nextpage;
	}

	public int getPrevpage() {
		return prevpage;
	}

	public void setPrevpage(int prevpage) {
		this.prevpage = prevpage;
	}

	public class Book {
		String title;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		String id;

	}
}
