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

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentRule implements MethodRule {
    public Statement apply(final Statement statement, final FrameworkMethod frameworkMethod, final Object o) {
        return new Statement() {
            public void evaluate() throws Throwable {
                Concurrency concurrency = frameworkMethod.getAnnotation(Concurrency.class);
                if (concurrency == null)
                    statement.evaluate();
                else {
                    int nThreads = Math.max(0, concurrency.value());
                    if (nThreads == 0)
                        nThreads = Runtime.getRuntime().availableProcessors();
                    ConcurrentRunnerScheduler scheduler = new ConcurrentRunnerScheduler(
                            o.getClass().getSimpleName() + "." + frameworkMethod.getName(),
                            nThreads, nThreads);
                    final CountDownLatch go = new CountDownLatch(1);
                    Runnable runnable = new Runnable() {
                        public void run() {
                            try {
                                go.await();
                                statement.evaluate();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            } catch (Throwable throwable) {
                                throw ConcurrentException.wrap(throwable);
                            }
                        }
                    };
                    for (int i = 0; i < nThreads; i++)
                        scheduler.schedule(runnable);
                    go.countDown();
                    try {
                        scheduler.finished();
                    } catch (ConcurrentException e) {
                        throw e.unwrap();
                    }
                }
            }
        };
    }
}
