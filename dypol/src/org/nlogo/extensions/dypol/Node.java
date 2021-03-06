/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nlogo.extensions.dypol;

import java.util.HashMap;

/**
 *
 * @author Simone Gabbriellini
 */
public class Node {
    private int who;
    private double xcor;
    private double ycor;
    private double interactions;
    private HashMap<Double, Double[]> contactedPeople = new HashMap<Double, Double[]>();
    private Double[] allTimeContactedPeople = new Double[Dypol.size];
    
    private HashMap<Double, Double[]> issuesChosen = new HashMap<Double, Double[]>();
    private HashMap<Double, Double[]> issuesInterest = new HashMap<Double, Double[]>();
    
    private HashMap<Double, Double[]> euclideanDistance = new HashMap<Double, Double[]>();
    private HashMap<Double, Double[]> normEuclideanDistance = new HashMap<Double, Double[]>();
    private HashMap<Double, Double[]> socDistanceProb = new HashMap<Double, Double[]>();

    /**
     * @return the who
     */
    public int getWho() {
        return who;
    }

    /**
     * @param who the who to set
     */
    public void setWho(int who) {
        this.who = who;
    }

    /**
     * @return the xcor
     */
    public double getXcor() {
        return xcor;
    }

    /**
     * @param xcor the xcor to set
     */
    public void setXcor(double xcor) {
        this.xcor = xcor;
    }

    /**
     * @return the ycor
     */
    public double getYcor() {
        return ycor;
    }

    /**
     * @param ycor the ycor to set
     */
    public void setYcor(double ycor) {
        this.ycor = ycor;
    }

    /**
     * @return the contactedPeople
     */
    public Double[] getContactedPeople(Double ticks) {
        return contactedPeople.get(ticks);
    }

    /**
     * @param contactedPeople the contactedPeople to set
     */
    public void setContactedPeople(Double ticks, Double[] turtles) {
        this.contactedPeople.put(ticks, turtles);
    }

    /**
     * @return the socDistanceProb
     */
    public Double[] getSocDistanceProb(Double ticks) {
        return socDistanceProb.get(ticks);
    }

    /**
     * @param socDistanceProb the socDistanceProb to set
     */
    public void setSocDistanceProb(Double ticks, Double[] turtles) {
        this.socDistanceProb.put(ticks, turtles);
    }

    /**
     * @return the issueChosen
     */
    public Double[] getIssuesChosen(Double ticks) {
        return issuesChosen.get(ticks);
    }

    /**
     * @param issueChosen the issueChosen to set
     */
    public void setIssuesChosen(Double ticks, Double[] turtles) {
        this.issuesChosen.put(ticks, turtles);
    }

    /**
     * @return the euclideanDistance
     */
    public Double[] getEuclideanDistance(Double ticks) {
        return euclideanDistance.get(ticks);
    }

    /**
     * @param euclideanDistance the euclideanDistance to set
     */
    public void setEuclideanDistance(Double ticks, Double[] turtles) {
        this.euclideanDistance.put(ticks, turtles);
    }

    /**
     * @return the normEuclideanDistance
     */
    public Double[] getNormEuclideanDistance(Double ticks) {
        return normEuclideanDistance.get(ticks);
    }

    /**
     * @param normEuclideanDistance the normEuclideanDistance to set
     */
    public void setNormEuclideanDistance(Double ticks, Double[] turtles) {
        this.normEuclideanDistance.put(ticks, turtles);
    }

    /**
     * @return the issuesInterest
     */
    public Double[] getIssuesInterest(Double ticks) {
        return issuesInterest.get(ticks);
    }

    /**
     * @param issuesInterest the issuesInterest to set
     */
    public void setIssuesInterest(Double ticks, Double[] turtles) {
        this.issuesInterest.put(ticks, turtles);
    }

    /**
     * @return the interactions
     */
    public double getInteractions() {
        return interactions;
    }

    /**
     * @param interactions the interactions to set
     */
    public void setInteractions() {
        this.interactions++;
    }
    
    public void setInitializeInteractions(){
        this.interactions = 0;
    }

    /**
     * @return the allTimeContactedPeople
     */
    public Double[] getAllTimeContactedPeople() {
        return allTimeContactedPeople;
    }

    /**
     * @param allTimeContactedPeople the allTimeContactedPeople to set
     */
    public void setAllTimeContactedPeople(int pos) {
        this.allTimeContactedPeople[pos]++;
    }

    public void setInitializeAllTimeContactedPeople(Double[] turtles) {
        this.allTimeContactedPeople = turtles;
    }
    
    
}
