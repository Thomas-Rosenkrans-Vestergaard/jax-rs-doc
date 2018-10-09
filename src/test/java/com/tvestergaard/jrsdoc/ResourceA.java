package com.tvestergaard.jrsdoc;

import com.tvestergaard.jrsdoc.annotations.Description;
import com.tvestergaard.jrsdoc.annotations.Name;
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
    @Description("The description of EndpointA on com.tvestergaard.jrsdoc.ResourceA.")
    @Produces(APPLICATION_JSON)
    @Returns(EntityA.class)
    public Response endpointA()
    {
        return null;
    }
}
