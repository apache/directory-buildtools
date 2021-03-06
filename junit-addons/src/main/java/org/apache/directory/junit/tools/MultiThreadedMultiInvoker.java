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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public static final boolean THREADSAFE = true;
    public static final boolean NOT_THREADSAFE = false;
    private static ExecutorService pool = Executors.newCachedThreadPool();
    private int numThreads;
    private int numInvocationsPerThread;
    private boolean trace;


    /**
     * Instantiates a new multi threaded invoker.
     * 
     * The number of threads and invocations per thread are derived from 
     * system properties 'threads' and 'invocations'.
     * 
     * @param threadSafe whether the tested class is thread safe
     */
    public MultiThreadedMultiInvoker( boolean threadSafe )
    {
        this.numThreads = threadSafe ? getSystemIntProperty( "mtmi.threads", 1 ) : 1;
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
                final List<Throwable> throwables = Collections.synchronizedList( new ArrayList<Throwable>() );

                final List<Future<Void>> futures = new ArrayList<Future<Void>>();
                for ( int threadNum = 0; threadNum < count; threadNum++ )
                {
                    Callable<Void> c = new Callable<Void>()
                    {
                        public Void call() throws Exception
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
                            return null;
                        }
                    };

                    Future<Void> future = pool.submit( c );
                    futures.add( future );
                }

                for ( Future<Void> future : futures )
                {
                    future.get();
                }
                if ( !throwables.isEmpty() )
                {
                    throw throwables.get( 0 );
                }
            }
        };
    }

}
