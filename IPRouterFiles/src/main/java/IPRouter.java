import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * IPRouter simulates the decision process for an IP router dispatching packets according a
 * prefix trie of routing rules.
 * 
 * @author Van Kelly
 * @version 1.0
 */
public class IPRouter
{

    final int nPorts; 
    final int cacheSize;
    final BitVectorTrie<Integer> trie = new BitVectorTrie<Integer>();
    private final RouteCache routeCache;

    /** Router constructor
     * @param nPorts    the number of output ports, numbered 0 ... nPorts-1.  Pseudo-port -1 is 
     *                  always used for errors.
     * @param cacheSize the number of IP Addresses to be kept in a cache of the most recently routed 
     *                  UNIQUE IP Addresses
     */
    public IPRouter (int nPorts, int cacheSize) 
    {
        this.nPorts = nPorts;
        this.cacheSize = cacheSize;
        if (cacheSize < 0){
            throw new IllegalArgumentException();
        }
        this.routeCache = new RouteCache(this.cacheSize);
    }

    /**
     * Add a routing rule to the router. Each rule associates an IP Address prefix with an output port.
     * In case rules overlap, longest prefix wins.  If two rules specify exactly the same prefix, then
     * the second rule triggers an IllegalArgumentException.  The port must be in the permitted range
     * for this router, or an IllegalArgumentException will be triggered as well.
     * 
     * @param  prefix    an IP Address prefix in CIDR (dotted decimal) notation
     * @param  port
     * @return        true if rule is accepted. 
     */
    public void addRule(String prefix, int port)
    {
        if (port < 0 || port > (nPorts - 1) || prefix == null ){
            throw new IllegalArgumentException();
        } else {
            trie.put(new IPAddress(prefix), port);
        }
    	// finish this (1 or 2 lines)
    }

    public void deleteRule(String prefix)
    {
    	// finish this (1 or 2 lines)
        if (prefix == null){
            throw new IllegalArgumentException();
        }
        trie.delete(new IPAddress(prefix));

    }

    /**
     * Simulate routing a packet to its output port based on a binary IP Address.
     * If no rules apply to an address, route it to port -1 and log an error to System.err
     * 
     * @param  address    an IP Address object
     * @return  number of output port 
     */
    public int getRoute(IPAddress address) 
    {
        if (routeCache.lookupRoute(address) != null){

            Integer p = routeCache.lookupRoute(address);
            this.routeCache.updateCache(address, p);
            return p;
        }
        Integer port = trie.get(address);
        if (port == null){
            routeCache.updateCache(address, -1);
            System.err.println("This is an error!! No rules are here!! C'mon man!!");
            return -1;
        }

        if (port >=0 && port <= nPorts-1){
            this.routeCache.updateCache(address, port);
            return port;
        } else {
            System.err.println("This is an error!! No rules are here!! C'mon man!!");
            return -1;
        }
    }

    /**
     * Tell whether an IP Address is currently in the cache of most recently routed addresses
      * 
     * @param  address    an IP Address in dotted decimal notation
     * @return  number of output port 
     */
    boolean isCached(IPAddress address) 
    {
        Integer x = routeCache.lookupRoute(address);
        if (x != null){
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * For testing and debugging, return the contents of the LRU queue in most-recent-first order,
     * as an array of IP Addresses.  Return a zero length array if the cache is empty
     * 
     */
    String[] dumpCache()
    {
        String[] temp = routeCache.dumpQueue();
        return temp;
    }
    
    /**
     * For testing and debugging, load a routing table from a text file
     * 
     */
    public void loadRoutes(String filename) throws FileNotFoundException
    {
        Scanner sc = new Scanner(new File(filename));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("+")) {
                String[] pieces = line.substring(1).split(",");
                int port = Integer.parseInt(pieces[1]);
                this.addRule(pieces[0].trim(), port);
            }
        }
    }
}
