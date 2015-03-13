/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.Self;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

import java.util.List;
import java.util.Map;

/**
 * @author Spencer Gibb
 */
public class ConsulHealthIndicator extends AbstractHealthIndicator {

    @Autowired
    private ConsulClient consul;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try {
            Response<Self> self = consul.getAgentSelf();
            Response<Map<String, List<String>>> services = consul.getCatalogServices(QueryParams.DEFAULT);
            builder.up()
                    .withDetail("services", services.getValue())
                    .withDetail("agent", self.getValue());
        } catch (Exception e) {
            builder.down(e);
        }
    }
}
