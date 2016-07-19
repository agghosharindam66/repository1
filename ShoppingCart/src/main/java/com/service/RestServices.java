package com.service;


import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.RestRequestBean;
import com.util.UtilClass;

@Path("/Image")

@Consumes({MediaType.APPLICATION_JSON})
public class RestServices {
	UtilClass utilClass=new UtilClass();		
	
	@POST
	@Path("/getImageById")
	@Produces("images/*")
	public Response getImageFile(String json) throws Exception {
		RestRequestBean requestBean=getEntity(json, RestRequestBean.class);		
		return Response.ok(utilClass.readImage(requestBean),"images/*").build();
	}
	
	@GET
	@Path("/getCategoryList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategoryList() throws Exception{
			
		return Response.ok(utilClass.getCategoryList(),MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/getMemberDtls")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMemberDtls(String json) throws Exception{
		RestRequestBean requestBean=getEntity(json, RestRequestBean.class);
		return Response.ok(utilClass.getMemberDtls(requestBean.getMemberId()),MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/getProductDtls")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductDtls(String json) throws Exception{
		RestRequestBean requestBean=getEntity(json, RestRequestBean.class);		
		return Response.ok(utilClass.getProductDtls(requestBean.getProductId()),MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/validateUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateUser(String json) throws Exception{
		RestRequestBean requestBean=getEntity(json, RestRequestBean.class);		
		return Response.ok(utilClass.validateUser(requestBean),MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/getProductIdListByCategory")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductIdListByCategory(String json) throws Exception{
		RestRequestBean requestBean=getEntity(json, RestRequestBean.class);		
		return Response.ok(utilClass.getProductIdListByCategory(requestBean),MediaType.APPLICATION_JSON).build();
	}  
	
	@POST
	@Path("/getProductIdListByName")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductIdListByName(String json) throws Exception{
		RestRequestBean requestBean=getEntity(json, RestRequestBean.class);
		return Response.ok(utilClass.getProductIdListByName(requestBean),MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/getImageIdListByProduct")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImageIdListByProduct(String json) throws Exception{
		RestRequestBean requestBean=getEntity(json, RestRequestBean.class);		
		return Response.ok(utilClass.getImageIdListByProduct(requestBean),MediaType.APPLICATION_JSON).build();
	}
	
	public <T> T getEntity(String json, Class<T> type) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, type);
    }
}
