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

    private int numThreads = 10;
    private int numInvocationsPerThread = 100;


    /**
     * Instantiates a new multi threaded invoker.
     * 
     * Using this default constructor each test method is invoked 100 times
     * by 10 thread.
     */
    public MultiThreadedMultiInvoker()
    {
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

                for ( int threadNum = 0; threadNum < numThreads; threadNum++ )
                {
                    Runnable r = new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                                for ( int invocationNum = 0; invocationNum < numInvocationsPerThread; invocationNum++ )
                                {
                                    base.evaluate();
                                }
                            }
                            catch ( Throwable t )
                            {
                                throwables.add( t );
                            }
                        }
                    };

                    Thread t = new Thread( r );
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
