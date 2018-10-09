package com.tvestergaard.jrsdoc.testdata;

import com.tvestergaard.jrsdoc.annotations.Description;
import com.tvestergaard.jrsdoc.annotations.Name;
import com.tvestergaard.jrsdoc.annotations.Param;
import com.tvestergaard.jrsdoc.annotations.Returns;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("resource-a")
@Name("Resource-A")
@Description("The description of Resource-A.")
public class ResourceA
{

    @GET
    @Name("EndpointA")
    @Description("The description of EndpointA on ResourceA.")
    @Produces(APPLICATION_JSON)
    @Returns(list = EntityA.class, description = "The description of the return value.")
    public Response endpointA()
    {
        return null;
    }

    @GET
    @Path("{parameter}")
    @Param("The parameter description.")
    @Name("EndpointB")
    @Description("The description of EndpointB on ResourceA.")
    @Produces(APPLICATION_JSON)
    @Returns(value = EntityA.class, description = "The description of the return value")
    public Response endpointB()
    {
        return null;
    }
}
