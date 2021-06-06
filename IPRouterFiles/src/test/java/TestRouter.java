
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

/**
 * The test class TestRouter.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TestRouter
{

    //private TrieST<Integer> router;
    private IPRouter router;
    private IPRouter secondRouter; //For faster Internet, obviously
    /**
     * Default constructor for test class TestRouter
     */
    public TestRouter()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
       this.router = new IPRouter(8,4);
       this.secondRouter = new IPRouter(15, 3);
        try {
            router.loadRoutes("routes1.txt");
            secondRouter.loadRoutes("routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
    }

    /**
     * Handle an unroutable address
     */
    @Test
    public void testBadRoute()
    {
        IPAddress address = new IPAddress("73.73.0.1");
        assertEquals(-1, this.router.getRoute(address));
    }

    /**
     * Handle an address that only matches one prefix
     */
    @Test
    public void port2Test()
    {
        IPAddress address = new IPAddress("85.2.0.1");
        int res = this.router.getRoute(address);
        assertEquals(2, res);
    }

    /**
     * Handle an address that only matches multiple prefixes. Only the longest one counts
     */
    @Test
    public void port1Test()
    {
        IPAddress address = new IPAddress("85.85.85.85");
        int res = this.router.getRoute(address);
        assertEquals(1, res);
    }

    @Test
    public void deletePortTest(){
        IPAddress address = new IPAddress("85.85.85.85");
        this.router.deleteRule("85.85.0.0/15");
        int res = this.router.getRoute(address);
        assertEquals(2, res);
    }

    @Test
    public void secondRouterTest(){
    IPAddress address = new IPAddress("24.16.16.16");
    IPAddress address2 = new IPAddress("24.0.0.0");
    IPAddress address3 = new IPAddress("24.91.43.56");
    IPAddress address4 = new IPAddress("24.60.123.234");
    IPAddress address5 = new IPAddress("85.13.43.245");
    IPAddress address6 = new IPAddress("85.0.0.0");
    int res = this.secondRouter.getRoute(address);
    assertTrue(secondRouter.isCached(address));
    int res2 = this.secondRouter.getRoute(address2);
    int res3 = this.secondRouter.getRoute(address3);
    int res4 = this.secondRouter.getRoute(address4);
    int res5 = this.secondRouter.getRoute(address5);
    int res6 = this.secondRouter.getRoute(address6);
//    System.out.println("Address dot: " + address.toString());
//    System.out.println("Address dot: " + new IPAddress("24.16.0.0/13"));
//    System.out.println("Address dot: " + new IPAddress("24.30.0.0/17"));
//    System.out.println("Address dot: " + new IPAddress("24.0.0.0/12"));
    assertEquals(4, res);
    assertFalse(secondRouter.isCached(address2));
    assertTrue(secondRouter.isCached(address6));
    }

    @Test
    public void port3Test()
    {
        IPAddress address = new IPAddress("24.64.0.0/10");
        System.out.println("Address dot: " + address.toString());
        System.out.println("Address dot: " + new IPAddress("24.91.0.0/16"));
        IPAddress ad = new IPAddress("24.91.123.234");
        int res = this.secondRouter.getRoute(ad);
        assertEquals(7, res);

    }

    @Test (expected = IllegalArgumentException.class)
    public void port4Test() throws FileNotFoundException {
        IPRouter test = new IPRouter(4, 2);
        test.loadRoutes("routes2.txt");
        IPAddress address1 = new IPAddress("24.64.0.12");
        IPAddress address2 = new IPAddress("24.128.24.95");
        IPAddress address3 = new IPAddress("24.91.102.187");
        IPAddress address4 = new IPAddress("24.60.12.43");
        IPAddress address5 = new IPAddress("24.0.0.0");
        IPAddress address6 = new IPAddress("85.0.0.0/10");
        IPAddress address7 = new IPAddress("85.85.85.85");
        int res1 = test.getRoute(address1);
        int res2 = test.getRoute(address2);
        int res3 = test.getRoute(address3);
        int res4 = test.getRoute(address4);
        int res5 = test.getRoute(address5);
        int res6 = test.getRoute(address6);
        int res7 = test.getRoute(address7);

    }

    @Test (expected = IllegalArgumentException.class)
    public void port5Test() throws FileNotFoundException {
        IPRouter test = new IPRouter(15, 2);
        test.loadRoutes("routes2.txt");
        IPAddress address1 = new IPAddress("24.64.0.12");
        IPAddress address2 = new IPAddress("24.128.24.95");
        IPAddress address3 = new IPAddress("24.91.102.187");
        IPAddress address4 = new IPAddress("24.60.12.43");
        IPAddress address5 = new IPAddress("24.0.0.0");
        IPAddress address6 = new IPAddress("85.0.0.0/10");
        IPAddress address7 = new IPAddress("85.85.85.85");
        test.addRule("24.128.0.0/9", 6);
        int res1 = test.getRoute(address1);
        int res2 = test.getRoute(address2);
        int res3 = test.getRoute(address3);
        assertEquals(7, res3);
        int res4 = test.getRoute(address4);
        int res5 = test.getRoute(address5);
        int res6 = test.getRoute(address6);
        int res7 = test.getRoute(address7);

    }

    @Test (expected = IllegalArgumentException.class)
    public void port6Test() throws FileNotFoundException {
        IPRouter test = new IPRouter(15, 2);
        test.loadRoutes("routes2.txt");
        test.deleteRule("24.128.0.0/29");

    }

    @Test
    public void port7Test() throws FileNotFoundException {
        IPRouter test = new IPRouter(15, 2);
        test.loadRoutes("routes2.txt");
        IPAddress address1 = new IPAddress("24.64.0.12");
        IPAddress address2 = new IPAddress("24.128.24.95");
        IPAddress address3 = new IPAddress("24.91.102.187");
        IPAddress address4 = new IPAddress("24.60.12.43");
        IPAddress address5 = new IPAddress("24.0.0.0");
        IPAddress address6 = new IPAddress("85.0.0.0/10");
        IPAddress address7 = new IPAddress("85.85.85.85");
        test.deleteRule("24.128.0.0/9");
        int res1 = test.getRoute(address1);
        int res2 = test.getRoute(address2);
        int res3 = test.getRoute(address3);
        int res4 = test.getRoute(address4);
        int res5 = test.getRoute(address5);
        int res6 = test.getRoute(address6);
        int res7 = test.getRoute(address7);
        test.deleteRule("85.0.0.0/8");
    }

    @Test
    public void portDouble7Test() throws FileNotFoundException {
        IPRouter test = new IPRouter(15, 3);
        test.loadRoutes("routes2.txt");
        IPAddress address1 = new IPAddress("24.64.0.12");
        IPAddress address2 = new IPAddress("24.128.24.95");
        IPAddress address3 = new IPAddress("24.91.102.187");
        IPAddress address4 = new IPAddress("24.60.12.43");
        IPAddress address5 = new IPAddress("24.0.0.0");
        IPAddress address6 = new IPAddress("85.0.0.0/10");
        IPAddress address7 = new IPAddress("85.85.85.85");
        test.deleteRule("24.128.0.0/9");
        int res1 = test.getRoute(address1);
        int res2 = test.getRoute(address2);
        int res3 = test.getRoute(address3);
        int res12 = test.getRoute(address1);
        String[] st = test.dumpCache();
        assertEquals("24.64.0.12", st[0]);
        int res4 = test.getRoute(address4);
        int res5 = test.getRoute(address5);
        int res6 = test.getRoute(address6);
        int res7 = test.getRoute(address7);
    }

    @Test
    public void same3TrieTest(){
        IPRouter test = new IPRouter(15, 2);
        test.addRule("24.64.0.0/10", 2);
        test.addRule("24.91.0.0/16", 3);
        test.addRule("0.0.0.0/16", 4);
        assertEquals(2, test.getRoute(new IPAddress("24.91.0.0/14")));
        assertEquals(3, test.getRoute(new IPAddress("24.91.23.198/20")));
        test.deleteRule("24.91.0.0/16");
        assertEquals(3, test.getRoute(new IPAddress("24.91.23.198/20")));

//        assertEquals(4, test.getRoute(new IPAddress("0.0.0.0/20")));
    }

    @Test
    public void port8Test()
    {
        IPAddress address = new IPAddress("85.2.0.1");
        int res = this.secondRouter.getRoute(address);
        int resCopy = this.secondRouter.getRoute(address);
        int resCopy2 = this.secondRouter.getRoute(address);
        assertEquals(2, res);
        String[] st = secondRouter.dumpCache();
        assertTrue(st.length == 1);


        IPAddress address1 = new IPAddress("85.85.85.85");
        int res1 = this.secondRouter.getRoute(address1);
        assertEquals(1, res1);
        String[] st2 = secondRouter.dumpCache();
        assertEquals(st2[0], address1.toCIDR());
        assertTrue(st2.length == 2);

        IPAddress address2 = new IPAddress("24.98.0.0");
        int res2 = this.secondRouter.getRoute(address2);
        assertEquals(6, res2);
        String[] st3 = secondRouter.dumpCache();
        assertEquals(st3[0], address2.toCIDR());
        assertEquals(st3[1], address1.toCIDR());
        assertEquals(st3[2], address.toCIDR());
        assertTrue(st3.length == 3);


        IPAddress address3 = new IPAddress("24.60.0.0");
        int res3 = this.secondRouter.getRoute(address3);
        assertEquals(5, res3);
        String[] st4 = secondRouter.dumpCache();
        assertEquals(st4[0], address3.toCIDR());
        assertEquals(st4[1], address2.toCIDR());
        assertEquals(st4[2], address1.toCIDR());
        assertTrue(st4.length == 3);

        //makes the head go away
        IPAddress address4 = new IPAddress("24.128.0.0");
        int res4 = this.secondRouter.getRoute(address4);
        assertEquals(3, res4);
        String[] st5 = secondRouter.dumpCache();
        assertEquals(st5[0], address4.toCIDR());
        assertEquals(st5[1], address3.toCIDR());
        assertEquals(st5[2], address2.toCIDR());
        assertTrue(st5.length == 3);

        IPAddress address5 = new IPAddress("107.165.232.12");
        int res5 = this.secondRouter.getRoute(address5);
        assertEquals(-1, res5);
        String[] st6 = secondRouter.dumpCache();
        assertEquals(st6[0], address5.toCIDR());
        assertEquals(st6[1], address4.toCIDR());
        assertEquals(st6[2], address3.toCIDR());
        assertTrue(st6.length == 3);

        assertEquals(3, this.secondRouter.getRoute(address4));
        String[] st7 = secondRouter.dumpCache();
        assertEquals(st7[0], address4.toCIDR());
        assertEquals(st7[1], address5.toCIDR());
        assertEquals(st7[2], address3.toCIDR());
        assertTrue(st7.length == 3);


        assertEquals(5, this.secondRouter.getRoute(address3));
        String[] st8 = secondRouter.dumpCache();
        assertEquals(st8[0], address3.toCIDR());
        assertEquals(st8[1], address4.toCIDR());
        assertEquals(st8[2], address5.toCIDR());
        assertTrue(st8.length == 3);

        assertEquals(6, this.secondRouter.getRoute(address2));
        String[] st9 = secondRouter.dumpCache();
        assertEquals(st9[0], address2.toCIDR());
        assertEquals(st9[1], address3.toCIDR());
        assertEquals(st9[2], address4.toCIDR());
        assertTrue(st9.length == 3);

        assertEquals(1, this.secondRouter.getRoute(address1));
        String[] st10 = secondRouter.dumpCache();
        assertEquals(st10[0], address1.toCIDR());
        assertEquals(st10[1], address2.toCIDR());
        assertEquals(st10[2], address3.toCIDR());
        assertTrue(st10.length == 3);

        assertEquals(2, this.secondRouter.getRoute(address));
        String[] st11 = secondRouter.dumpCache();
        assertEquals(st11[0], address.toCIDR());
        assertEquals(st11[1], address1.toCIDR());
        assertEquals(st11[2], address2.toCIDR());
        assertTrue(st11.length == 3);

    }

    @Test
    public void portDouble8Test()
    {
        IPAddress address = new IPAddress("85.2.0.1");
        int res = this.secondRouter.getRoute(address);
        assertEquals(2, res);
        String[] st = secondRouter.dumpCache();
        assertTrue(st.length == 1);


        IPAddress address1 = new IPAddress("85.85.85.85");
        int res1 = this.secondRouter.getRoute(address1);
        assertEquals(1, res1);
        String[] st2 = secondRouter.dumpCache();
        assertEquals(st2[0], address1.toCIDR());
        assertTrue(st2.length == 2);

        IPAddress address2 = new IPAddress("24.98.0.0");
        int res2 = this.secondRouter.getRoute(address2);
        int resCopy = this.secondRouter.getRoute(address2);
        int resCopy2 = this.secondRouter.getRoute(address2);
        assertEquals(6, res2);
        String[] st3 = secondRouter.dumpCache();
        assertEquals(st3[0], address2.toCIDR());
        assertEquals(st3[1], address1.toCIDR());
        assertEquals(st3[2], address.toCIDR());
        assertTrue(st3.length == 3);


        IPAddress address3 = new IPAddress("24.60.0.0");
        int res3 = this.secondRouter.getRoute(address3);
        assertEquals(5, res3);
        String[] st4 = secondRouter.dumpCache();
        assertEquals(st4[0], address3.toCIDR());
        assertEquals(st4[1], address2.toCIDR());
        assertEquals(st4[2], address1.toCIDR());
        assertTrue(st4.length == 3);

        //makes the head go away
        IPAddress address4 = new IPAddress("24.128.0.0");
        int res4 = this.secondRouter.getRoute(address4);
        assertEquals(3, res4);
        String[] st5 = secondRouter.dumpCache();
        assertEquals(st5[0], address4.toCIDR());
        assertEquals(st5[1], address3.toCIDR());
        assertEquals(st5[2], address2.toCIDR());
        assertTrue(st5.length == 3);

        IPAddress address5 = new IPAddress("107.165.232.12");
        int res5 = this.secondRouter.getRoute(address5);
        assertEquals(-1, res5);
        String[] st6 = secondRouter.dumpCache();
        assertEquals(st6[0], address5.toCIDR());
        assertEquals(st6[1], address4.toCIDR());
        assertEquals(st6[2], address3.toCIDR());
        assertTrue(st6.length == 3);

        assertEquals(3, this.secondRouter.getRoute(address4));
        String[] st7 = secondRouter.dumpCache();
        assertEquals(st7[0], address4.toCIDR());
        assertEquals(st7[1], address5.toCIDR());
        assertEquals(st7[2], address3.toCIDR());
        assertTrue(st7.length == 3);


        assertEquals(5, this.secondRouter.getRoute(address3));
        String[] st8 = secondRouter.dumpCache();
        assertEquals(st8[0], address3.toCIDR());
        assertEquals(st8[1], address4.toCIDR());
        assertEquals(st8[2], address5.toCIDR());
        assertTrue(st8.length == 3);

        assertEquals(6, this.secondRouter.getRoute(address2));
        String[] st9 = secondRouter.dumpCache();
        assertEquals(st9[0], address2.toCIDR());
        assertEquals(st9[1], address3.toCIDR());
        assertEquals(st9[2], address4.toCIDR());
        assertTrue(st9.length == 3);

        assertEquals(1, this.secondRouter.getRoute(address1));
        String[] st10 = secondRouter.dumpCache();
        assertEquals(st10[0], address1.toCIDR());
        assertEquals(st10[1], address2.toCIDR());
        assertEquals(st10[2], address3.toCIDR());
        assertTrue(st10.length == 3);

        assertEquals(2, this.secondRouter.getRoute(address));
        String[] st11 = secondRouter.dumpCache();
        assertEquals(st11[0], address.toCIDR());
        assertEquals(st11[1], address1.toCIDR());
        assertEquals(st11[2], address2.toCIDR());
        assertTrue(st11.length == 3);

    }


    @Test
    public void same2TrieTest(){
        IPRouter test = new IPRouter(15, 1);
        test.addRule("24.64.0.0/10", 2);
        test.addRule("24.91.0.0/16", 3);
        test.addRule("0.0.0.0/16", 4);
        assertEquals(3, test.getRoute(new IPAddress("24.91.23.198/20")));
        test.getRoute(new IPAddress("24.91.23.198/20"));
        assertEquals(2, test.getRoute(new IPAddress("24.91.0.0/14")));
        test.deleteRule("24.91.0.0/16");
        assertFalse(test.isCached(new IPAddress("24.91.0.0/16")));
        assertEquals(2, test.getRoute(new IPAddress("24.91.23.198/20")));

    }

    @Test
    public void zeroCacheSize() throws FileNotFoundException {
        IPRouter test = new IPRouter(15, 0);
        test.addRule("24.64.0.0/10", 2);
        IPAddress address = new IPAddress("24.91.0.0/14");
        int res = test.getRoute(address);
        assertEquals(2, res);
        assertFalse(test.isCached(address));
    }

    @Test
    public void zeroCacheSize1() throws FileNotFoundException {
        IPRouter test = new IPRouter(15, 3);
        test.addRule("24.64.0.0/10", 4);
        test.addRule("24.91.0.0/16", 7);
        test.addRule("24.98.0.0/15", 6);

        IPAddress address = new IPAddress("24.96.0.0/16");
        int res = test.getRoute(address);
        assertEquals(4, res);
    }


    @Test (expected = IllegalArgumentException.class)
    public void delete3TrieTest(){
        IPRouter test = new IPRouter(15, 2);
        test.addRule("24.64.0.0/10", 2);
        test.addRule("24.91.0.0/16", 3);
        test.deleteRule("24.91.0.0/16");
        test.deleteRule("24.91.0.0/16");

//        assertEquals(4, test.getRoute(new IPAddress("0.0.0.0/20")));
    }

    @Test (expected = IllegalArgumentException.class)
    public void delete4TrieTest(){
        IPRouter test = new IPRouter(15, 2);
        test.addRule("24.64.0.0/10", 2);
        test.addRule("24.91.0.0/16", 3);
        test.deleteRule("24.91.0.0/14");
//        test.deleteRule("24.91.0.0/16");

//        assertEquals(4, test.getRoute(new IPAddress("0.0.0.0/20")));
    }

    @Test
    public void route0Test(){
        RouteCache routeCache = new RouteCache(0);
        assertNull(routeCache.lookupRoute(new IPAddress("24.64.0.0/10")));
    }

    @Test
    public void errLineTest(){
        IPAddress ip = new IPAddress("107.165.232.12");
        int x = secondRouter.getRoute(ip);
    }

    @Test
    public void sameTrieTest(){
        IPRouter test = new IPRouter(15, 2);
        test.addRule("24.64.0.0/10", 2);
        test.addRule("24.91.0.0/16", 3);
        assertEquals(3, test.getRoute(new IPAddress("24.91.109.12")));


//        assertEquals(4, test.getRoute(new IPAddress("0.0.0.0/20")));
    }


















    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
}
