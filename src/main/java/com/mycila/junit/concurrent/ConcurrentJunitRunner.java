/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.junit.concurrent;

import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentJunitRunner extends BlockJUnit4ClassRunner {
    public ConcurrentJunitRunner(final Class<?> klass) throws InitializationError {
        super(klass);
        int nThreads = 0;
        if (klass.isAnnotationPresent(Concurrency.class))
            nThreads = Math.max(0, klass.getAnnotation(Concurrency.class).value());
        if (nThreads == 0)
            nThreads = new TestClass(klass).getAnnotatedMethods(Test.class).size();
        if (nThreads == 0)
            nThreads = Runtime.getRuntime().availableProcessors();
        setScheduler(new ConcurrentRunnerScheduler(
                klass.getSimpleName(),
                Math.min(Runtime.getRuntime().availableProcessors(), nThreads),
                nThreads));
    }
}