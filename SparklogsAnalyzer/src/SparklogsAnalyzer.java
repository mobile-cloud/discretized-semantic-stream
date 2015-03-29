import java.io.*;
import java.util.regex.Pattern;  
import java.util.regex.Matcher;
import java.util.Date; 
import jxl.*; 
import jxl.write.*; 
import jxl.write.Number;

public class SparklogsAnalyzer {

	public static void main(String[] args) throws IOException, WriteException {
		String dir=args(0);
		String fileName=args(1);
		
		String inPath=dir+fileName+".log";
		WritableWorkbook workbook = 
			Workbook.createWorkbook(new File(dir+fileName+".xls"));
		WritableSheet sheet = workbook.createSheet("FirstSheet", 0);
		
		File file = new File(inPath);
		BufferedReader br=null;
		try {
			br =new BufferedReader(new FileReader(file));
			String line="";
			int col=1;
			while((line=br.readLine())!=null){
				if(line.contains("Total delay")){
					System.out.println(line);
					Pattern p = Pattern.compile("delay: \\d+\\.\\d+");
					Matcher m = p.matcher(line);
					while(m.find()){
						//System.out.println(m.group());
						Pattern p1 = Pattern.compile("\\d+\\.\\d+");
						Matcher m1 = p1.matcher(m.group());
						while(m1.find()){
							System.out.println(m1.group());
							Number number = new Number(col, 1, Double.parseDouble(m1.group())); 
							sheet.addCell(number);
							col++;
						}
					}
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			br.close();
		}
		
		workbook.write(); 
		workbook.close();

	}

}
