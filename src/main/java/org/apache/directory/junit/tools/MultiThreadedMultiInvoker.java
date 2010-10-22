/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.junit.tools;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;


/**
 * Rule that invokes each test method multiple times using multiple threads.
 * 
 * Just put the following two lines to your test.
 * 
 * <pre>
 *     &#064;RuleRule
 *     public MultiThreadedMultiInvoker i = new MultiThreadedMultiInvoker();
 * </pre>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class MultiThreadedMultiInvoker implements MethodRule
{
    private static AtomicInteger threadCounter = new AtomicInteger();
    private int numThreads;
    private int numInvocationsPerThread;
    private boolean trace;


    /**
     * Instantiates a new multi threaded invoker.
     * 
     * The number of threads and invocations per thread are derived from 
     * system properties 'threads' and 'invocations'.
     */
    public MultiThreadedMultiInvoker()
    {
        this.numThreads = getSystemIntProperty( "mtmi.threads", 1 );
        this.numInvocationsPerThread = getSystemIntProperty( "mtmi.invocations", 1 );
        this.trace = getSystemBoolProperty( "mtmi.trace", false );
    }


    private int getSystemIntProperty( String key, int def )
    {
        String property = System.getProperty( key, "" + def );
        int value = Integer.parseInt( property );
        return value;
    }


    private boolean getSystemBoolProperty( String key, boolean def )
    {
        String property = System.getProperty( key, "" + def );
        boolean value = Boolean.parseBoolean( property );
        return value;
    }


    /**
     * Instantiates a new multi threaded multi invoker.
     *
     * @param numThreads the number of threads
     * @param numInvocationsPerThread the number of method invocations per thread
     */
    public MultiThreadedMultiInvoker( int numThreads, int numInvocationsPerThread )
    {
        super();
        this.numThreads = numThreads;
        this.numInvocationsPerThread = numInvocationsPerThread;
        this.trace = false;
    }


    /**
     * Instantiates a new multi threaded multi invoker.
     *
     * @param numThreads the number of threads
     * @param numInvocationsPerThread the number of method invocations per thread
     * @param trace the trace flag
     */
    public MultiThreadedMultiInvoker( int numThreads, int numInvocationsPerThread, boolean trace )
    {
        super();
        this.numThreads = numThreads;
        this.numInvocationsPerThread = numInvocationsPerThread;
        this.trace = trace;
    }


    /**
     * {@inheritDoc}
     */
    public Statement apply( final Statement base, final FrameworkMethod method, final Object target )
    {
        final List<Throwable> throwables = Collections.synchronizedList( new ArrayList<Throwable>() );
        final List<Thread> threads = new ArrayList<Thread>();

        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                int count = numThreads;
                if ( method.getAnnotation( NoMultiThreadedInvocation.class ) != null )
                {
                    count = 1;
                }

                final long start = System.currentTimeMillis();
                for ( int threadNum = 0; threadNum < count; threadNum++ )
                {
                    Runnable r = new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                                for ( int invocationNum = 0; invocationNum < numInvocationsPerThread; invocationNum++ )
                                {
                                    if ( trace )
                                    {
                                        long t = System.currentTimeMillis() - start;
                                        System.out.println( t + " - " + method.getName() + " - "
                                            + Thread.currentThread().getName() + " - Invocation "
                                            + ( invocationNum + 1 ) + "/" + numInvocationsPerThread );
                                    }

                                    base.evaluate();
                                }
                            }
                            catch ( Throwable t )
                            {
                                if ( trace )
                                {
                                    t.printStackTrace();
                                }
                                throwables.add( t );
                            }
                        }
                    };

                    String name = MultiThreadedMultiInvoker.class.getSimpleName() + "-Thread-"
                        + threadCounter.incrementAndGet();
                    Thread t = new Thread( r, name );
                    threads.add( t );
                }

                for ( Thread thread : threads )
                {
                    thread.start();
                }

                for ( Thread thread : threads )
                {
                    thread.join();
                }

                if ( !throwables.isEmpty() )
                {
                    throw throwables.get( 0 );
                }
            }
        };
    }

}
