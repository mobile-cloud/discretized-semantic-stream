package sensor.server;

import java.io.PrintStream;
import java.net.Socket;

import sensor.dataitems.DataItem;
import sensor.rdf.SensorRDF;

class SensorRDFStreamGenerator implements Runnable {
	private DataItem dataItem = null;
	private Socket socket = null;
	private PrintStream ps = null;

	public SensorRDFStreamGenerator(Socket s, DataItem di, PrintStream ps) {
		this.socket = s;
		this.dataItem = di;
		this.ps = ps;
	}

	public Boolean isClientClose(Socket socket) {
		try {
			socket.sendUrgentData(0);// ����1���ֽڵĽ������ݣ�Ĭ������£���������û�п����������ݴ�����Ӱ������ͨ��
			return false;
		}
		catch (Exception se) {
			return true;
		}
	}

	public void run() {
		try {
			while (true) {
				boolean isSocketClosed = isClientClose(socket);
				if (isSocketClosed) {
					//System.out.println("Is connection to " + socket.getInetAddress().getHostAddress() + " closed? " + isSocketClosed);
					RDFServer.socketList.remove(socket);
					socket.close();
					break;
				}
				synchronized(ps){
					SensorRDF.generateRDFModel(dataItem).write(ps, "RDF/XML");
				}
				//ps.println("####");
				Thread.sleep(2000);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
