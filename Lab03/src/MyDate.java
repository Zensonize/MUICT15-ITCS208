
public class MyDate {

	int year;
	int month;
	int day;
	int objectNumber;
	static int objectCounter = 1;
	static String strMonths[] = {"January", "February", "March", "April", "May", "June", 
			"July", "August", "September", "October", "November", "December"};
	
	public static void count() {
		objectCounter++;
	}

	public MyDate() {
		year = 1900;
		month = 1;
		day = 1;
		objectNumber = objectCounter;
		count();
	}
	
	public MyDate(int aDay,int aMonth, int aYear) {
		year = aYear;
		month = aMonth;
		day = aDay;
		objectNumber = objectCounter;
		count();
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getObjectNumber() {
		return objectNumber;
	}
	
	public void setDate(int aDay,int aMonth, int aYear) {
		year = aYear;
		month = aMonth;
		day = aDay;
	}
	
	public String toString() {
		return day + " " + strMonths[month-1] + " " + year;
	}
	
	public MyDate nextDay() {
		if(day == checkMargin()) {
			if(month == 12) {
				year++;
				month = 1;
				day = 1;
			}
			else {
				month++;
				day = 1;
			}
		}
		else {
			day++;
		}
		return this;
	}
	
	public MyDate previousDay() {
		if(day == 1) {
			if(month == 1) {
				year--;
				month = 12;
				day = 31;
			}
			else {
				month--;
				day = checkMargin();
			}
		}
		else day--;
		return this;
	}
	
	public MyDate nextMonth() {
		if (month == 12) {
			year++;
			month = 1;
		}
		else month++;
		return this;
	}
	
	public MyDate previousMonth() {
		if (month == 1) {
			year--;
			month = 12;
		}
		else month--;
		return this;
	}
	
	public MyDate nextYear() {
		year++;
		return this;
	}
	
	public MyDate previousYear() {
		year--;
		if(day == 29 && month == 2 && isLeapYear(year) == false) day = 28;
		return this;
	}
	
	//CHECK THE MONTH
	private int checkMargin() {
		if(month == 2 && isLeapYear(year) == false) return 28;
		else if(month == 2) return 29;
		else if(month == 4 || month == 6 || month == 9 || month == 11) return 30;
		else return 31;
	}
	
	//CHECK THE YEAR
	public boolean isLeapYear(int year) {
		if(year%4 == 0) {
			if(year%100 == 0) {
				if(year%400 == 0) return true;
				else return false;
			}
			else return true;
		}

		else return false;
	}
}
