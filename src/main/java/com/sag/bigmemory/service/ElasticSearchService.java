package com.sag.bigmemory.service;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sag.bigmemory.util.ElasticSearchHelper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryBuilders.*;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ElasticSearchService {
	private static Logger LOG = LoggerFactory.getLogger(ElasticSearchService.class);
	
	public int load(String filename, int batchsize, int numthreads, int totalrecords) {
		int count = 0;
		boolean checktotal = false;
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
			if (is == null) {
				LOG.error("Unable to find file");
				return count;
			}

			InputStreamReader ins = new InputStreamReader(is);

			BufferedReader in = new BufferedReader(ins);

			if (in == null) {
				LOG.error("Unable to load data file " + filename);
				return count;
			}
			String line = null;
			if (totalrecords > 0) checktotal=true;
			int totalcount = 0;
			Client client = ElasticSearchHelper.getElasticClient();
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			
			while (((line = in.readLine()) != null) ) {
				if ( (checktotal) && (totalcount > totalrecords) ) break;
				String fields[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				if (fields.length >= 11) {

					bulkRequest.add(client.prepareIndex("genentech", "hcohcp", fields[0])
					        .setSource(jsonBuilder()
					                    .startObject()
					                    .field("hcohcpname", fields[1]+" "+fields[2])
								        .field("npiid",fields[0] )
					                    .endObject()
					                  )
					        );
					
					
				}
				
				if ( count >= batchsize){
					BulkResponse bulkResponse = bulkRequest.get();
					if (bulkResponse.hasFailures()) {
						LOG.error("Error Bulk Request to Elastic search");
						break;
					}
					bulkRequest = client.prepareBulk();
					count=0;
					
				}else{
					
					count++;

				}
				totalcount++;

			}
			LOG.info("Completed loading data from file " + filename + "to Elastic Search  Loaded "
					+ totalcount + " elements");
			in.close();

		} catch (Exception ex) {
			LOG.error("Execption processing", ex);
			ex.printStackTrace();
		}
		return count;
	}

	public String[] getHCPNamesFuzzySearch(String pattern,int numrows) {

	//	String[] names = new String[numrows];
		
		
		SearchResponse response = ElasticSearchHelper.getElasticClient().prepareSearch()
//                        .setQuery(QueryBuilders.wildcardQuery("hcohcpname", "john*")) // Query
				.setQuery(QueryBuilders.fuzzyQuery("hcohcpname", pattern))
                      .setFrom(0).setSize(numrows).setExplain(false)
				.execute().actionGet();

		SearchHit[] results = response.getHits().getHits();
		int index=0;
		String[] names = new String[results.length];
		for (SearchHit hit : results) {
			Map<String, Object> result = hit.getSource();
			names[index]= (String) result.get("hcohcpname");
			index++;
		}
		
		
		
		return names;

	}
	
	
}
