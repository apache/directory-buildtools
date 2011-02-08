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

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentException extends RuntimeException {
    private ConcurrentException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public Throwable unwrap() {
        Throwable t = getCause();
        while (t instanceof ConcurrentException)
            t = t.getCause();
        return t;
    }

    public static ConcurrentException wrap(Throwable t) {
        if (t instanceof ConcurrentException)
            t = ((ConcurrentException) t).unwrap();
        ConcurrentException concurrentException = new ConcurrentException(t);
        concurrentException.setStackTrace(t.getStackTrace());
        return concurrentException;
    }

}
