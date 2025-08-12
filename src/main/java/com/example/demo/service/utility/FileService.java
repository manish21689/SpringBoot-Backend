package com.example.demo.service.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Worker;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class FileService {
	@Autowired
	private JavaMailSender javaMailSender;

	public void generateExcel(Worker worker) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Worker Report");
			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("ID");
			header.createCell(1).setCellValue("Name");
			header.createCell(2).setCellValue("Email");

			Row dataRow = sheet.createRow(1);
			dataRow.createCell(0).setCellValue(worker.getId());
			dataRow.createCell(1).setCellValue(worker.getName());
			dataRow.createCell(2).setCellValue(worker.getEmail());

			String filename = "File_" + worker.getId() + "_" + System.currentTimeMillis() + ".xlsx";
			Path path = Paths.get("generated", filename);
			Files.createDirectories(path.getParent());
			try (OutputStream os = Files.newOutputStream(path)) {
				workbook.write(os);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generatePdf(Worker worker) {
		try {
			String filename = "File_" + worker.getId() + "_" + System.currentTimeMillis() + ".pdf";
			Path path = Paths.get("generated", filename);
			Files.createDirectories(path.getParent());

			PdfWriter writer = new PdfWriter(path.toString());
			PdfDocument pdfDoc = new PdfDocument(writer);
			Document document = new Document(pdfDoc);

			document.add(new Paragraph("Worker Report"));
			document.add(new Paragraph("ID: " + worker.getId()));
			document.add(new Paragraph("Name: " + worker.getName()));
			document.add(new Paragraph("Email: " + worker.getEmail()));

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File zipFiles(Long workerId) {
		File dir = new File("generated");
		File[] matchingFiles = dir.listFiles((d, name) -> name.contains("File_" + workerId));
		String zipFileName = "generated/Report_" + workerId + ".zip";
		try (FileOutputStream fos = new FileOutputStream(zipFileName); ZipOutputStream zos = new ZipOutputStream(fos)) {
			for (File file : matchingFiles) {
				ZipEntry entry = new ZipEntry(file.getName());
				zos.putNextEntry(entry);
				Files.copy(file.toPath(), zos);
				zos.closeEntry();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new File(zipFileName);
	}

	public void sendReportEmail(Worker worker, File zipFile) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(worker.getEmail());
			helper.setSubject("Report for date: " + LocalDate.now());
			helper.setText("Dear " + worker.getName() + ",\n\nPlease find the report attached.\n\nThanks.");
			FileSystemResource file = new FileSystemResource(zipFile);
			helper.addAttachment(zipFile.getName(), file);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
