import java.util.HashMap;
import java.util.Map;

/**
 * This is a bounded cache that maintains only the most recently accessed IP Addresses
 * and their routes.  Only the least recently accessed route will be purged from the
 * cache when the cache exceeds capacity.  There are 2 closely coupled data structures:
 *   -  a Map keyed to IP Address, used for quick lookup
 *   -  a Queue of the N most recently accessed IP Addresses
 * All operations must be O(1).  A big hint how to make that happen is contained
 * in the type signature of the Map on line 38.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RouteCache
{
    
    // Cache total capacity and current fill count.
    private final int capacity;
    private int nodeCount;
    
    // private class for nodes in a doubly-linked queue
    // used to keep most-recently-used data
    private class Node {
        private Node prev, next;
        private final IPAddress elem; 
        private final int route;

        Node(IPAddress elem, int route) {
            prev = next = null;
            this.elem = elem;
            this.route = route;
        }  
    }
    private Node head = null;
    private Node tail = null;
    private Map<IPAddress, Node> nodeMap; // the cache itself

    /**
     * Constructor for objects of class RouteCache
     */
    public RouteCache(int cacheCapacity)
    {
        if (cacheCapacity < 0){
            throw new IllegalArgumentException();
        }
        capacity = cacheCapacity;
        nodeCount = 0;
        nodeMap = new HashMap<>();
    }

    /**
     * Lookup the output port for an IP Address in the cache, adding it if not already there
     * 
     * @param  addr   a possibly cached IP Address
     * @return     the cached route for this address, or null if not found 
     */
    public Integer lookupRoute(IPAddress addr)
    {
        if (nodeMap.containsKey(addr)){
            return nodeMap.get(addr).route;
        } else {
            return null;
        }
     }

     
    /**
     * Update the cache each time an element's route is looked up.
     * Make sure the element and its route is in the Map.
     * Enqueue the element at the tail of the queue if it is not already in the queue.  
     * Otherwise, move it from its current position to the tail of the queue.  If the queue
     * was already at capacity, remove and return the element at the head of the queue.
     * 
     * @param  elem  an element to be added to the queue, which may already be in the queue. 
     *               If it is, don't add it redundantly, but move it to the back of the queue
     * @return       the expired least recently used element, if any, or null
     */
    public IPAddress updateCache(IPAddress elem, int route)
    {
        if (capacity < 1){
            return elem;
        }
        if (!nodeMap.containsKey(elem)) {
            Node node = new Node(elem, route);
            if (nodeCount == 0){
                this.tail = node;
                this.head = node;
                node.prev = null;
                node.next = null;
            } else {
                this.tail.prev = node;
                node.next = this.tail;
                this.tail = node;
            }
            nodeCount++;
            nodeMap.put(elem, node);

        } else {
            Node node = nodeMap.get(elem);
            if (node == this.head && nodeCount > 1){
                this.head = this.head.prev;
                this.head.next = null;
                this.tail.prev = node;
                node.next = this.tail;
                node.prev = null;
                this.tail = node;
            } else if (node != this.head && node != this.tail){
                node.prev.next = node.next;
                node.next.prev = node.prev;
                this.tail.prev = node;
                node.prev = null;
                node.next = this.tail;
                this.tail = node;
            }

        }
        if (nodeCount > capacity){
            IPAddress temp = this.head.elem;
            this.head = this.head.prev;
            this.head.next = null;
            nodeMap.remove(temp);
            nodeCount--;
            return temp;
        } else{
            return null;
        }
    }

    
    /**
     * For testing and debugging, return the contents of the LRU queue in most-recent-first order,
     * as an array of IP Addresses in CIDR format. Return a zero length array if the cache is empty
     * 
     */
    String[] dumpQueue()
    {
        if (nodeCount == 0){
            return new String[0];
        }
        String[] cidr = new String[nodeCount];
        Node temp = this.tail;
        for (int i = 0; i < nodeCount; i++){
            cidr[i] = temp.elem.toCIDR();
            temp = temp.next;
        }
        return cidr;
    }

    private void intoString(){
        Node temp = this.tail;
        System.out.println();
        for (int i = 0; i < nodeCount; i++){
            System.out.print(temp.elem.toCIDR() + " -> ");
            temp = temp.next;
        }
        System.out.println();
        System.out.println();
    }
    
    
}
