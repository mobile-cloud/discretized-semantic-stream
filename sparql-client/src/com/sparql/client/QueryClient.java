package com.sparql.client;

import java.io.*;
import java.net.*;

/*
 Query Statements:
 Q1
 PREFIX p:<http://hem.org/predicate#> SELECT ?type ?value WHERE {<http://hem.org/room#bedroom_1> p:hasSensor ?sensor . ?sensor p:hasValue ?value . ?sensor p:valueType ?type . }

 Q2
 PREFIX p:<http://hem.org/predicate#> SELECT ?value WHERE {<http://hem.org/room#bedroom_1> p:hasSensor ?sensor . ?sensor p:hasValue ?value . ?sensor p:valueType 'PM2p5' . }

 Q3
 PREFIX p:<http://hem.org/predicate#> SELECT ?room ?value WHERE { ?room p:hasSensor ?sensor . ?sensor p:hasValue ?value . ?sensor p:valueType 'CO' . }

 Q4
 PREFIX p:<http://hem.org/predicate#> SELECT ?sensor WHERE { ?sensor p:hasValue ?value . ?sensor p:valueType 'SO2' . }

 Q5
 PREFIX p:<http://hem.org/predicate#> SELECT ?room ?time  WHERE { ?room p:hasSensor ?sensor . ?sensor p:hasValue ?value . ?sensor p:valueType 'humidity' . ?sensor p:samplingTime ?time . }

 Q6
 PREFIX p:<http://hem.org/predicate#> SELECT ?value ?time WHERE {<http://hem.org/room#bedroom_1> p:hasSensor ?sensor . ?sensor p:hasValue ?value . ?sensor p:valueType 'temp' . ?sensor p:samplingTime ?time }
 */

public class QueryClient {
	static String queryStr = "";

	/**
	 * 
	 * @param args
	 *            (0) SPARQL server port
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));

		new Thread(new SparqlReceiver()).start();
		new Thread(new Listener(ss)).start();

//		while (true) {
//			System.out.println("SPARQL server is waiting for connection!");
//			Socket s = ss.accept();
//			System.out.println("socket connected!");
//			PrintStream ps = new PrintStream(s.getOutputStream());
//
//			ps.println(queryStr);
//			System.out.println("socket closed!");
//			s.close();
//		}

	}
}

class SparqlReceiver implements Runnable {
	public SparqlReceiver() {

	}

	@Override
	public void run() {
		while (true) {
			System.out.println("Please input SPARQL query statement:");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String line = "";
			String query = "";
			try {
				while ((line = br.readLine()) != null) {
					query += line;
					if (line.endsWith("}")) {
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			QueryClient.queryStr = query;
		}
	}// run()
}

class Listener implements Runnable {
	ServerSocket ss = null;

	public Listener(ServerSocket ss) {
		this.ss = ss;
	}

	public static Boolean isClientClose(Socket socket) {
		try {
			// 发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
			socket.sendUrgentData(0);
			return false;
		} catch (Exception se) {
			return true;
		}
	}

	public void run() {
		try {
			while (true) {
				System.out.println("SPARQL server is waiting for connection!");
				Socket s = ss.accept();
				System.out.println(s.getInetAddress() + " connected!");
//				boolean isSocketClosed = isClientClose(s);
//				if (isSocketClosed) {
//					System.out.println("Is connection to "
//							+ s.getInetAddress().getHostAddress() + " closed? "
//							+ isSocketClosed);
//					try {
//						s.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
				PrintStream ps = new PrintStream(s.getOutputStream());
				ps.println(QueryClient.queryStr);
				System.out.println("socket closed");
				s.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
