/**
 * The classes in this package represent PetClinic's REST API.
 * 
 * <p>This package contains RESTful web service endpoints built on Jakarta
 * RESTful Services (JAX-RS) API. All endpoints follow standard REST conventions
 * for HTTP method semantics and status code handling.
 * 
 * <p>Key features:
 * <ul>
 * <li>{@code @Path} - defines resource URIs</li>
 * <li>{@code @GET}, {@code @POST}, {@code @PUT}, {@code @DELETE} - HTTP method mappings</li>
 * <li>{@code @Produces}, {@code @Consumes} - content negotiation for JSON</li>
 * <li>{@code @PathParam}, {@code @QueryParam} - parameter extraction</li>
 * <li>Exception handling via {@code jakarta.ws.rs.ext.Provider}</li>
 * </ul>
 * 
 * <p>All REST endpoints in this package use Jakarta Validation API for input
 * validation and return standardized error responses via {@code BindingErrorsResponse}.
 * 
 * @since Jakarta EE 9 / Jakarta RESTful Services 3.0
 */
package com.demo.rest;