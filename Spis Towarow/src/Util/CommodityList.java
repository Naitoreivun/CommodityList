package Util;

import java.io.*;

import javafx.collections.ObservableList;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import application.model.Jacket;


public class CommodityList 
{
	private static final int AMOUNT_OF_BASIC_ROWS = 5; 
	private int amountOfAdditionalRows;
	private Workbook workbook;
	private Sheet sheet;
	private CellStyle defaultStyle;
	private CellStyle greyStyle;
	private CellStyle signatureStyle;
	private Row titlesRow;
	private ObservableList<Jacket> jackets;
	private boolean additionalTextEnabled;
	private String additionalText;
	
	public CommodityList(ObservableList<Jacket> jackets, String nr,
			String date, File file, String additionalText, boolean additionalTextEnabled)
	{
		workbook = new HSSFWorkbook();
	
		sheet = workbook.createSheet("Spis Towarów");
		
		amountOfAdditionalRows = jackets.size();
		this.jackets = jackets;
		this.additionalTextEnabled = additionalTextEnabled;
		this.additionalText = additionalText;
		
		initColumnSize();
		initDefaultStyle();
		createBasicCells();
		makeHeadLine(nr, date);
		makeTitlesRow();
		makeLastRow();
		makeSignatureFields();
		
		addJackets();
		
		try
		{
			FileOutputStream output = new FileOutputStream(file.getAbsolutePath());
			workbook.write(output);
			output.close();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void initColumnSize()
	{//tak, wiem, liczby z dupy wziête
		sheet.setColumnWidth(0, 4365); //FASON
		sheet.setColumnWidth(1, 4255); //ARTYKU£
		sheet.setColumnWidth(2, 1680); //ILOŒÆ
		sheet.setColumnWidth(3, 4000); //CENA NETTO [Z£]
		for(int i = 4; i <= 12; ++i)
			sheet.setColumnWidth(i, 940); //POSZCZEGÓLNE ROZMIARY
	}
	
	private void initDefaultStyle()
	{
		defaultStyle = workbook.createCellStyle();
		setStyleAlign(defaultStyle);
		setStyleFont(defaultStyle, false, 11);
		setStyleBorder(defaultStyle);
	}
	
	private void setStyleAlign(CellStyle cellStyle)
	{
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	}

	private void setStyleFont(CellStyle cellStyle, boolean bold, int size)
	{
		HSSFFont defaultFont = (HSSFFont) workbook.createFont();
		if(bold)
			defaultFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		defaultFont.setFontName("Calibri");
		defaultFont.setFontHeightInPoints((short) size);
		
		cellStyle.setFont(defaultFont);
	}
	
	private void setStyleBorder(CellStyle cellStyle)
	{
		cellStyle.setBorderBottom(defaultStyle.BORDER_MEDIUM);
		cellStyle.setBorderTop(defaultStyle.BORDER_MEDIUM);
		cellStyle.setBorderRight(defaultStyle.BORDER_MEDIUM);
		cellStyle.setBorderLeft(defaultStyle.BORDER_MEDIUM);
	}

	private void createBasicCells()
	{
		final int amountOfRows = amountOfAdditionalRows + AMOUNT_OF_BASIC_ROWS;
		for(int j = 0; j < amountOfRows; ++j)
		{
			Row row = sheet.createRow(j);
			for(int i = 0; i <= 12; ++i)
			{
				Cell cell = sheet.getRow(j).createCell(i);
				cell.setCellStyle(defaultStyle);
			}
		}		
	}
	
	private void makeHeadLine(String nr, String date)
	{
		sheet.addMergedRegion(new CellRangeAddress(0,1,0,12));
		initHeadCell(nr, date);
	}
	
	private void initHeadCell(String nr, String date)
	{	
		Cell headCell = sheet.getRow(0).createCell(0);

		String headCellText = buildHeadCellText(nr, date);
		
		headCell.setCellValue(headCellText);
		
		CellStyle headCellStyle = workbook.createCellStyle();
		headCellStyle.cloneStyleFrom(defaultStyle);
		setStyleFont(headCellStyle, true, 11);
		
		headCell.setCellStyle(headCellStyle);
	}

	private String buildHeadCellText(String nr, String date)
	{
		String text = "SPIS TOWARÓW DO PRZECHOWANIA NR " + nr;
		if(additionalTextEnabled)
			text = "KONTROLNY " + text + additionalText;
		text += " Z DNIA " + date;
		
		return text;
	}
	
	private void makeTitlesRow()
	{
		final int titlesRowNum = 2;
		titlesRow = sheet.getRow(titlesRowNum);
		makeCutCell();
		makeArticleCell();
		makeNumberCell();
		makeValueCell();
		makeSizesCells();
		setGreyStyle();
	}
	
	private void makeTitleCell(String text, int colNum)
	{
		final int rowNum1 = 2;
		final int rowNum2 = 3;
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum1, rowNum2, colNum, colNum));
		
		Cell cell = titlesRow.getCell(colNum);
		cell.setCellValue(text);
	}
	
	private void makeCutCell()
	{
		makeTitleCell("FASON", 0);
	}

	private void makeArticleCell()
	{
		makeTitleCell("ARTYKU£", 1);
	}
	
	private void makeNumberCell()
	{
		makeTitleCell("ILOŒÆ", 2);
	}
	
	private void makeValueCell()
	{
		makeTitleCell("CENA NETTO [Z£]", 3);
	}
	
	private void makeSizesCells()
	{
		final int mainHeadSizeRowNum = 2;
		final int firstHeadSizeColNum = 4;
		final int lastHeadSizeColNum = 12;
		final int sizesRowNum = 3;
		makeHeadSizeCell(mainHeadSizeRowNum, firstHeadSizeColNum, lastHeadSizeColNum);
		makeSpecificSizeCells(sizesRowNum, firstHeadSizeColNum, lastHeadSizeColNum);
	}

	private void makeHeadSizeCell(int mainHeadSizeRowNum, int firstHeadSizeColNum, int lastHeadSizeColNum)
	{
		sheet.addMergedRegion(new CellRangeAddress(mainHeadSizeRowNum, 
				mainHeadSizeRowNum, firstHeadSizeColNum, lastHeadSizeColNum));
		sheet.getRow(mainHeadSizeRowNum).getCell(firstHeadSizeColNum).setCellValue("ROZMIARY");
	}

	private void makeSpecificSizeCells(int sizesRowNum, int firstHeadSizeColNum, int lastHeadSizeColNum)
	{
		for(int i = firstHeadSizeColNum; i <= lastHeadSizeColNum; ++i)
			sheet.getRow(sizesRowNum).getCell(i).setCellValue(Jacket.SIZES[i - firstHeadSizeColNum]);
	}
		
	private void setGreyStyle()
	{
		initGreyStyle();
		for(int j = 2; j <= 3; ++j)
			for(int i = 0; i <= 12; ++i)
				sheet.getRow(j).getCell(i).setCellStyle(greyStyle);
	}
	
	private void initGreyStyle()
	{
		greyStyle = workbook.createCellStyle();
		greyStyle.cloneStyleFrom(defaultStyle);
		greyStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		greyStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	}

	private void makeLastRow()
	{
		final int lastRowNum = AMOUNT_OF_BASIC_ROWS + amountOfAdditionalRows - 1;
		Row lastRow = sheet.getRow(lastRowNum);
		sheet.addMergedRegion(new CellRangeAddress(
				lastRowNum, lastRowNum, 0, 1));
		
		makeAltogetherCell(lastRow);
		makeSumCell(lastRowNum, lastRow);

		sheet.addMergedRegion(new CellRangeAddress(
				lastRowNum, lastRowNum, 3, 12));
	}

	private void makeAltogetherCell(Row lastRow)
	{
		lastRow.getCell(0).setCellValue("RAZEM");
	}

	private void makeSumCell(int lastRowNum, Row lastRow)
	{
		String strFormula= "SUM(C" + (AMOUNT_OF_BASIC_ROWS) + ":C" + (lastRowNum) + ")";
		Cell sumCell = lastRow.getCell(2);
		sumCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		sumCell.setCellFormula(strFormula);
	}

	private void makeSignatureFields()
	{
		final int space = 2;
		final int signatureRowNum = AMOUNT_OF_BASIC_ROWS + amountOfAdditionalRows + space;
		Row signatureRow = sheet.createRow(signatureRowNum);
		for(int i = 0; i <= 12; ++i)
			signatureRow.createCell(i);
		
		initSignatureStyle();
		makeSignatureField(signatureRowNum, signatureRow, 0, 2, "SK£ADAJ¥CY");
		makeSignatureField(signatureRowNum, signatureRow, 4, 12, "PRZECHOWAWCY");
	}
	
	private void initSignatureStyle()
	{
		signatureStyle = workbook.createCellStyle();
		setStyleAlign(signatureStyle);
		setStyleFont(signatureStyle, false, 12);
	}

	private void makeSignatureField(int signatureRowNum, Row signatureRow, int firstCol, int lastCol, String title)
	{
		sheet.addMergedRegion(new CellRangeAddress(signatureRowNum, signatureRowNum, firstCol, lastCol));
		Cell cell = signatureRow.getCell(firstCol);
		cell.setCellValue(title);
		cell.setCellStyle(signatureStyle);
	}
	
	private void addJackets()
	{
		for(int rowNum = AMOUNT_OF_BASIC_ROWS - 1, jacketNum = 0; jacketNum < jackets.size(); ++rowNum, ++jacketNum)
			addJacket(sheet.getRow(rowNum), jackets.get(jacketNum));
	}

	private void addJacket(Row row, Jacket jacket)
	{
		row.getCell(0).setCellValue(jacket.getCut());
		row.getCell(1).setCellValue(jacket.getArticle());
		row.getCell(2).setCellValue(jacket.getQuantity());
		row.getCell(3).setCellValue(jacket.getValue());
		for(int size : jacket.getSelectedSizesList())
				row.getCell(size + 4).setCellValue(Jacket.SIZES[size]);
	}
}