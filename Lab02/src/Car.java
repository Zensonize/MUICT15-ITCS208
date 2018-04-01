//# COPYRIGHT KrittametK

public class Car {
	private double gas;
	private double eff;
	
	public Car(double eff) {
		gas = 0;
		this.eff = eff;
	}
	
	public void addGas(double gas) {
		this.gas += gas;
	}
	
	public void drive(double dist) {
		gas -= fuel_used(eff,dist);
	}
	
	public double getGasInTank() {
		return gas;
	}
	
	private static double fuel_used(double eff,double dist) {
		return dist/eff;
	}
	
}
