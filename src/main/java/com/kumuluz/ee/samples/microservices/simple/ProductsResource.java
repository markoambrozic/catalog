package com.kumuluz.ee.samples.microservices.simple;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.samples.microservices.simple.Models.Product;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;

@Path("/products")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log(LogParams.METRICS)
public class ProductsResource {

    @PersistenceContext
    private EntityManager em;

    private static final Logger LOG = LogManager.getLogger(ProductsResource.class.getName());

    @GET
    @Log
    public Response getProducts() {
        TypedQuery<Product> query = em.createNamedQuery("Product.findAll", Product.class);

        List<Product> products = query.getResultList();

        LOG.info("Query debug: {}", query);

        return Response.ok(products).build();
    }

    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") Integer id) {

        Product b = em.find(Product.class, id);

        if (b == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(b).build();
    }

    @POST
    public Response createProduct(Product p) {

        p.setId(null);

        em.getTransaction().begin();

        em.persist(p);

        em.getTransaction().commit();

        return Response.status(Response.Status.CREATED).entity(p).build();
    }
}
