package org.nlogo.extensions.dypol;

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.nlogo.agent.AgentSet;
import org.nlogo.agent.World;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.agent.Turtle;
import org.nlogo.api.AgentException;

/**
 * This class reproduces the influence process postulated by the dynamic of
 * political polarization model.
 *
 * @author Simone Gabbriellini
 */
class Influence extends DefaultCommand {

    @Override
    public void perform(Argument[] argmnts, Context cntxt) throws ExtensionException, LogoException {

        // retrieve world
        World world = (World) cntxt.getAgent().world();
        // retrieve ticks now
        Double ticks = world.ticks();
        // influence constant
        double infl = Dypol.infl;
        // retrieve all the nodes in the network
        Collection<Node> nodes = Dypol.g.getVertices();
        // retrieve all the turtles in NetLogo
        AgentSet turtles = world.turtles();
        // population size
        int size = turtles.count();
        // ask nodes
        for (Node pi : nodes) {
            Double[] issuesInterest = pi.getIssuesInterest(ticks);
            double meanValue = 0;
            double counter = 0;
            for (double d : issuesInterest) {
                meanValue += Math.abs(d);
                counter++;
            }
            double issuesMean = meanValue / counter;
            
            Node[] contacts = sample(nodes, .005 * Math.pow(Math.round(issuesMean), 2), world);
            
            if (pi.getContactedPeople(ticks) == null) {
                Double[] contactedPeople = new Double[size];
                for (int i = 0; i < size; i++) {
                    contactedPeople[i] = 0.0;
                }
                pi.setContactedPeople(ticks, contactedPeople);
            }
            for (Node pj : contacts) {
                boolean nextBoolean = cntxt.getRNG().nextBoolean(pi.getSocDistanceProb(ticks)[pj.getWho()]);
                if (nextBoolean) {
                    pi.getContactedPeople(ticks)[pj.getWho()] = 1.0;
                } else {
                    pi.getContactedPeople(ticks)[pj.getWho()] = 0.0;
                }
            }
        }
        // ask nodes
        for (Node pi : nodes) {
            for (Node pj : nodes) {
                if (pi.getContactedPeople(ticks)[pj.getWho()] == 1) {
                    // add link to improve layout - only if it does not exist yet
                    // TODO : maybe adding weight may help the visualization?
                    checkForLink(pi, pj, world);
                    // update count of interactions for speed up
                    Dypol.interactions++;
                    pi.setInteractions();
                    pi.setAllTimeContactedPeople(pj.getWho());
                    pj.setInteractions();
                    pj.setAllTimeContactedPeople(pi.getWho());
                    //count the number of issues in which actors are in agreement
                    int count = 0;
                    for (int i = 0; i < Dypol.issues; i++) {
                        double piIssue = pi.getIssuesInterest(ticks)[i];
                        double pjIssue = pj.getIssuesInterest(ticks)[i];
                        if ((piIssue <= 0 && pjIssue <= 0) || (piIssue > 0 && pjIssue > 0)) {
                            count++;
                        }
                    }
                    // select the issue on which actors confront, by comparing all 
                    // the issues and selecting the one in which their joint interest (disregarding the sign) is higher.
                    double[] sum = new double[Dypol.issues];
                    for (int i = 0; i < Dypol.issues; i++) {
                        sum[i] = Math.abs(pi.getIssuesInterest(ticks)[i]) + Math.abs(pj.getIssuesInterest(ticks)[i]);
                    }
                    double maxValue = 0;
                    int max = 0;
                    for (int i = 0; i < Dypol.issues; i++) {
                        if (sum[i] > maxValue) {
                            maxValue = sum[i];
                            max = i;
                        }
                    }
                    // update most popular issue counter
                    Dypol.mostPopularIssue[max] += 1;
                    // information on choosed issues
                    if (pi.getIssuesChosen(ticks) == null) {
                        Double[] issuesChosen = new Double[Dypol.issues];
                        for (int i = 0; i < Dypol.issues; i++) {
                            issuesChosen[i] = 0.0;
                        }
                        pi.setIssuesChosen(ticks, issuesChosen);
                    }
                    if (pj.getIssuesChosen(ticks) == null) {
                        Double[] issuesChosen = new Double[Dypol.issues];
                        for (int i = 0; i < Dypol.issues; i++) {
                            issuesChosen[i] = 0.0;
                        }
                        pj.setIssuesChosen(ticks, issuesChosen);
                    }
                    pi.getIssuesChosen(ticks)[max] += 1;
                    pj.getIssuesChosen(ticks)[max] += 1;

                    double dpi = (Math.abs(pi.getIssuesInterest(ticks)[max] - pj.getIssuesInterest(ticks)[max]) / Math.abs(pi.getIssuesInterest(ticks)[max])) * infl;
                    double dpj = (Math.abs(pi.getIssuesInterest(ticks)[max] - pj.getIssuesInterest(ticks)[max]) / Math.abs(pj.getIssuesInterest(ticks)[max])) * infl;

                    if (pi.getIssuesInterest(ticks)[max] > 0 && pj.getIssuesInterest(ticks)[max] > 0) {

                        if (pi.getIssuesInterest(ticks + 1) == null) {
                            Double[] issuesInterest = new Double[Dypol.issues];
                            for (int i = 0; i < Dypol.issues; i++) {
                                issuesInterest[i] = 0.0;
                            }
                            pi.setIssuesInterest(ticks + 1, issuesInterest);
                        }
                        pi.getIssuesInterest(ticks + 1)[max] += dpi;

                        if (pj.getIssuesInterest(ticks + 1) == null) {
                            Double[] issuesInterest = new Double[Dypol.issues];
                            for (int i = 0; i < Dypol.issues; i++) {
                                issuesInterest[i] = 0.0;
                            }
                            pj.setIssuesInterest(ticks + 1, issuesInterest);
                        }
                        pj.getIssuesInterest(ticks + 1)[max] += dpj;
                    }

                    if (pi.getIssuesInterest(ticks)[max] < 0 && pj.getIssuesInterest(ticks)[max] < 0) {

                        if (pi.getIssuesInterest(ticks + 1) == null) {
                            Double[] issuesInterest = new Double[Dypol.issues];
                            for (int i = 0; i < Dypol.issues; i++) {
                                issuesInterest[i] = 0.0;
                            }
                            pi.setIssuesInterest(ticks + 1, issuesInterest);
                        }
                        pi.getIssuesInterest(ticks + 1)[max] -= dpi;

                        if (pj.getIssuesInterest(ticks + 1) == null) {
                            Double[] issuesInterest = new Double[Dypol.issues];
                            for (int i = 0; i < Dypol.issues; i++) {
                                issuesInterest[i] = 0.0;
                            }
                            pj.setIssuesInterest(ticks + 1, issuesInterest);
                        }
                        pj.getIssuesInterest(ticks + 1)[max] -= dpj;
                    }

                    dpi = (Math.abs(pi.getIssuesInterest(ticks)[max] + pj.getIssuesInterest(ticks)[max]) / Math.abs(pi.getIssuesInterest(ticks)[max])) * infl;
                    dpj = (Math.abs(pi.getIssuesInterest(ticks)[max] + pj.getIssuesInterest(ticks)[max]) / Math.abs(pj.getIssuesInterest(ticks)[max])) * infl;

                    if (pi.getIssuesInterest(ticks)[max] > 0 && pj.getIssuesInterest(ticks)[max] < 0) {
                        if (count > 1) {

                            if (pi.getIssuesInterest(ticks + 1) == null) {
                                Double[] issuesInterest = new Double[Dypol.issues];
                                for (int i = 0; i < Dypol.issues; i++) {
                                    issuesInterest[i] = 0.0;
                                }
                                pi.setIssuesInterest(ticks + 1, issuesInterest);
                            }
                            pi.getIssuesInterest(ticks + 1)[max] -= dpi;

                            if (pj.getIssuesInterest(ticks + 1) == null) {
                                Double[] issuesInterest = new Double[Dypol.issues];
                                for (int i = 0; i < Dypol.issues; i++) {
                                    issuesInterest[i] = 0.0;
                                }
                                pj.setIssuesInterest(ticks + 1, issuesInterest);
                            }
                            pj.getIssuesInterest(ticks + 1)[max] += dpj;

                        } else {

                            if (pi.getIssuesInterest(ticks + 1) == null) {
                                Double[] issuesInterest = new Double[Dypol.issues];
                                for (int i = 0; i < Dypol.issues; i++) {
                                    issuesInterest[i] = 0.0;
                                }
                                pi.setIssuesInterest(ticks + 1, issuesInterest);
                            }
                            pi.getIssuesInterest(ticks + 1)[max] += dpi;

                            if (pj.getIssuesInterest(ticks + 1) == null) {
                                Double[] issuesInterest = new Double[Dypol.issues];
                                for (int i = 0; i < Dypol.issues; i++) {
                                    issuesInterest[i] = 0.0;
                                }
                                pj.setIssuesInterest(ticks + 1, issuesInterest);
                            }
                            pj.getIssuesInterest(ticks + 1)[max] -= dpj;
                        }
                    }

                    if (pi.getIssuesInterest(ticks)[max] < 0 && pj.getIssuesInterest(ticks)[max] > 0) {
                        if (count > 1) {

                            if (pi.getIssuesInterest(ticks + 1) == null) {
                                Double[] issuesInterest = new Double[Dypol.issues];
                                for (int i = 0; i < Dypol.issues; i++) {
                                    issuesInterest[i] = 0.0;
                                }
                                pi.setIssuesInterest(ticks + 1, issuesInterest);
                            }
                            pi.getIssuesInterest(ticks + 1)[max] += dpi;

                            if (pj.getIssuesInterest(ticks + 1) == null) {
                                Double[] issuesInterest = new Double[Dypol.issues];
                                for (int i = 0; i < Dypol.issues; i++) {
                                    issuesInterest[i] = 0.0;
                                }
                                pj.setIssuesInterest(ticks + 1, issuesInterest);
                            }
                            pj.getIssuesInterest(ticks + 1)[max] -= dpj;

                        } else {

                            if (pi.getIssuesInterest(ticks + 1) == null) {
                                Double[] issuesInterest = new Double[Dypol.issues];
                                for (int i = 0; i < Dypol.issues; i++) {
                                    issuesInterest[i] = 0.0;
                                }
                                pi.setIssuesInterest(ticks + 1, issuesInterest);
                            }
                            pi.getIssuesInterest(ticks + 1)[max] -= dpi;

                            if (pj.getIssuesInterest(ticks + 1) == null) {
                                Double[] issuesInterest = new Double[Dypol.issues];
                                for (int i = 0; i < Dypol.issues; i++) {
                                    issuesInterest[i] = 0.0;
                                }
                                pj.setIssuesInterest(ticks + 1, issuesInterest);
                            }
                            pj.getIssuesInterest(ticks + 1)[max] += dpj;
                        }
                    }
                    // update soc.distance prob for people who get in touch
                    double[] squaredDifferences = new double[Dypol.issues];
                    if (pi.getWho() != pj.getWho()) {
                        for (int i = 0; i < Dypol.issues; i++) {
                            squaredDifferences[i] = Math.pow(pi.getIssuesInterest(ticks)[i] - pj.getIssuesInterest(ticks)[i], 2);
                        }
                        double sumSquaredDiff = 0;
                        for (double d : squaredDifferences) {
                            sumSquaredDiff += d;
                        }
                        sumSquaredDiff = Math.sqrt(sumSquaredDiff);
                        pi.getEuclideanDistance(ticks)[pj.getWho()] = sumSquaredDiff;
                        pj.getEuclideanDistance(ticks)[pi.getWho()] = sumSquaredDiff;
                    }

                    double maxEuclDist = 0;
                    for (Node n : nodes) {
                        Double[] euclideanDistance = n.getEuclideanDistance(ticks);
                        for (double d : euclideanDistance) {
                            if (d > maxEuclDist) {
                                maxEuclDist = d;
                            }
                        }
                    }
                    pi.getNormEuclideanDistance(ticks)[pj.getWho()] = pi.getEuclideanDistance(ticks)[pj.getWho()] / maxEuclDist;
                    pj.getNormEuclideanDistance(ticks)[pi.getWho()] = pj.getEuclideanDistance(ticks)[pi.getWho()] / maxEuclDist;
                    pi.getSocDistanceProb(ticks)[pj.getWho()] = 1 - pi.getNormEuclideanDistance(ticks)[pj.getWho()];
                    pj.getSocDistanceProb(ticks)[pi.getWho()] = 1 - pj.getNormEuclideanDistance(ticks)[pi.getWho()];
                }
                ////
            }
            if (pi.getIssuesInterest(ticks + 1) == null) {
                pi.setIssuesInterest(ticks + 1, pi.getIssuesInterest(ticks));
            }
        }
        // END INFLUENCE

        // find most popular issue
        double maxValue = 0;
        int mostPopular = 0;
        for (int i = 0; i < Dypol.issues; i++) {
            if (Dypol.mostPopularIssue[i] > maxValue) {
                maxValue = Dypol.mostPopularIssue[i];
                mostPopular = i;
            }
        }
        //  
        for (Node n : nodes) {
            try {
                // set turtle's color according to sign of most popular issue
                if (n.getIssuesInterest(ticks)[mostPopular] < 0) {
                    world.getTurtle(n.getWho()).setVariable(Dypol.variables.get("COLOR"), 8.9);
                } else {
                    world.getTurtle(n.getWho()).setVariable(Dypol.variables.get("COLOR"), 123.0);
                }
            } catch (AgentException ex) {
                throw new ExtensionException(ex);
            }
            //
            for (int i = 0; i < Dypol.issues; i++) {
                double newValue = n.getIssuesInterest(ticks + 1)[i] + n.getIssuesInterest(ticks)[i];
                if (newValue > 100.0) {
                    newValue = 100.0;
                } else if (newValue < -100.0) {
                    newValue = -100.0;
                } else if (newValue > 0.0 && newValue < 1.0) {
                    newValue = 1.0;
                } else if (newValue > -1.0 && newValue < 0.0) {
                    newValue = -1.0;
                } else {
                    // do nothing
                }
                n.getIssuesInterest(ticks + 1)[i] = newValue;
            }

            // update euclidean and norm euclidean distances and soc distance probabilities hashmaps for next round
            n.setEuclideanDistance(ticks + 1, n.getEuclideanDistance(ticks));
            n.setNormEuclideanDistance(ticks + 1, n.getNormEuclideanDistance(ticks));
            n.setSocDistanceProb(ticks + 1, n.getSocDistanceProb(ticks));
        }
    }

    private Node[] sample(Collection<Node> nodes, double par, World world) {
        Node[] sample = new Node[(int) par];
        Object[] pool = nodes.toArray();
        for(int i=0;i<(int)par;i++){
            sample[i] = (Node)pool[world.mainRNG.nextInt(Dypol.size)];
        }
        return sample;
    }

    private void checkForLink(Node pi, Node pj, World world) throws ExtensionException {

        // TODO : FIX ME I AM WAY TOO SLOW...
        if (Dypol.g.getNeighbors(pi).contains(pj) || Dypol.g.getNeighbors(pj).contains(pi)) {
            // do nothing
        } else {
            double[] associationPi = new double[world.turtles().count()];
            Object[] vertices = Dypol.g.getVertices().toArray();
            for (int v = 0; v < vertices.length; v++) {
                Node vv = (Node) vertices[v];
                double observed = pi.getAllTimeContactedPeople()[vv.getWho()] + vv.getAllTimeContactedPeople()[pi.getWho()];
                double expected = (pi.getInteractions() * vv.getInteractions()) / Dypol.interactions;
                associationPi[v] = (observed - expected) / expected;
            }
            //
            if (associationPi[pj.getWho()] > 2 * flanagan.analysis.Stat.standardDeviation(associationPi)) {
                // retrieve turtles
                Turtle ti = world.getTurtle(pi.getWho());
                Turtle tj = world.getTurtle(pj.getWho());
                // add link in NetLogo world.
                world.linkManager.createLink(ti, tj, world.links());
                // add link to internal network representation
                LinkNodes link = new LinkNodes();
                link.setId(Dypol.linkId);
                link.setEnd1(pi);
                link.setEnd2(pj);
                Dypol.g.addEdge(link, pi, pj, EdgeType.UNDIRECTED);
                //edu.uci.ics.jung.algorithms.layout.ISOMLayout
                Layout<Node, LinkNodes> layout = new ISOMLayout<Node, LinkNodes>(Dypol.g);
                layout.setSize(new Dimension(32, 32));
                for (Node n : Dypol.g.getVertices()) {
                    Point2D coord = layout.transform(n);
                    try {
                        // retrieve the right turtle
                        Turtle t = world.getTurtle(n.getWho());
                        // set turtle's xcor
                        t.setVariable(Dypol.variables.get("XCOR"), coord.getX());
                        // set turtle's ycor
                        t.setVariable(Dypol.variables.get("YCOR"), coord.getY());
                    } catch (AgentException ex) {
                        // throw corresponding exception if it fails retrieving agents
                        throw new ExtensionException(ex);
                    }
                }
            }
        }
    }
}
