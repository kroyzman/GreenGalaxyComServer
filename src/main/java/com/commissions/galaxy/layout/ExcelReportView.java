package com.commissions.galaxy.layout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.commissions.galaxy.logic.Deal_DealerCommission;
import com.commissions.galaxy.logic.Dealer;

public class ExcelReportView {

	public static ByteArrayInputStream customersToExcel(Dealer dealer, List<Deal_DealerCommission> commissions,
			List<Deal_DealerCommission> directCommissions, List<Deal_DealerCommission> derivedCommissions,
			List<Deal_DealerCommission> partnerCommissions, String currency) throws IOException {
		final String USD = "USD";
		final String ILS = "ILS";
		final double VAT = 1.17;
		final int AUTO_SIZE_LENGTH = 11;
		final String[] DIRECT_HEADERS = { "#", "Date", "Customer", "Invoice Number", "Deal Value", "Dealer",
				"Dealer Commission Rate", "Dealer Commission Amount" };
		final String[] DERIVED_HEADERS = { "#", "Date", "Customer", "Invoice Number", "Deal Value", "Dealer",
				"Dealer Commission Rate", "Dealer Commission Amount", "Derived Dealer",
				"Derived Dealer Commission Rate", "Derived Dealer Commission Amount" };
		final String[] PARTNER_HEADERS = { "#", "Date", "Customer", "Invoice Number", "Deal Value", "Deal Cost",
				"Dealer", "Partner", "Partner Commission Rate", "Partner Commission Amount" };

		int rowIndex = 0;
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Commission Report - " + dealer.getName());

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		CellStyle underlineStyle = workbook.createCellStyle();
		underlineStyle.setBorderBottom(BorderStyle.DOUBLE);

		CellStyle bigFontStyle = workbook.createCellStyle();
		underlineStyle.setBorderBottom(BorderStyle.DOUBLE);

		CellStyle formatedStyle = workbook.createCellStyle();
		formatedStyle.setAlignment(HorizontalAlignment.CENTER);
		CreationHelper ch = workbook.getCreationHelper();
		if (currency.equals(USD)) {
			formatedStyle.setDataFormat((short) 7);
		} else {
			formatedStyle.setDataFormat(ch.createDataFormat().getFormat("[$₪-he-IL] #,##0.00;[$₪-he-IL] -#,##0.00"));
		}

		// First Row Commission Report Header
		Row row = sheet.createRow(rowIndex++);
		Cell cell = row.createCell(0);
		cell.setCellValue("Commission Report: " + dealer.getName());
		cell.setCellStyle(cellStyle);

		// Second Row Direct Deals
		rowIndex++;
		row = sheet.createRow(rowIndex);

		cell = row.createCell(0);
		cell.setCellValue("Commissions From Direct Deals");
		cell.setCellStyle(cellStyle);

		rowIndex++;
		row = sheet.createRow(rowIndex);
		int i;
		for (i = 0; i < DIRECT_HEADERS.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(DIRECT_HEADERS[i]);
			cell.setCellStyle(cellStyle);
		}

		if (currency.equals(ILS)) {
			cell = row.createCell(i);
			cell.setCellValue("Dollar Rate");
			cell.setCellStyle(cellStyle);
		}

		int commissionCellIndex = 0, commissionRowIndex = 1;
		for (Deal_DealerCommission directCommission : directCommissions) {
			rowIndex++;
			commissionCellIndex = 0;
			row = sheet.createRow(rowIndex);
			cell = row.createCell(commissionCellIndex);
			cell.setCellValue(commissionRowIndex++);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(++commissionCellIndex);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String date = df.format(directCommission.getDeal().getDate());
			cell.setCellValue(date);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(++commissionCellIndex);
			cell.setCellValue(directCommission.getDeal().getCustomer());
			cell.setCellStyle(cellStyle);
			cell = row.createCell(++commissionCellIndex);
			cell.setCellValue(directCommission.getDeal().getInvoiceNumber());
			cell.setCellStyle(cellStyle);
			cell = row.createCell(++commissionCellIndex);
			cell.setCellValue(currencySwitchForAmount(directCommission.getDeal().getDealValue(),
					directCommission.getDeal().getCurrentDollarValue(), currency));
			cell.setCellStyle(formatedStyle);
			cell = row.createCell(++commissionCellIndex);
			cell.setCellValue(directCommission.getDealer().getName());
			cell.setCellStyle(cellStyle);
			cell = row.createCell(++commissionCellIndex);
			cell.setCellValue(directCommission.getDealerCommissionRate() + "%");
			cell.setCellStyle(cellStyle);
			cell = row.createCell(++commissionCellIndex);
			cell.setCellValue(currencySwitchForAmount(directCommission.getDealCommissionAmountForDealer(),
					directCommission.getDeal().getCurrentDollarValue(), currency));
			cell.setCellStyle(formatedStyle);
			if (currency.equals(ILS)) {
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(directCommission.getDeal().getCurrentDollarValue());
				cell.setCellStyle(cellStyle);
			}
		}

		// Sum Derived
		rowIndex++;
		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Sum Direct");
		cell.setCellStyle(underlineStyle);
		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Num Of Direct Deals:");
		cell = row.createCell(1);
		cell.setCellValue(directCommissions.size());
		cell.setCellStyle(cellStyle);
		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Total Direct Deals Amount:");
		cell = row.createCell(1);
		cell.setCellValue(sumDealAmount(directCommissions, currency));
		cell.setCellStyle(formatedStyle);
		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Direct Deals Total Commission Amount:");
		cell = row.createCell(1);
		cell.setCellValue(sumCommissionsAmount(directCommissions, currency));
		cell.setCellStyle(formatedStyle);

		// Third Row Derived Deals
		if (!derivedCommissions.isEmpty()) {

			rowIndex++;
			rowIndex++;
			row = sheet.createRow(rowIndex);

			cell = row.createCell(0);
			cell.setCellValue("Commissions From Derived Deals");
			cell.setCellStyle(cellStyle);

			rowIndex++;
			row = sheet.createRow(rowIndex);
			for (i = 0; i < DERIVED_HEADERS.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(DERIVED_HEADERS[i]);
				cell.setCellStyle(cellStyle);
			}

			if (currency.equals(ILS)) {
				cell = row.createCell(i);
				cell.setCellValue("Dollar Rate");
				cell.setCellStyle(cellStyle);
			}

			commissionCellIndex = 0;
			commissionRowIndex = 1;
			for (Deal_DealerCommission derivedCommission : derivedCommissions) {
				rowIndex++;
				commissionCellIndex = 0;
				row = sheet.createRow(rowIndex);
				cell = row.createCell(commissionCellIndex);
				cell.setCellValue(commissionRowIndex++);
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				String date = df.format(derivedCommission.getDeal().getDate());
				cell.setCellValue(date);
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(derivedCommission.getDeal().getCustomer());
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(derivedCommission.getDeal().getInvoiceNumber());
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(currencySwitchForAmount(derivedCommission.getDeal().getDealValue(),
						derivedCommission.getDeal().getCurrentDollarValue(), currency));
				cell.setCellStyle(formatedStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(derivedCommission.getDeal().getDealer().getName());
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(derivedCommission.getDeal().getDealer().getBaseCommissionRate() + "%");
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(currencySwitchForAmount(
						derivedCommission.getDeal().getDealer().getBaseCommissionRate() / 100
								* derivedCommission.getDeal().getDealValue(),
						derivedCommission.getDeal().getCurrentDollarValue(), currency));
				cell.setCellStyle(formatedStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(derivedCommission.getDealer().getName());
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(derivedCommission.getDealerCommissionRate() + "%");
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(currencySwitchForAmount(derivedCommission.getDealCommissionAmountForDealer(),
						derivedCommission.getDeal().getCurrentDollarValue(), currency));
				cell.setCellStyle(formatedStyle);

				if (currency.equals(ILS)) {
					cell = row.createCell(++commissionCellIndex);
					cell.setCellValue(derivedCommission.getDeal().getCurrentDollarValue());
					cell.setCellStyle(cellStyle);
				}
			}

			// Sum Derived
			rowIndex++;
			rowIndex++;
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue("Sum Derived");
			cell.setCellStyle(underlineStyle);
			rowIndex++;
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue("Num Of Derived Deals:");
			cell = row.createCell(1);
			cell.setCellValue(derivedCommissions.size());
			cell.setCellStyle(cellStyle);
			rowIndex++;
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue("Total Derived Deals Amount:");
			cell = row.createCell(1);
			cell.setCellValue(sumDealAmount(derivedCommissions, currency));
			cell.setCellStyle(formatedStyle);
			rowIndex++;
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue("Derived Deals Total Commission Amount:");
			cell = row.createCell(1);
			cell.setCellValue(sumCommissionsAmount(derivedCommissions, currency));
			cell.setCellStyle(formatedStyle);
		}

		// Forth Row Partner Deals
		if (!partnerCommissions.isEmpty()) {
			rowIndex++;
			rowIndex++;
			row = sheet.createRow(rowIndex);

			cell = row.createCell(0);
			cell.setCellValue("Commissions From Partnership Deals");
			cell.setCellStyle(cellStyle);

			rowIndex++;
			row = sheet.createRow(rowIndex);
			for (i = 0; i < PARTNER_HEADERS.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(PARTNER_HEADERS[i]);
				cell.setCellStyle(cellStyle);
			}

			if (currency.equals(ILS)) {
				cell = row.createCell(i);
				cell.setCellValue("Dollar Rate");
				cell.setCellStyle(cellStyle);
			}

			commissionCellIndex = 0;
			commissionRowIndex = 1;
			for (Deal_DealerCommission partnerCommission : partnerCommissions) {
				rowIndex++;
				commissionCellIndex = 0;
				row = sheet.createRow(rowIndex);
				cell = row.createCell(commissionCellIndex);
				cell.setCellValue(commissionRowIndex++);
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				String date = df.format(partnerCommission.getDeal().getDate());
				cell.setCellValue(date);
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(partnerCommission.getDeal().getCustomer());
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(partnerCommission.getDeal().getInvoiceNumber());
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(currencySwitchForAmount(partnerCommission.getDeal().getDealValue(),
						partnerCommission.getDeal().getCurrentDollarValue(), currency));
				cell.setCellStyle(formatedStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(currencySwitchForAmount(partnerCommission.getDeal().getDealCost(),
						partnerCommission.getDeal().getCurrentDollarValue(), currency));
				cell.setCellStyle(formatedStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(partnerCommission.getDeal().getDealer().getName());
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(partnerCommission.getDealer().getName());
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(partnerCommission.getDealerCommissionRate() + "%");
				cell.setCellStyle(cellStyle);
				cell = row.createCell(++commissionCellIndex);
				cell.setCellValue(currencySwitchForAmount(partnerCommission.getDealCommissionAmountForDealer(),
						partnerCommission.getDeal().getCurrentDollarValue(), currency));
				cell.setCellStyle(formatedStyle);

				if (currency.equals(ILS)) {
					cell = row.createCell(++commissionCellIndex);
					cell.setCellValue(partnerCommission.getDeal().getCurrentDollarValue());
					cell.setCellStyle(cellStyle);
				}
			}

			// Sum Derived
			rowIndex++;
			rowIndex++;
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue("Sum Partnership");
			cell.setCellStyle(underlineStyle);
			rowIndex++;
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue("Num Of Partnership Deals:");
			cell = row.createCell(1);
			cell.setCellValue(partnerCommissions.size());
			cell.setCellStyle(cellStyle);
			rowIndex++;
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue("Total Partnership Deals Amount:");
			cell = row.createCell(1);
			cell.setCellValue(sumDealAmount(partnerCommissions, currency));
			cell.setCellStyle(formatedStyle);
			rowIndex++;
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue("Partnership Deals Total Commission Amount:");
			cell = row.createCell(1);
			cell.setCellValue(sumCommissionsAmount(partnerCommissions, currency));
			cell.setCellStyle(formatedStyle);
		}

		// Total
		rowIndex++;
		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Total Sum");
		cell.setCellStyle(underlineStyle);
		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Total Commission Amount:");
		cell = row.createCell(1);
		cell.setCellValue(sumCommissionsAmount(commissions, currency));
		cell.setCellStyle(formatedStyle);
		rowIndex++;
		if (currency.equals(ILS)) {
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue("Total Commission Amount VAT include:");
			cell = row.createCell(1);
			cell.setCellValue(sumCommissionsAmount(commissions, currency) * VAT);
			cell.setCellStyle(formatedStyle);
			rowIndex++;
		}
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Num Of Deals:");
		cell = row.createCell(1);
		cell.setCellValue(commissions.size());
		cell.setCellStyle(cellStyle);
		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Total Deals Amount:");
		cell = row.createCell(1);
		cell.setCellValue(sumDealAmount(commissions, currency));
		cell.setCellStyle(formatedStyle);

		// Auto Size Column
		for (i = 0; i < AUTO_SIZE_LENGTH; i++) {
			sheet.autoSizeColumn(i);
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);

		return new ByteArrayInputStream(outputStream.toByteArray());

	}

	private static double currencySwitchForAmount(double amount, double dollarValue, String currency) {
		final String USD = "USD";
		final String ILS = "ILS";
		if (currency.equals(ILS)) {
			return amount * dollarValue;
		}
		return amount;
	}

	private static double sumCommissionsAmount(List<Deal_DealerCommission> commissions, String currency) {
		final String USD = "USD";
		final String ILS = "ILS";
		double commissionSum = 0;

		for (Deal_DealerCommission commission : commissions) {
			if (currency.equals(USD)) {
				commissionSum += commission.getDealCommissionAmountForDealer();
			} else {
				commissionSum += commission.getDealCommissionAmountForDealer()
						* commission.getDeal().getCurrentDollarValue();
			}

		}
		return commissionSum;
	}

	private static double sumDealAmount(List<Deal_DealerCommission> commissions, String currency) {
		final String USD = "USD";
		final String ILS = "ILS";
		double dealSum = 0;

		for (Deal_DealerCommission commission : commissions) {
			if (currency.equals(USD)) {
				dealSum += commission.getDeal().getDealValue();
			} else {
				dealSum += commission.getDeal().getDealValue() * commission.getDeal().getCurrentDollarValue();
			}

		}
		return dealSum;
	}

}
