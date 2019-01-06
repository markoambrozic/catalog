/*
 *  Copyright (c) 2014-2017 Kumuluz and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
*/
package com.kumuluz.ee.samples.microservices.simple;

import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.inject.Inject;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import java.util.Optional;

@Path("/info")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InfoResource {

    @PersistenceContext
    private EntityManager em;

    @Inject
    @DiscoverService(value = "order-service", environment = "dev", version = "*")
    private Optional<String> basePath;

    @Inject
    @DiscoverService(value = "cart-service", environment = "dev", version = "*")
    private Optional<String> otherPath;

    @GET
    public Response getInfo() {
        JSONObject obj = new JSONObject();

        obj.put("clani", new String[] {"ma6794", "dm9929"});
        obj.put("opis_projekta", "Najin projekt implementita plug & play vtičnik za povečanje skalabilnosti in odpornosti na napake monolitnih ecommerce platform");
        obj.put("mikrostoritve", new String[] {"http://13.81.31.49:8081/orders", "http://51.145.135.199:8080/products"});
        obj.put("github", new String[] {"https://github.com/rso-team2/catalog", "https://github.com/rso-team2/orders"});
        obj.put("travis", new String[] {"https://travis-ci.com/markoambrozic/catalog", "https://travis-ci.com/markoambrozic/orders"});
        obj.put("dockerhub", new String[] {"https://hub.docker.com/r/markoambrozic/catalog/", "https://hub.docker.com/r/markoambrozic/orders/"});
        obj.put("order-service",basePath.get());
        obj.put("cart-service",otherPath.get());
        return Response.ok(obj.toString()).build();
    }
}
