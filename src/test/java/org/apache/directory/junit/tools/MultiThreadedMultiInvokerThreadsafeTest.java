package org.apache.directory.junit.tools;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;


public class MultiThreadedMultiInvokerThreadsafeTest
{
    private static final int NUM_THREADS = 100;
    private static final int NUM_INVOCATIONS = 10000;
    private static final int NUM_METHODS = 10;

    @Rule
    public MultiThreadedMultiInvoker i = new MultiThreadedMultiInvoker( NUM_THREADS, NUM_INVOCATIONS );

    private static AtomicLong counter;
    private static long startTime;


    @BeforeClass
    public static void initCounter()
    {
        counter = new AtomicLong( 0L );
    }


    @BeforeClass
    public static void initStartTime()
    {
        startTime = System.currentTimeMillis();
    }


    @AfterClass
    public static void checkCounter()
    {
        assertEquals( 1L * NUM_THREADS * NUM_INVOCATIONS * NUM_METHODS, counter.get() );
    }


    @AfterClass
    public static void checkRuntime()
    {
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        if ( runTime > 10000 )
        {
            fail( "Test shut run in less than 10 seconds." );
        }
    }


    @Test
    public void test0()
    {
        counter.incrementAndGet();
    }


    @Test
    public void test1()
    {
        counter.incrementAndGet();
    }


    @Test
    public void test2()
    {
        counter.incrementAndGet();
    }


    @Test
    public void test3()
    {
        counter.incrementAndGet();
    }


    @Test
    public void test4()
    {
        counter.incrementAndGet();
    }


    @Test
    public void test5()
    {
        counter.incrementAndGet();
    }


    @Test
    public void test6()
    {
        counter.incrementAndGet();
    }


    @Test
    public void test7()
    {
        counter.incrementAndGet();
    }


    @Test
    public void test8()
    {
        counter.incrementAndGet();
    }


    @Test
    public void test9()
    {
        counter.incrementAndGet();
    }

}
