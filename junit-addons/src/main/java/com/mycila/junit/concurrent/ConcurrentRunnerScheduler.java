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

import org.junit.runners.model.RunnerScheduler;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ConcurrentRunnerScheduler implements RunnerScheduler {

    private final ExecutorService executorService;
    private final Queue<Future<Void>> tasks = new LinkedList<Future<Void>>();
    private final CompletionService<Void> completionService;

    public ConcurrentRunnerScheduler(String name, int nThreadsMin, int nThreadsMax) {
        executorService = new ThreadPoolExecutor(
                nThreadsMin, nThreadsMax,
                10L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new NamedThreadFactory(name),
                new ThreadPoolExecutor.CallerRunsPolicy());
        completionService = new ExecutorCompletionService<Void>(executorService);
    }

    public void schedule(Runnable childStatement) {
        tasks.offer(completionService.submit(childStatement, null));
    }

    public void finished() throws ConcurrentException {
        try {
            while (!tasks.isEmpty()) {
                Future<Void> f = completionService.take();
                tasks.remove(f);
                f.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw ConcurrentException.wrap(e.getCause());
        } finally {
            while (!tasks.isEmpty())
                tasks.poll().cancel(true);
            executorService.shutdownNow();
        }
    }

}
