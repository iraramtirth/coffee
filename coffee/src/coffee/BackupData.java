package coffee;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * 备份数据库
 * @author bob
 *
 */
public class BackupData {
	public static void main(String[] args) {
		try {
			String cmd = "mysql -hlocalhost -uroot -proot test";
			
			cmd = "mysqldump -u root -proot test>test_users.sql";


			ProcessBuilder pb = new ProcessBuilder(cmd);

			pb.redirectErrorStream(true);

			Process p = pb.start();

			p.getOutputStream().close();

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String tmp = null;

			while ((tmp = br.readLine()) != null) {

			System.out.println(tmp);

			}


			
			
			final Process  process=Runtime.getRuntime().exec(cmd); 
			System.out.println( "tt "); 
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(true){
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
						try {
							writer.write("\ndfdfdfd");
							writer.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();;
			BufferedReader   inputBufferedReader=new   BufferedReader(new   InputStreamReader(process.getInputStream())); 
			String   line   =   null; 
			while((line   =   inputBufferedReader.readLine())   !=   null) 
			{ 
			System.out.println("xxx--" + line); 
			} 
			process.waitFor(); 
			System.out.println( "tt2 "); 

			
			
		//	new BackupData().backup(cmd, "ss.sql");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 /**
     * 备份检验一个sql文件是否可以做导入文件用的一个判断方法：把该sql文件分别用记事本和ultra
     * edit打开，如果看到的中文均正常没有乱码，则可以用来做导入的源文件（不管sql文件的编码格式如何，也不管db的编码格式如何）
	 * @throws IOException 
     */
    public void backup(String cmd,String filename) throws IOException {
    	FileOutputStream fout=null;
    	OutputStreamWriter writer=null;
        try {
            Runtime rt = Runtime.getRuntime();
            // 调用 mysql 的 cmd:
            Process child = rt.exec(cmd);// 设置导出编码为utf8。这里必须是utf8
            // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行
            
            Scanner scanner = new Scanner(child.getInputStream());
            
            System.out.println(scanner.nextLine());
            DataInputStream in = new DataInputStream(child.getInputStream());
            byte[] data = new byte[1024 * 10];
            in.read(data);
            System.out.println(new String(data,0,data.length));
            StringBuffer sb = new StringBuffer("");
            String outStr;
            // 组合控制台输出信息字符串
            outStr = sb.toString();
            // 要用来做导入用的sql目标文件：
            fout = new FileOutputStream("e:\\"+filename);
            writer = new OutputStreamWriter(fout, "utf-8");
            writer.write(outStr);
            // 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免
            writer.flush();
            // 别忘记关闭输入输出流
            System.out.println("/* Output OK! */");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            fout.close();
            writer.close();
        }

    }
    /**
     * 导入
     * @throws IOException 
     *
     */
    public  void load(String path,String filepath) throws IOException {
    	OutputStream out=null;
    	BufferedReader br=null;
    	OutputStreamWriter writer=null;
    	Process child = null;
    	FileInputStream in =null;
        try {
            // 要用来做导入用的sql目标文件：
        	Runtime rt = Runtime.getRuntime();
            in = new FileInputStream(filepath);
            // 调用 mysql 的 cmd:
            child = rt.exec(path);

            out = child.getOutputStream();//控制台的输入信息作为输出流
            InputStreamReader xx =new InputStreamReader(in, "utf-8");
            String inStr;
            StringBuffer sb = new StringBuffer("");
            String outStr;
            br = new BufferedReader(xx);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();
            writer = new OutputStreamWriter(out, "utf-8"); // 设置输出流编码为utf8。这里必须是utf8，否则从流中读入的是乱码
            writer.write(outStr);
            // 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免
            writer.flush();
           
            System.out.println("/* Load OK! */");

        } catch (Exception e) {
            e.printStackTrace();
        } finally{  
            // 别忘记关闭输入输出流
        	in.close();
            writer.close();
            out.close();
            br.close();
            child.destroy();
        }

    }
}
