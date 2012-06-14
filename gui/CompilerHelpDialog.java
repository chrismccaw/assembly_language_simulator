package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

/**
 * This dialog contains information on how the compiler works. The text that is
 * display is retrieved from help/CompilerHelp.txt
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class CompilerHelpDialog extends JFrame {
	public CompilerHelpDialog() {
		super("Compiler Help");

		setLayout(new GridBagLayout());
		JTextPane textArea = new JTextPane();
		textArea.setContentType("text/html");
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(500, 350));
		scrollPane.setWheelScrollingEnabled(true);
		// Add Components to this panel.
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.HORIZONTAL;

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		JButton ok = new JButton("Exit");
		textArea.setText(helpContent());
		JLabel title = new JLabel("How to use the Compiler");
		title.setFont(new Font("Helvetica", Font.BOLD, 16));

		getContentPane().add(title, c);
		getContentPane().add(scrollPane, c);
		getContentPane().add(ok, c);

		setSize(500, 350);

		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		});

		pack();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public String helpContent() {

		ClassLoader CLDR = this.getClass().getClassLoader();
		InputStream inputStream = CLDR
				.getResourceAsStream("help/CompilerHelp.html");
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(isr);

		String content = "";
		String readLine = "";
		try {
			while ((readLine = br.readLine()) != null) {
				// Print the content on the console
				content += readLine + "\n";
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return content;
	}

	public static void main(String[] args) {
		CompilerHelpDialog f = new CompilerHelpDialog();
		f.setVisible(true);
		f.pack();
	}
}
