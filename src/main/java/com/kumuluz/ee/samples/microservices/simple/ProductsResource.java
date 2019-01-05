package com.kumuluz.ee.samples.microservices.simple;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.samples.microservices.simple.Models.Product;
import com.kumuluz.ee.samples.microservices.simple.Interceptors.LogContextInterceptor;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

@Path("/products")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class ProductsResource {

    @PersistenceContext
    private EntityManager em;

    private static final Logger LOG = LogManager.getLogger(ProductsResource.class.getName());

    @GET
    @Log
    public Response getProducts() {
        TypedQuery<Product> query = em.createNamedQuery("Product.findAll", Product.class);

        List<Product> products = query.getResultList();
        
        return Response.ok(products).build();
    }

    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") Integer id) {

        Product p = em.find(Product.class, id);

        if (p == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        String ean = p.getEan();
        p.setAdditionalInfo(getAdditionalProductInfo(ean));

        return Response.ok(p).build();
    }

    @GET
    @Path("/additionalInfo/{id}")
    public Response getProductAdditionalInfo(@PathParam("id") Integer id) {

        Product p = em.find(Product.class, id);
        String ean = p.getEan();

        return Response.ok(getAdditionalProductInfo(ean)).build();
    }

    private String getAdditionalProductInfo(String ean) {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://mignify.p.rapidapi.com/gtins/v1.0/productsToGtin?gtin="+ean)
                    .header("X-RapidAPI-Key", "dcae760143mshc50cc7f3978025ep12837cjsn0d9e3d839a0d")
                    .asJson();
            return response.getBody().toString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return "";
    }

    @POST
    public Response createProduct(Product p) {

        p.setId(null);

        em.getTransaction().begin();

        em.persist(p);

        em.getTransaction().commit();

        return Response.status(Response.Status.CREATED).entity(p).build();
    }

    @PUT
    public Response updateProduct(Product p) {

        int id = p.getId();

        em.getTransaction().begin();

        Product pUpdate = em.find(Product.class, id);
        pUpdate.setName(p.getName());
        pUpdate.setDescription(p.getDescription());
        pUpdate.setPrice(p.getPrice());

        em.persist(pUpdate);

        em.getTransaction().commit();

        return Response.status(Response.Status.FOUND).entity(pUpdate).build();
    }

    @DELETE
    public Response deleteProduct(Product p) {
        em.getTransaction().begin();

        p = em.find(Product.class, p.getId());
        em.remove(p);

        em.getTransaction().commit();

        return Response.status(Response.Status.OK).build();
    }
}
