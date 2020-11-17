package com.anahuac.rest.api.DAO
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Base64;

import java.awt.Color;
import com.anahuac.rest.api.Entity.Result;
import com.bonitasoft.web.extension.rest.RestAPIContext;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import be.quodlibet.boxable.*;
import be.quodlibet.boxable.line.LineStyle;
import be.quodlibet.boxable.text.WrappingFunction;


class ArchivosDAO {
	
	
	
	public Result base64Encode(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		List<String> lstResultado = new ArrayList<String>();
		try {
			PDFont fontPlain = PDType1Font.HELVETICA;
			PDFont fontBold = PDType1Font.HELVETICA_BOLD;
			PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
			PDFont fontMono = PDType1Font.COURIER;
			
			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			PDRectangle rect = page.getMediaBox();
			document.addPage(page);
 
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
 
			contentStream.setFont(PDType1Font.COURIER, 12);
			
			//Dummy Table
			float margin = 50;
			// starting y position is whole page height subtracted by top and bottom margin
			float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
			// we want table across whole page width (subtracted by left and right margin ofcourse)
			float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
	
			boolean drawContent = true;
			float yStart = yStartNewPage;
			float bottomMargin = 70;
			// y position is your coordinate of top left corner of the table
			float yPosition = 550;
	
			BaseTable table = new BaseTable(yPosition, yStartNewPage,
				bottomMargin, tableWidth, margin, document, page, true, drawContent);
	
			// the parameter is the row height
			Row<PDPage> headerRow = table.createRow(50);
			// the first parameter is the cell width
			Cell<PDPage> cell = headerRow.createCell(100, "Header");
			cell.setFont(fontBold);
			cell.setFontSize(20);
			// vertical alignment
			cell.setValign(VerticalAlignment.MIDDLE);
			// border style
			cell.setTopBorderStyle(new LineStyle(Color.BLACK, 10));
			table.addHeaderRow(headerRow);
	
			Row<PDPage> row = table.createRow(20);
			cell = row.createCell(30, "black left plain");
			cell.setFontSize(15);
			cell = row.createCell(70, "black left bold");
			cell.setFontSize(15);
			cell.setFont(fontBold);
	
			row = table.createRow(20);
			cell = row.createCell(50, "red right mono");
			cell.setTextColor(Color.RED);
			cell.setFontSize(15);
			cell.setFont(fontMono);
			// horizontal alignment
			cell.setAlign(HorizontalAlignment.RIGHT);
			cell.setBottomBorderStyle(new LineStyle(Color.RED, 5));
			cell = row.createCell(50, "green centered italic");
			cell.setTextColor(Color.GREEN);
			cell.setFontSize(15);
			cell.setFont(fontItalic);
			cell.setAlign(HorizontalAlignment.CENTER);
			cell.setBottomBorderStyle(new LineStyle(Color.GREEN, 5));
	
			row = table.createRow(20);
			cell = row.createCell(40, "rotated");
			cell.setFontSize(15);
			// rotate the text
			cell.setTextRotated(true);
			cell.setAlign(HorizontalAlignment.RIGHT);
			cell.setValign(VerticalAlignment.MIDDLE);
			// long text that wraps
			cell = row.createCell(30, "long text long text long text long text long text long text long text");
			cell.setFontSize(12);
			// long text that wraps, with more line spacing
			cell = row.createCell(30, "long text long text long text long text long text long text long text");
			cell.setFontSize(12);
			cell.setLineSpacing(2);
	
			table.draw();
	
			float tableHeight = table.getHeaderAndDataHeight();
			
			contentStream.close();
 
			document.save("C:/Users/pc/Documents/Itson/pdfBoxHelloWorld.pdf");
			document.close();
			Path pdfPath = Paths.get("C:/Users/pc/Documents/Itson/pdfBoxHelloWorld.pdf");
			byte[] pdf = Files.readAllBytes(pdfPath);
			
			
			String str = "guru";
			String b64 = Base64.getEncoder().encodeToString(pdf);
			
			lstResultado.add(b64);
		}catch(Exception e){
			lstResultado.add(e.toString());
		}
		
		
		//System.out.println(b64);  str.getBytes()
		//-> "Z3VydQ=="
		
		
		resultado.setData(lstResultado);
		resultado.setSuccess(true);
		return resultado;
	}
	
	public Result base64Decode(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		List<String> lstResultado = new ArrayList<String>();
		String b64 = "Z3VydQ==";
		byte[] decoder = Base64.getDecoder().decode(b64);
		//String str = new String(decoder);
		
		
		lstResultado.add(b64);
		resultado.setData(lstResultado);
		resultado.setSuccess(true);
		return resultado;
		
	}
}
