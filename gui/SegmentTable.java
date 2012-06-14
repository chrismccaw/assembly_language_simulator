package gui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * A customized JTable has a set font size and column width. This class allows
 * the highlighting of specific rows
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class SegmentTable extends JTable {

	public SegmentTable(DefaultTableModel model, ArrayList<String> columns) {

		setModel(model);
		setPreferredScrollableViewportSize(getPreferredSize());
		setShowHorizontalLines(false);
		setShowVerticalLines(false);

		for (String s : columns) {
			model.addColumn(s);

		}
		setRowHeight(25);
		setCellSelectionEnabled(false);
		setFocusable(true);
		setFont(new Font("Arial", Font.BOLD, 14));
		setSelectionBackground(new Color(163, 204, 239));
		setColumnSelectionAllowed(true);
		setRowSelectionAllowed(true);
	}

	public String[] getSelectionRows() {
		String[] rows = new String[getRowCount()];
		for (int i = 0; i < getRowCount(); i++) {
			rows[i] = (String) getValueAt(i, 1);
		}

		return rows;
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param tableRow
	 *            - The table row to be highlight, an integer
	 */
	public void highlightRow(int tableRow) {
		changeSelection(tableRow, 1, false, false);
		repaint();
		revalidate();
	}

	public void addTableListener(ListSelectionListener window) {
		getSelectionModel().addListSelectionListener(window);
	}

}
