/*
* AMRIT – Accessible Medical Records via Integrated Technology
* Integrated EHR (Electronic Health Records) Solution
*
* Copyright (C) "Piramal Swasthya Management and Research Institute"
*
* This file is part of AMRIT.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.wipro.fhir.utils.pdf;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Small top-to-bottom text writer over PDFBox: tracks the cursor, wraps long
 * lines and starts new pages as needed, so callers just emit headings, key/value
 * pairs and bullet lists.
 */
public class PdfWriter implements AutoCloseable {

	private static final PDRectangle PAGE_SIZE = PDRectangle.A4;
	private static final float MARGIN = 50f;
	private static final float BOTTOM_LIMIT = 60f;
	private static final float LINE_GAP = 4f;

	private static final PDFont REGULAR = PDType1Font.HELVETICA;
	private static final PDFont BOLD = PDType1Font.HELVETICA_BOLD;

	private final PDDocument document;
	private PDPageContentStream content;
	private float cursorY;

	public PdfWriter(PDDocument document) throws IOException {
		this.document = document;
		newPage();
	}

	private void newPage() throws IOException {
		if (content != null) {
			content.close();
		}
		PDPage page = new PDPage(PAGE_SIZE);
		document.addPage(page);
		content = new PDPageContentStream(document, page);
		cursorY = PAGE_SIZE.getHeight() - MARGIN;
	}

	public void heading(String text) throws IOException {
		write(text, BOLD, 15f);
		gap(6f);
	}

	public void title(String text) throws IOException {
		write(text, BOLD, 12f);
		gap(2f);
	}

	public void section(String text) throws IOException {
		gap(10f);
		write(text, BOLD, 11f);
		gap(2f);
	}

	public void keyValue(String key, String value) throws IOException {
		if (value == null || value.trim().isEmpty()) {
			return;
		}
		write(key + ": " + value, REGULAR, 10f);
	}

	/** Emits a section with bullet lines, or "Not recorded" when there are none. */
	public void bulletSection(String heading, List<String> lines) throws IOException {
		section(heading);
		if (lines == null || lines.isEmpty()) {
			write("Not recorded", REGULAR, 10f);
			return;
		}
		for (String line : lines) {
			if (line != null && !line.trim().isEmpty()) {
				write("- " + line.trim(), REGULAR, 10f);
			}
		}
	}

	public void footNote(String text) throws IOException {
		gap(14f);
		write(text, REGULAR, 8f);
	}

	private void gap(float amount) {
		cursorY -= amount;
	}

	private void write(String rawText, PDFont font, float fontSize) throws IOException {
		float maxWidth = PAGE_SIZE.getWidth() - (2 * MARGIN);
		for (String line : wrap(sanitize(rawText), font, fontSize, maxWidth)) {
			if (cursorY <= BOTTOM_LIMIT) {
				newPage();
			}
			content.beginText();
			content.setFont(font, fontSize);
			content.newLineAtOffset(MARGIN, cursorY);
			content.showText(line);
			content.endText();
			cursorY -= fontSize + LINE_GAP;
		}
	}

	/**
	 * The standard PDF fonts use WinAnsi encoding, so any character outside it —
	 * Devanagari or Tamil names, curly quotes, em dashes — makes showText throw and
	 * would lose the whole PDF. Replace what cannot be drawn instead.
	 */
	private String sanitize(String text) {
		if (text == null) {
			return "";
		}
		String normalized = text.replace('—', '-').replace('–', '-').replace('‘', '\'')
				.replace('’', '\'').replace('“', '"').replace('”', '"');
		StringBuilder sb = new StringBuilder(normalized.length());
		for (char c : normalized.toCharArray()) {
			if (c == '\r' || c == '\n' || c == '\t') {
				sb.append(' ');
			} else if (c >= 32 && c <= 255) {
				sb.append(c);
			} else {
				sb.append('?');
			}
		}
		return sb.toString();
	}

	private List<String> wrap(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
		List<String> lines = new java.util.ArrayList<>();
		if (text.isEmpty()) {
			lines.add("");
			return lines;
		}
		StringBuilder current = new StringBuilder();
		for (String word : text.split(" ")) {
			String candidate = current.length() == 0 ? word : current + " " + word;
			if (widthOf(candidate, font, fontSize) <= maxWidth) {
				current.setLength(0);
				current.append(candidate);
				continue;
			}
			if (current.length() > 0) {
				lines.add(current.toString());
				current.setLength(0);
			}
			// A single word longer than the line has to be split character-wise.
			if (widthOf(word, font, fontSize) > maxWidth) {
				StringBuilder chunk = new StringBuilder();
				for (char c : word.toCharArray()) {
					if (widthOf(chunk.toString() + c, font, fontSize) > maxWidth) {
						lines.add(chunk.toString());
						chunk.setLength(0);
					}
					chunk.append(c);
				}
				current.append(chunk);
			} else {
				current.append(word);
			}
		}
		if (current.length() > 0) {
			lines.add(current.toString());
		}
		return lines;
	}

	private float widthOf(String text, PDFont font, float fontSize) throws IOException {
		return font.getStringWidth(text) / 1000 * fontSize;
	}

	@Override
	public void close() throws IOException {
		if (content != null) {
			content.close();
			content = null;
		}
	}
}
