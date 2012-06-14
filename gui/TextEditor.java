package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * A customized text editor that displays numbered lines and allows the
 * highlighting of words within the Text Pane
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class TextEditor extends JTextPane {

	StyledDocument doc;
	Style style;

	public TextEditor() {
		super();
		setEditorKit(new NumberedEditorKit());

		doc = (StyledDocument) getDocument();

		style = doc.addStyle("StyleName", null);
		setFont(new Font("Helvetica", Font.BOLD, 16));

	}

	Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(
			new Color(255, 204, 102));

	// A private subclass of the default highlight painter
	class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
		public MyHighlightPainter(Color color) {
			super(color);
		}
	}

	/**
	 * Highlights the word passed into this method
	 * 
	 * @param pattern
	 *            the string that you want to be highlighted in text area
	 */
	public void findAndHighlight(String pattern, int line) {
		// First remove all old highlights
		Highlighter removeHilite = getHighlighter();
		Highlighter.Highlight[] hilites = removeHilite.getHighlights();

		for (int i = 0; i < hilites.length; i++) {
			if (hilites[i].getPainter() instanceof MyHighlightPainter) {
				removeHilite.removeHighlight(hilites[i]);
			}
		}

		try {
			Highlighter hilite = getHighlighter();
			Document doc = getDocument();
			String text = doc.getText(0, doc.getLength());

			int start = 0;
			int lineNum = 1;
			String lineText = "";
			int charCount = 0;
			for(int i = 0; i < doc.getLength(); i++){
				
				if(doc.getText(i, 1).equals("\n")){
					lineText = doc.getText(start, charCount);
					
					charCount = 1;
					if(lineNum == line)
						break;
					++lineNum;
					start = i;
					
				}
				 ++charCount;
			}
		int pos = lineText.indexOf(pattern, 0);
				hilite.addHighlight(start+pos, (pos+start) + pattern.length(),
						myHighlightPainter);
			
		} catch (BadLocationException e) {
		}
	}

	/**
	 * Adds the string to the end of the text Area
	 * 
	 * @param append
	 *            the string to append at the end of the text area
	 */
	public void appendString(String append) {
		try {
			if (append.trim().equals("INCOMPLETE!!")) {
				StyleConstants.setForeground(style, Color.red);
			} else {
				StyleConstants.setForeground(style, Color.black);
			}
			StyleConstants.setLeftIndent(style, 20f);
			doc.insertString(doc.getLength(), "     " + append, style);

		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class NumberedEditorKit extends StyledEditorKit {

	@Override
	public ViewFactory getViewFactory() {
		return new NumberedViewFactory();
	}
}

class NumberedViewFactory implements ViewFactory {
	@Override
	public View create(Element elem) {
		String kind = elem.getName();
		if (kind != null)
			if (kind.equals(AbstractDocument.ContentElementName)) {
				return new LabelView(elem);
			} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
				return new NumberedParagraphView(elem);
			} else if (kind.equals(AbstractDocument.SectionElementName)) {
				return new BoxView(elem, View.Y_AXIS);
			} else if (kind.equals(StyleConstants.ComponentElementName)) {
				return new ComponentView(elem);
			} else if (kind.equals(StyleConstants.IconElementName)) {
				return new IconView(elem);
			}
		return new LabelView(elem);
	}
}

class NumberedParagraphView extends ParagraphView {
	public static short NUMBERS_WIDTH = 20;

	public NumberedParagraphView(Element e) {
		super(e);
		short top = 0;
		short left = 0;
		short bottom = 0;
		short right = 0;
		this.setInsets(top, left, bottom, right);
	}

	@Override
	protected void setInsets(short top, short left, short bottom, short right) {
		super.setInsets(top, (short) (left + NUMBERS_WIDTH), bottom, right);
	}

	@Override
	public void paintChild(Graphics g, Rectangle r, int n) {
		super.paintChild(g, r, n);
		int previousLineCount = getPreviousLineCount();
		int numberX = r.x - getLeftInset();
		int numberY = r.y + r.height - 4;
		g.setColor(Color.blue);
		g.drawString(Integer.toString(previousLineCount + n + 1), numberX,
				numberY);
	}

	public int getPreviousLineCount() {
		int lineCount = 0;
		View parent = this.getParent();
		int count = parent.getViewCount();
		for (int i = 0; i < count; i++) {
			if (parent.getView(i) == this) {
				break;
			} else {
				lineCount += parent.getView(i).getViewCount();
			}
		}
		return lineCount;
	}
}
