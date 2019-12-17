package com.touraj.priceconsumer.rest;

import com.google.gson.Gson;
import com.touraj.priceconsumer.domain.Cache;
import com.touraj.priceconsumer.domain.PriceData;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by toraj on 08/26/2018.
 */
@Path("/lastprice")
public class LastValuePriceRestService {

    @GET
    @Path("/{id}")
    public Response getMsg(@PathParam("id") String id) {

        try {
            PriceData priceData = Cache.getLastValuePriceByID(id);
            Gson gson = new Gson();
            String priceDataString = gson.toJson(priceData);

            if (priceData != null) {
            return Response.status(200).entity(priceDataString).build();
            } else
            {
                return Response.status(404).entity("Data Not Available; Please send request later").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Internal Server Error; Please Contact Admin!").build();
        }
    }
}