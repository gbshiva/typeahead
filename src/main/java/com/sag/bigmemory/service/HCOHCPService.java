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

	public HCOHCPService(Cache c) {
		this.hccache = c;
	}

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
			int batchcount = 0;
			List inputdata = new ArrayList();
			
			while (((line = in.readLine()) != null) ) {
				if ( (checktotal) && (totalcount > totalrecords) ) break;
				totalcount++;
				String fields[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				if (fields.length >= 11) {
					// hcohcp a = new hcohcp(fields[0], fields[4], fields[3],
					// fields[6], fields[8], fields[17], fields[21],
					// fields[22], fields[24], fields[25], fields[26]);

					hcohcp a = new hcohcp(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6],
							fields[7], fields[8], fields[9], fields[10]);

					Element data = new Element(fields[0], a);
					count++;
					inputdata.add(data);
					batchcount++;
					if (batchcount >= batchsize) {
						// hccache.put(data);

						int eachThreadCount = batchcount / numthreads;
						ExecutorService executor = Executors.newFixedThreadPool(numthreads);
						for (int i = 0; i < numthreads; i++) {
							List sublist = sublist(inputdata, i * eachThreadCount, (i + 1) * eachThreadCount);
							Runnable worker = new Cacheput(hccache, sublist);
							executor.execute(worker);
						}
						executor.shutdown();
						while (!executor.isTerminated()) {
							Thread.sleep(50L);
						}
						batchcount=0;
						inputdata.clear();
					}
					

				}
			}
			LOG.info("Completed loading data from file " + filename + "to Cache " + hccache.getName() + " Loaded "
					+ totalcount + " elements");
			in.close();

		} catch (Exception ex) {
			LOG.error("Execption processing", ex);
			ex.printStackTrace();
		}
		return count;
	}

	public class Cacheput implements Runnable {
		private List keys;
		private Cache cache;

		public Cacheput(Cache cache, List keys) {
			this.cache = cache;
			this.keys = keys;
		}

		public void run() {
			for (int i = 0; i < this.keys.size(); i++) {
				cache.put((Element) this.keys.get(i));

			}
		}
	}

	public String[] getHCPNames(String pattern,int numrows) {

		String[] names = new String[numrows];
		Attribute<String> name = hccache.getSearchAttribute("name");

		QueryManager queryManager = QueryManagerBuilder.newQueryManagerBuilder().addCache(hccache).build();

		Query assetQuery = queryManager
				.createQuery("select name from hccache where ( name like '" + pattern + "%') limit "+numrows);

		Results results = assetQuery.end().execute();
		int i = 0;
		for (Result result : results.all()) {

			names[i] = (String) result.getAttribute(name);
			i++;

		}
		results.discard();
		return names;

	}

	public List searchHCP(String name, String speciality, String address, String city, String state, String zip,int numrows) {

		List data = new ArrayList();
		QueryManager queryManager = QueryManagerBuilder.newQueryManagerBuilder().addCache(hccache).build();

		String query = "select value from hccache ";
		boolean first = true;

		if (name != null) {
			if (first) {
				query += "where ( ";
				first = false;
				query += "( name like '" + name + "%')";
			} else {
				query += " and ( name like '" + name + "%')";
			}
		}

		if ((address != null) && (address.length() > 0)) {
			if (first) {
				query += "where ( ";
				first = false;
				query += "( address like '" + address + "%')";
			} else {
				query += " and ( address like '" + address + "%')";
			}
		}

		if ((state != null) && (state.length() > 0)) {
			if (first) {
				query += "where (";
				first = false;
				query += "( state = '" + state + "')";
			} else {
				query += " and ( state = '" + state + "')";
			}
		}

		if ((zip != null) && (zip.length() > 0)) {
			if (first) {
				query += "where (";
				first = false;
				query += "( zip = '" + zip + "')";
			} else {
				query += " and ( zip = '" + zip + "')";
			}
		}

		if ((city != null) && (city.length() > 0)) {
			if (first) {
				query += "where ( ";
				first = false;
				query += "( city = '" + city + "')";
			} else {
				query += " and ( city = '" + city + "')";
			}
		}

		if ((speciality != null) && (speciality.length() > 0)) {
			if (first) {
				query += "where ( ";
				first = false;
				query += "( speciality = '" + speciality + "')";
			} else {
				query += " and ( speciality = '" + speciality + "')";
			}
		}

		if (first)
			query += " limit "+ numrows;
		else
			query += " ) limit "+ numrows;

		LOG.info("Executing BigMemory Query = " + query);

		Query hcoQuery = queryManager.createQuery(query);
		Results results = hcoQuery.end().execute();

		for (Result result : results.all()) {
			hcohcp hs = (hcohcp) result.getValue();
			JSONHCOHCP dhs = new JSONHCOHCP(hs.getProfessionalEnrollmentID(), hs.getDisplayName(),
					hs.getPrimarySpeciality(), hs.getOrganization(), hs.getAddress1(), hs.getCity(), hs.getState(),
					hs.getZipcode());
			data.add(dhs);
		}

		return data;

	}

	public static List sublist(List s, int strindex, int endindex) {
		ArrayList sub = new ArrayList();
		for (int i = 0; i < endindex - strindex; i++) {
			sub.add(s.get(strindex + i));
		}

		return sub;
	}

}
