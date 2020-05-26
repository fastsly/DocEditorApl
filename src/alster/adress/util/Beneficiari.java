package alster.adress.util;

public class Beneficiari {
	private String name;
	private Boolean week1;
	private Boolean week2;
	private Boolean week3;
	private Boolean week4;

	public Beneficiari(String name, Boolean week1, Boolean week2, Boolean week3, Boolean week4) {
		this.setName(name);
		this.setWeek1(week1);
		this.setWeek2(week2);
		this.setWeek3(week3);
		this.setWeek4(week4);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isWeek1() {
		return week1;
	}

	public void setWeek1(Boolean week1) {
		this.week1 = week1;
	}

	public Boolean isWeek2() {
		return week2;
	}

	public void setWeek2(Boolean week2) {
		this.week2 = week2;
	}

	public Boolean isWeek3() {
		return week3;
	}

	public void setWeek3(Boolean week3) {
		this.week3 = week3;
	}

	public Boolean isWeek4() {
		return week4;
	}

	public void setWeek4(Boolean week4) {
		this.week4 = week4;
	}
}