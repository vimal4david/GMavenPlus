/*
 * Copyright (C) 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gmavenplus.mojo;

import gmavenplus.util.ReflectionUtils;
import java.lang.reflect.InvocationTargetException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;


/**
 * @author Keegan Witt
 *
 * @requiresDependencyResolution compile
 * @configurator include-project-dependencies
 */
public abstract class AbstractGroovyMojo extends AbstractMojo {
    private Log log;

    @Override
    public Log getLog() {
        if (log == null) {
            log = new SystemStreamLog();
        }

        return log;
    }

    /**
     * @param goal
     */
    protected void logGroovyVersion(String goal) {
        if (getLog().isInfoEnabled()) {
            getLog().info("Using Groovy " + getGroovyVersion() + " from project compile classpath to perform " + goal);
        }
    }

    /**
     * @return
     */
    protected String getGroovyVersion() {
        String groovyVersion = null;

        try {
            /* This method is considered to be for internal-only use by the Groovy folks, but the preferred method
             * <code>groovy.lang.GroovySystem.getVersion()</code> was not added until 1.6.6.  So to reliably get the
             * information we need, we're going to use this method anyway.
             */
            Class InvokerHelperClass = Class.forName("org.codehaus.groovy.runtime.InvokerHelper");
            groovyVersion = (String) ReflectionUtils.invokeStaticMethod(ReflectionUtils.findMethod(InvokerHelperClass, "getVersion"));
        } catch (ClassNotFoundException e) {
            getLog().warn("Unable to log Groovy Version", e);
        } catch (IllegalAccessException e) {
            getLog().warn("Unable to log Groovy Version", e);
        } catch (InvocationTargetException e) {
            getLog().warn("Unable to log Groovy Version", e);
        }

        return groovyVersion;
    }

}