package coffee.tools.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/**
 * @author 王涛
 */
public class XlsWriter {
	private HSSFWorkbook wb; 
	private HSSFSheet sheet;
	private FileOutputStream out;
	//
	private int startY = 0 ;//默认从第1列开始追加 
	private boolean isBold ; //是否粗体(居中)
	
	public static final int AppendType_COL = 0;
	public static final int AppendType_RAW = 1;
	
	public interface AppendType{
		public static int COL = 0;	//列追加
		public static int ROW = 1;	//行追加
	}
	
	
	public XlsWriter(String xlsPath) {
		try {
			File file = new File(xlsPath);
			if(file.exists() == false){
				XlsUtils.createXls(xlsPath);
			}
			FileInputStream fs = new FileInputStream(xlsPath);
			POIFSFileSystem ps = new POIFSFileSystem(fs);
			wb = new HSSFWorkbook(ps);
			sheet = wb.getSheetAt(0);
			out = new FileOutputStream(new File(xlsPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重载
	 * 默认从最左端开始追加
	 * @Overload 
	 */
	private void appendRow(String[] columns) {
		HSSFRow row = sheet.createRow(sheet.getLastRowNum()+1);
		for (int i = 0; i < columns.length; i++) {
			HSSFCell cell = row.createCell(i + startY);
			HSSFCellStyle style = wb.createCellStyle(); 
			//设置样式
			if(isBold){//如果设置是粗体,则默认居中
				HSSFFont font = wb.createFont(); 
				//font.setFontHeightInPoints((short) 11);//字号 
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
				style.setFont(font);
				//居中
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中 
				style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
			
			}
			//style.setWrapText(true); //文本自动换行  
			style.setHidden(true);//超出单元格的文本自动隐藏
			cell.setCellStyle(style);
			//
			cell.setCellValue(columns[i].toString());
		}
	}
	
	private void appendCol(String[] columns) {
		int maxRow = sheet.getLastRowNum();
		//HSSFRow row = sheet.getRow(maxRow);
		//列最后一个非空的Cell的rowIndex
		int lastCellIndex = 0;
		for(int i=maxRow; i>0; i--){
			HSSFCell cell = sheet.getRow(i).getCell(startY);
			if(cell == null){
				continue;
			}
			String value = XlsUtils.getCellValue(cell);
			if(value != null && value.trim().length() > 0){
				lastCellIndex = i+1;
				break;
			}
		}
		for (int i = 0; i < columns.length; i++) {
			HSSFRow row = sheet.getRow(lastCellIndex + i);
			if(row == null){
				row = sheet.createRow(lastCellIndex + i);
			}
			HSSFCell cell = row.createCell(startY);////////////
			HSSFCellStyle style = wb.createCellStyle(); 
			//设置样式
			if(isBold){//如果设置是粗体,则默认居中
				HSSFFont font = wb.createFont(); 
				//font.setFontHeightInPoints((short) 11);//字号 
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
				style.setFont(font);
				//居中
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中 
				style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
			
			}
			//style.setWrapText(true); //文本自动换行  
			style.setHidden(true);//超出单元格的文本自动隐藏
			cell.setCellStyle(style);
			//
			cell.setCellValue(columns[i].toString());
		}
	}
	
	/**
	 * 追加
	 * @param columns ：待追加的数据 
	 * @param x ：起始列 , 从0开始
	 */
	public void append(String[] columns, int startY,int type) {
		this.startY = startY;
		if(type == AppendType.ROW){
			this.appendRow(columns);
		}else{
			this.appendCol(columns);
		}
	}

	/**
	 * @param <T>
	 * @param itemsList
	 * @param startY
	 * @param type
	 */
	public <T> void append(List<T> itemsList,int startY,int type){
		if(itemsList.size() > 0){
			Class<?> clazz = itemsList.get(0).getClass();
			Field[] fields = clazz.getDeclaredFields();
			String[] values = new String[fields.length];
			int i=0;
			for(T t : itemsList){
				i = 0;
				for(Field field : fields){
					field.setAccessible(true);
					try {
						Object obj = field.get(t);
						if(obj == null){
							obj = "";
						}
						values[i] = obj.toString();
						i++;
					}catch (Exception e) {
						e.printStackTrace();
					}
				}//for
				append(values, startY, type);
			}//for
		}//if
	}
	
	/**
	 * 方法重载
	 * @param columns
	 * @param isBold : 字体是否是粗体
	 */
	public void append(String[] columns, int startY, int type, boolean isBold){
		this.startY = startY;
		this.isBold = isBold;
		this.startY = startY;
		if(type == AppendType.ROW){
			this.appendRow(columns);
		}else{
			this.appendCol(columns);
		}
	}
	
	
	
	public void flush(){
		try {
			out.flush();
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//关闭 IO 流
	public void close(){
		try {
			out.flush();
			wb.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}