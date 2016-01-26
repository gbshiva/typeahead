package com.sag.bigmemory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;


import org.terracotta.toolkit.Toolkit;
import org.terracotta.toolkit.ToolkitFactory;
import org.terracotta.toolkit.concurrent.atomic.ToolkitAtomicLong;

import com.sag.bigmemory.domain.cacheKeyValue;
import com.sag.bigmemory.service.ElasticSearchService;
import com.sag.bigmemory.service.HCOHCPService;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;


/**
 * Cache as a resource


*@CrossOriginResourceSharing(
 *       allowOrigins = true, 
  *      allowCredentials = true, 
   *     maxAge = 1 
        
*)
*/


@Path("/bigmemory/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SuppressWarnings("rawtypes")
public class InMemorySearchService {

    private static int counter = 0;
    private static int bulkLoadCounter = 0;
    private static CacheManager cacheManager = 
    	CacheManager.newInstance();
    private static Logger LOG = LoggerFactory.getLogger(InMemorySearchService.class);
    private ObjectMapper mapper = new ObjectMapper();
    
    public CacheManager getCacheManager() {
        return cacheManager;
    }
    
    
    @GET
    @Path("/getHCPNames")
    public String getHCPNames(@QueryParam("pattern") String pattern,@QueryParam("numrows") int numrows)  throws Exception {
    	String cacheName="hccache";
    	Cache cache = cacheManager.getCache(cacheName);
    	if (cache == null){
    		LOG.error("Unable to get create cache " + cacheName);
    		return "{\"Result\":\"Error Creating cache }"+cacheName+ "\"";
    	}
    	if ( numrows == 0 ){
    		numrows=10;
    	}
    	HCOHCPService aservice = new HCOHCPService(cache);
    	return mapper.writeValueAsString(aservice.getHCPNames(pattern,numrows));
    	
    }
    
    @GET
    @Path("/getHCPNamesFuzzy")
    public String getHCPNamesFuzzy(@QueryParam("pattern") String pattern,@QueryParam("numrows") int numrows)  throws Exception {
    	if ( numrows == 0 ){
    		numrows=10;
    	}
		ElasticSearchService svc = new ElasticSearchService();
    	return mapper.writeValueAsString(svc.getHCPNamesFuzzySearch(pattern, numrows));
    	
    }
    
    
    
    
    
    
    @GET
    @Path("/searchHCO1")
    public String searchHCPNames1(@QueryParam("name") String name, @QueryParam("speciality") String speciality, @QueryParam("address") String address, @QueryParam("city") String city, @QueryParam("state") String state, @QueryParam("zip") String zip)  throws Exception {
    	return "[{\"professionalEnrollmentID\":\"I20110506000477\",\"displayName\":\"MARGOLIES RICHARD \",\"primarySpeciality\":\"\",\"organization\":\"RICHARD P MARGOLIES PA\",\"address1\":\"3355 BURNS RD\",\"city\":\"PALM BEACH GARDENS\",\"state\":\"FL\",\"zipcode\":\"334104356\"}]";

    }
    @GET
    @Path("/searchHCO")
    public String searchHCPNames(@QueryParam("name") String name, @QueryParam("speciality") String speciality, @QueryParam("address") String address, @QueryParam("city") String city, @QueryParam("state") String state, @QueryParam("zip") String zip , @QueryParam("numrows") int numrows)  throws Exception {
    	String cacheName="hccache";
    	Cache cache = cacheManager.getCache(cacheName);
    	if (cache == null){
    		LOG.error("Unable to get create cache " + cacheName);
    		return "{\"Result\":\"Error Creating cache }"+cacheName+ "\"";
    	}
    	if ( numrows == 0 ){
    		numrows=100;
    	}
    	HCOHCPService aservice = new HCOHCPService(cache);
    	return mapper.writeValueAsString(aservice.searchHCP(name,speciality,address,city,state,zip,numrows));
    	
    	
    }
    
    
    
    
    
    
    
    
    /**
     * JSON data is stored within the provided cache. If 'id' attribute is present, that is recognized as the key - update existing object
     * Otherwise, it will be a new object.
     *
     * @param cacheName
     * @param json
     * @return
     * @throws IOException
     */
    @POST
    @Path("/{cache}")
    public String createOrUpdate(@PathParam("cache") 
    		String cacheName, String json) throws Exception {
    	return genericCreateOrUpdate(cacheName, json);
    }

    @GET
    @Path("/{cache}/bulkload")
    public String handleBulkLoad(@PathParam("cache") 
    		String cacheName,@QueryParam("filename") String filename,@QueryParam("batchsize") int batchsize,@QueryParam("threads") int numthreads, @QueryParam("totalrows") int totalrows, @QueryParam("elasticsearch") int elasticsearch) throws Exception {
    	int count=0;
    	Cache cache = cacheManager.getCache(cacheName);
    	if (cache == null){
    		LOG.error("Unable to get create cache " + cacheName);
    		return "{\"Result\":\"Error Creating cache }"+cacheName+ "\"";
    	}
    	if (batchsize == 0){
    		batchsize = 100000;
    	}
    	if (numthreads == 0){
    		numthreads = 5;
    	}
    	
    	
    	cache.setNodeBulkLoadEnabled(true);
    	if (cacheName.equals("hccache")){
    		HCOHCPService aservice = new HCOHCPService(cache);
    		count=aservice.load(filename,batchsize,numthreads,totalrows);
    		
    	}

    	if (elasticsearch == 1){
    		
    		ElasticSearchService svc = new ElasticSearchService();
    		svc.load(filename, batchsize, numthreads, totalrows);
    	}
    		
    		
    	cache.setNodeBulkLoadEnabled(false);
    	return "{\"Result\":\"Bulk Load Success. Loaded " + count +" entries\"}";
    }

	/**
     * Get all keys from the cache
     *
     * @param cacheName
     * @return
     * @throws IOException
     * @throws
     */
    @GET
    @Path("/{cache}/keys")
    public String getAllKeys(@PathParam("cache") String cacheName) throws Exception {
        Ehcache cache = getEhcache(cacheName);
        return mapper.writeValueAsString(cache.getKeys());
    }

    /**
     * Get all values from the given cache
     *
     * @param cacheName
     * @return
     * @throws IOException
     * @throws
     */
    @GET
    @Path("/{cache}/values")
    public String getAllValues(@PathParam("cache") String cacheName) throws Exception {
        Ehcache cache = getEhcache(cacheName);
        List<Object> values = new ArrayList<Object>();
        for (Object key : cache.getKeys()) {
            values.add(cache.get(key).getObjectValue());
        }
        return mapper.writeValueAsString(values);
    }

    /**
     * Get all values from the given cache
     *
     * @param cacheName
     * @return
     * @throws IOException
     * @throws
     */
    @GET
    @Path("/{cache}/keysvalues")
    public String getAllKeysValues(@PathParam("cache") String cacheName) throws Exception {
        Ehcache cache = getEhcache(cacheName);
        List<cacheKeyValue> keyvalues = new ArrayList<cacheKeyValue>();
        
        for (Object key : cache.getKeys()) {
        	keyvalues.add(new cacheKeyValue(cache.get(key).getObjectKey(),cache.get(key).getObjectValue()));
            
        }
        return mapper.writeValueAsString(keyvalues);
    }
    
    /**
     * Return the JSON document stored given the cache and the key name
     *
     * @param cacheName
     * @param key
     * @return
     * @throws IOException
     * @throws
     */
    @GET
    @Path("/{cache}/{key}")
    public String get(@PathParam("cache") String cacheName,
                      @PathParam("key") String key) throws Exception {

            return genericGet(cacheName, key);
    }

    /**
     * Remove all entries from the cache
     *
     * @param cacheName
     * @return
     * @throws IOException
     * @throws
     */
    @DELETE
    @Path("/{cache}/all")
    public String remove(@PathParam("cache") String cacheName) throws Exception {
        Ehcache cache = getEhcache(cacheName);
        cache.removeAll();
        return "Removed all keys from cache " + cacheName;

    }

    /**
     * Return the JSON document stored given the cache and the key name
     *
     * @param cacheName
     * @param key
     * @return
     * @throws IOException
     * @throws
     */
    @DELETE
    @Path("/{cache}/{key}")
    public String remove(@PathParam("cache") String cacheName,
                         @PathParam("key") String key) throws Exception {
        Ehcache cache = getEhcache(cacheName);
        return cache.remove(key) + "";

    }    
    
    
    private String genericCreateOrUpdate(String cacheName, String json) throws Exception {
        Ehcache cache = getEhcache(cacheName);
        // id to the json string
        ObjectNode root = (ObjectNode) mapper.readTree(json);
        JsonNode keyNode = root.get("id");
        String key = null;
        if (keyNode == null)
            key = "" + (counter++);
        else
            key = keyNode.asText();
        root.put("id", key);
        json = mapper.writeValueAsString(root);
  
        cache.put(new Element(key, json));
        LOG.debug("cache[" + cacheName + "] put key[" + key + "] value[" + json + "]");
        return key;
    }


    private String genericGet(String cacheName, String key) throws Exception {
        Ehcache cache = getEhcache(cacheName);
        Element element = cache.get(key);
        if (element == null) {
            LOG.info("cache[" + cacheName + "] get key[" + key + "] NOT FOUND");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        String json = mapper.writeValueAsString( element.getObjectValue());
        LOG.debug("cache[" + cacheName + "] get key[" + key + "] value[" + json + "]");
        return json;
    }

    private Ehcache getEhcache(String cacheName) {
        Ehcache ehCache = cacheManager.getEhcache(cacheName);
        if (ehCache == null) {
            LOG.error("EhCache[" + cacheName + "] NOT FOUND");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return ehCache;
    }
    
    
    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok("")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }
    
    
}