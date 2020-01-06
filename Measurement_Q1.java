package lsda3;

import java.io.Serializable;

public class Measurement_Q1 implements Serializable
{
	// Universal version identifier for a Serializable class.
	private static final long serialVersionUID = 1L;
	
	//Setting up class attributes
    private int time;
    private double temperature;

    //Setting up constructor for the Measurement_Q1 object
    public Measurement_Q1(int time, double temperature){
        this.time = time;
        this.temperature=temperature;
    }

    //Setting up the setters and getters for the attribute time
    public void setTime(int time){
        this.time=time;
    }

    public int getTime(){
        return time;        
    }

    //Setting up the setters and getters for the attribute temperature
    public void setTemperature(double temperature){
        this.temperature = temperature;
    }

    public double getTemperature(){
        return temperature;
    }
}
