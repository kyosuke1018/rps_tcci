/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apereo.inspektr.common.spi;

import org.aspectj.lang.JoinPoint;
import org.apereo.inspektr.common.web.ClientInfo;
import org.apereo.inspektr.common.web.ClientInfoHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation that gets it from the ThreadLocal.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 1.0
 *
 */
public class DefaultClientInfoResolver implements ClientInfoResolver {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public ClientInfo resolveFrom(final JoinPoint joinPoint, final Object retVal) {
        final ClientInfo clientInfo = ClientInfoHolder.getClientInfo();

        if (clientInfo != null) {
            return clientInfo;
        }

        log.warn("No ClientInfo could be found.  Returning empty ClientInfo object.");

        return ClientInfo.EMPTY_CLIENT_INFO;
    }
}
