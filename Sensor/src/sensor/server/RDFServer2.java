package sensor.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

import sensor.dataitems.*;
import sensor.rdf.SensorRDF;

//为Sensor生成模拟的RDF数据
public class RDFServer2 {
	// 保存所有Socket的ArrayList
	public static ArrayList<Socket> socketList = new ArrayList<>();

	public static Boolean isClientClose(Socket socket) {
		try {
			socket.sendUrgentData(0);// 发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
			return false;
		}
		catch (Exception se) {
			return true;
		}
	}
	
	/**
	 * 
	 * @param args[0] 每种房间个数，args[1] 监听端口号
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("Usage:RDFServer <each_num> <listening_port>");
			System.exit(1);
		}
		
		int eachNum=Integer.parseInt(args[0]);	//每种房间的个数
		
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
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("a.txt")));
		
		for (DataItem di : dl) {
			SensorRDF.generateRDFModel(di).write(bw, "RDF/XML");
		}
		
		File file = new File("a.txt");
		final BufferedReader br = new BufferedReader((new FileReader(file)));
//		br.mark((int)file.length()+1);
		
		StringBuffer sb = new StringBuffer();
		String line = "";
		
		while((line=br.readLine())!=null){
			sb.append(line+"\n");
		}
		
		final String total = sb.toString();
		bw.close();
		br.close();
		
		// 启动服务器。
		ServerSocket ss = new ServerSocket(Integer.parseInt(args[1]));

		while (true) {
			System.out.println("Server is waiting for connection from Spark input stream...");
			final Socket s = ss.accept();
			System.out.println(s.getInetAddress() + " connected!");

			socketList.add(s);

			/*
			PrintStream ps = new PrintStream(s.getOutputStream());
			// 线程池，每个传感器的RDF流由一个线程生成
			ExecutorService pool = Executors.newFixedThreadPool(80);
			for (DataItem di : dl) {
				pool.submit(new SensorRDFStreamGenerator(s, di, ps));
			}*/

			
			//new Thread(new ServerThread(s, dl)).start();
			
			
			try {
				final PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

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
						
						pw.print(total);

					}
				}, 0, 5000);	//周期5秒
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}
