package com.opplus.utilidades;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public final class Utilidades {

	private Utilidades() {
	}

	public static double suma(double sum1, double sum2) {
		return sum1 + sum2;
	}

	public static byte[] exportPDF(String textJson) {
		PDDocument doc = null;
		PDPage page = new PDPage();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		List<String> jsonList = Arrays.asList(gson.toJson(new JsonParser().parse(textJson)).split("\n"));

		try {
			doc = new PDDocument();
			doc.addPage(page);
			PDPageContentStream contentStream = new PDPageContentStream(doc, page);

			PDFont pdfFont = PDType1Font.COURIER;
			float fontSize = 10;
			float leading = 1.0f * fontSize;

			PDRectangle mediabox = page.getMediaBox();
			float margin = 10;
			float startX = mediabox.getLowerLeftX() + margin;
			float startY = mediabox.getUpperRightY() - margin;

			contentStream.beginText();
			contentStream.setFont(pdfFont, fontSize);
			contentStream.newLineAtOffset(startX, startY);
			jsonList.stream().forEach((linePage) -> {
				System.out.println(linePage);
				try {
					contentStream.showText(linePage);
					contentStream.newLineAtOffset(0, -leading);
				} catch (IOException e) {
					System.out.println("Se ha producido un error");
					e.printStackTrace();
					return;
				}
			});
			contentStream.endText();
			contentStream.close();
			doc.save(baos);		
			doc.close();
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
