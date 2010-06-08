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


import java.util.Arrays;
import java.util.List;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;


/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentSuite extends Suite
{
    public ConcurrentSuite( Class<?> klass ) throws InitializationError
    {
        super( klass, new AllDefaultPossibilitiesBuilder( true )
        {
            @Override
            public Runner runnerForClass( Class<?> testClass ) throws Throwable
            {
                List<RunnerBuilder> builders = Arrays.asList( new RunnerBuilder()
                {
                    @Override
                    public Runner runnerForClass( Class<?> testClass ) throws Throwable
                    {
                        Concurrent annotation = testClass.getAnnotation( Concurrent.class );
                        if ( annotation != null )
                            return new ConcurrentJunitRunner( testClass );
                        return null;
                    }
                }, ignoredBuilder(), annotatedBuilder(), suiteMethodBuilder(), junit3Builder(), junit4Builder() );
                for ( RunnerBuilder each : builders )
                {
                    Runner runner = each.safeRunnerForClass( testClass );
                    if ( runner != null )
                        return runner;
                }
                return null;
            }
        } );
    }
}
