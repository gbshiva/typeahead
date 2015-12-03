package com.sag.bigmemory.service;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import net.sf.ehcache.search.query.QueryManager;
import net.sf.ehcache.search.query.QueryManagerBuilder;

import com.sag.bigmemory.InMemorySearchService;
import com.sag.bigmemory.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HCOHCPService {
	private static Logger LOG = LoggerFactory.getLogger(HCOHCPService.class);
	private Cache hccache;
	public HCOHCPService(Cache c){
		this.hccache = c;
	}
	
	
	public int load(String filename){
		int count = 0;
		
		try {
			
			InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
			if (is == null){
				LOG.error("Unable to find file");
				return count;
			}
			
			
			InputStreamReader ins = new InputStreamReader(is);
				
			BufferedReader in = new BufferedReader( ins);
			
			
			if (in == null) {
				LOG.error("Unable to load data file "+filename );
				return count;
			}
			String line = null;
		
			
			while ((line = in.readLine()) != null) {
				String fields[] = line.split(",");
				if (fields.length > 26){
					hcohcp a = new hcohcp(fields[2],fields[3],fields[4],fields[6],fields[8],fields[17],fields[21],fields[22],fields[24],fields[25],fields[26]);
				Element data = new Element(fields[2], a);
				count++;
				hccache.put(data);
				}
				
			}
			LOG.info("Completed loading data from file " + filename + "to Cache " + hccache.getName() +" Loaded "+count +" elements");
			
			in.close();
			
		} catch (Exception ex) {
			LOG.error("Execption processing",ex);
			ex.printStackTrace();
		}
		return count;
	}

	public String[] getHCPNames(String pattern){
		
		String[] names = new String[10];
		Attribute<String> name = hccache.getSearchAttribute("name");

		QueryManager queryManager = QueryManagerBuilder.newQueryManagerBuilder().addCache(hccache).build();

		Query assetQuery = queryManager.createQuery(
				"select name from hccache where ( name like '"+pattern+"%') limit 10");
		
		Results results = assetQuery.end().execute();
		 int i=0;
		for (Result result : results.all()) {
	
			names[i]=(String)result.getAttribute(name);
			i++;
			
		}
		
		return names;
		
	}
	
	
	private static int getCounts(Query query, int cardinality){
		Results assetResults = query.execute();
		int totalcount=0;
		for (Result result : assetResults.all()) {
			//System.out.println(result);
			Iterator val = result.getAggregatorResults().iterator();
			while ( val.hasNext()){
				int count = (Integer)val.next();
				if (count == cardinality)
					totalcount++;
			}
		}
		assetResults.discard();
		return totalcount;
		
		
	}

}
