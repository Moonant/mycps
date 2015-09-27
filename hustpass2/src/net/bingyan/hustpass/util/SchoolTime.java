package net.bingyan.hustpass.util;




public class SchoolTime{
	public int week;
	public int day;
	public int term=HustUtils.getTerm();

	SchoolTime(int w,int d){
		week=w;
		day=d;
	}
	
	public SchoolTime() {
		if(HustUtils.getWeekNum()>21){
			week=22;
			day=0;
		} else {
		week = HustUtils.getWeekNum();
		day = HustUtils.getDayNum();
		}
	}

	public SchoolTime next() {
		day++;
		if (day == 7) {
			week++;
			day = 0;
		}
		if(week==23){
			if(term%2==0)
				term+=10-1;
			else
				term++;
			week=1;
		}
		return this;
	}

	public SchoolTime prev() {
		day--;
		if (day < 0) {
			week--;
			day = 6;
		}
		if(week==0){
			if(term%2==0)
				term--;
			else
				term-=10+1;
			week=22;
		}
		return this;
	}

	public SchoolTime move(int position) {
		if (position == 0) {
			this.prev();
		} else if (position == 2) {
			this.next();
		}
		return this;
	}

	public SchoolTime getMove(int position) {
		SchoolTime newtime = new SchoolTime(week,day);
		if (position == 0) {
			newtime.prev();
		} else if (position == 2) {
			newtime.next();
		}
		return newtime;
	}

}