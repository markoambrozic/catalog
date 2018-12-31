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
import java.util.List;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;

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

        return Response.ok(p).build();
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
