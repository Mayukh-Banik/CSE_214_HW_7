//Mayukh Banik CSE 214 R03 HW 7 114489797 mayukh.banik@stonybroo.edu
import java.util.*;
public class City implements Comparable
{
    private boolean visited;
    private HashMap<String, Integer> neighbors;
    private String name;

    /**
     * default constructor
     */
    public City()
    {
        neighbors = null;
        name = null;
        visited = false;
    }

    /**
     * allows for comparison of city types
     * @param o object of type city
     * @return their string relative value due to alphabetical order
     */
    public int compareTo(Object o)
    {
        return getName().compareTo(((City) o).getName());
    }

    /**
     * sets the name of the city
     * @param name of the city
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * returns the city name
     * @return the name of the city
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * returns the hashmap of the neighbors
     * @return hashmap of neighbors
     */
    public HashMap<String, Integer> getNeighbors()
    {
        return neighbors;
    }

    /**
     * sets the neighbors
     * @param neighbors to be set to
     */
    public void setNeighbors(HashMap<String, Integer> neighbors)
    {
        this.neighbors = neighbors;
    }

    /**
     * sets if the city has been traversed
     * @param visited if it has been visited
     */
    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }

    /**
     * gets whether it was visited or not
     * @return visiting status
     */
    public boolean getVisited()
    {
        return visited;
    }
}