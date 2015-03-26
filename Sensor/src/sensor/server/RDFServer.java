package sensor.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

import sensor.dataitems.*;
import sensor.rdf.SensorRDF;

//ΪSensor���ģ���RDF���
public class RDFServer {
	// ��������Socket��ArrayList
	public static ArrayList<Socket> socketList = new ArrayList<>();
	
	public static Boolean isClientClose(Socket socket) {
		try {
			socket.sendUrgentData(0);
			return false;
		}
		catch (Exception se) {
			return true;
		}
	}

	/**
	 * 
	 * @param args[0] ÿ�ַ������args[1] ����˿ں�
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("Usage:RDFServer <each_num> <listening_port>");
			System.exit(1);
		}
		
		int eachNum=Integer.parseInt(args[0]);	//ÿ�ַ���ĸ���
		
		int livingroomNum=eachNum;
		int bedroomNum=eachNum;
		int studyroomNum=eachNum;
		int kitchenNum=eachNum;
		int bathroomNum=eachNum;
		
		ArrayList<String> livingroomList = new ArrayList<String>();
		ArrayList<String> bedroomList = new ArrayList<String>();
		ArrayList<String> studyroomList = new ArrayList<String>();
		ArrayList<String> kitchenList = new ArrayList<String>();
		ArrayList<String> bathroomList = new ArrayList<String>();
		ArrayList<String> allroomList = new ArrayList<String>();
		
		for(int i=0;i<livingroomNum;i++)
			livingroomList.add("livingroom");
		
		for(int i=0;i<bedroomNum;i++)
			bedroomList.add("bedroom");
		
		for(int i=0;i<studyroomNum;i++)
			studyroomList.add("studyroom");
		
		for(int i=0;i<kitchenNum;i++)
			kitchenList.add("kitchen");
		
		for(int i=0;i<bathroomNum;i++)
			bathroomList.add("bathroom");
		
		allroomList.addAll(livingroomList);
		allroomList.addAll(bedroomList);
		allroomList.addAll(studyroomList);
		allroomList.addAll(kitchenList);
		allroomList.addAll(bathroomList);
		

		//String[] rooms = { "livingroom", "bedroom", "studyroom","kitchen","bathroom"};
		String[] dataTypes = { "temp","humidity","volume","illumination","HCHO","benzene","NH3","TVOC",
			"CO2","PM10","PM2p5","O3","CO","SO2","NO2" }; //15

		SensorFactory.addAllRoom(allroomList);
		SensorFactory.addAllDataType(dataTypes);
		final ArrayList<DataItem> dl = SensorFactory.generateSensorDataItemList();
		
		// ����������
		ServerSocket ss = new ServerSocket(Integer.parseInt(args[1]));

		while (true) {
			System.out.println("Server is waiting for connection from Spark input stream...");
			final Socket s = ss.accept();
			System.out.println(s.getInetAddress() + " connected!");

			socketList.add(s);

			/*
			PrintStream ps = new PrintStream(s.getOutputStream());
			// �̳߳أ�ÿ����������RDF����һ���߳����
			ExecutorService pool = Executors.newFixedThreadPool(80);
			for (DataItem di : dl) {
				pool.submit(new SensorRDFStreamGenerator(s, di, ps));
			}*/

			
			//new Thread(new ServerThread(s, dl)).start();
			
			
			try {
				final PrintStream ps = new PrintStream(s.getOutputStream());
				
				
				Timer timer = new Timer();
				
				timer.scheduleAtFixedRate(new TimerTask(){
					public void run(){
						boolean isSocketClosed = isClientClose(s);
						if (isSocketClosed) {
							System.out.println("Is connection to " + s.getInetAddress().getHostAddress() + " closed? " + isSocketClosed);
							RDFServer.socketList.remove(s);
								try {
									s.close();
								} catch (IOException e) {
									e.printStackTrace();
								}			
							this.cancel();
						}
						
						for (DataItem di : dl) {
							SensorRDF.generateRDFModel(di).write(ps, "RDF/XML");
						}

					}
				}, 0, 5000);	//����5��
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}//while
	}
}


class ServerThread implements Runnable {
	// ���嵱ǰ�߳������Socket
	Socket s = null;
	ArrayList<DataItem> dataitemList = null;

	public ServerThread(Socket s, ArrayList<DataItem> dl) throws IOException {
		this.s = s;
		this.dataitemList = dl;
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
			PrintStream ps = new PrintStream(s.getOutputStream());
			while (true) {
				boolean isSocketClosed = isClientClose(s);
				if (isSocketClosed) {
					System.out.println("Is connection to " + s.getInetAddress().getHostAddress() + " closed? " + isSocketClosed);
					RDFServer.socketList.remove(s);
					s.close();
					break;
				}
				
				for (DataItem di : dataitemList) {
					SensorRDF.generateRDFModel(di).write(ps, "RDF/XML");
				}

				Thread.sleep(2000);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
