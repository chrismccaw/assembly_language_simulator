package gui;

import high_level_parser.HighLevelCompiler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * This JFrame it used to display the compiler components. It contains a Text
 * Editor to write programs JButton to compile the code and a button to display
 * the help dialog A checkbox to remove the assessment Drop Down Menu to display
 * high level templates
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class HighLevelGUI extends JFrame implements WindowListener,
		ComponentListener {

	private JPanel tools;
	private JPanel editor;
	TextEditor textEditor;
	private SimulatorView view;
	private HighLevelCompiler compiler;
	private JCheckBox noAssessment;
	private JButton compile;
	private JButton help;
	private JComboBox samplePrograms;
	private CompilerHelpDialog compilerHelp;

	public HighLevelGUI(SimulatorView view) {
		super("High-Level Language");
		this.view = view;
		compilerHelp = new CompilerHelpDialog();
		setSize(new Dimension(400, 500));
		noAssessment = new JCheckBox("Remove Assessment");
		setLayout(new BorderLayout());
		compile = new JButton("Compile");
		compile.setFont(new Font("Helvetica", Font.BOLD, 14));
		compile.setForeground(new Color(27, 84, 16));
		compile.setPreferredSize(new Dimension(100, 40));
		add(toolsPanel(), BorderLayout.NORTH);
		add(editorPanel(), BorderLayout.CENTER);
		add(compile, BorderLayout.SOUTH);

		view.addWindowListener(this);
		view.addComponentListener(this);
		addWindowListener(this);
		addComponentListener(this);

		setVisible(true);
		setResizable(false);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	 String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
	      URL dirURL = clazz.getClassLoader().getResource(path);
	      if (dirURL != null && dirURL.getProtocol().equals("file")) {
	        return new File(dirURL.toURI()).list();
	      } 

	      if (dirURL == null) {
	        /* 
	         * In case of a jar file, we can't actually find a directory.
	         * Have to assume the same jar as clazz.
	         */
	        String me = clazz.getName().replace(".", "/")+".class";
	        dirURL = clazz.getClassLoader().getResource(me);
	      }
	      
	      if (dirURL.getProtocol().equals("jar")) {
	        /* A JAR path */
	        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
	        JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
	        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
	        Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
	        while(entries.hasMoreElements()) {
	          String name = entries.nextElement().getName();
	          if (name.startsWith(path)) { //filter according to the path
	            String entry = name.substring(path.length());
	            int checkSubdir = entry.indexOf("/");
	            if (checkSubdir >= 0) {
	              // if it is a subdirectory, we just return the directory name
	              entry = entry.substring(0, checkSubdir);
	            }
	            result.add(entry);
	          }
	        }
	        return result.toArray(new String[result.size()]);
	      } 
	        
	      throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
	  }

	/**
	 * Creates a Tool Bar Area for the user to interact with
	 * 
	 * @return returns a JPanel that contains the help button, combo box and
	 *         high level templates
	 */
	public JPanel toolsPanel() {
		tools = new JPanel();
		help = new JButton("Help");
		compile.addActionListener(new compilerListener());
		help.addActionListener(new HelpListener());
		tools.add(help);
		try{
		int index = 0;
		String[] files = getResourceListing(HighLevelGUI.class, "gui/sample_programs/");
		String[] programs = new String[files.length];
		for (String name : files) {
			programs[index] = name.replace(".txt", "")
					.replace("_", " ");
			index++;
		}
		samplePrograms = new JComboBox(programs);
		samplePrograms.addActionListener(new comboChangeListener());
		tools.add(samplePrograms);
		tools.add(noAssessment);
		
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error");
		}

		return tools;
	}

	/**
	 * Creates the text area for users to enter their high level programs into
	 * 
	 * @return returns a JPanel
	 */
	public JPanel editorPanel() {
		editor = new JPanel();
		editor.setPreferredSize(new Dimension(400, 430));

		textEditor = new TextEditor();
		JScrollPane scroll = new JScrollPane(textEditor);

		scroll.setPreferredSize(new Dimension(400, 390));
		editor.add(scroll);
		return editor;
	}

	/**
	 * Returns the state of the button. True if the assessment check box is
	 * selected, false if it's not.
	 * 
	 * @return true the assess check box is selected, otherwise false
	 */
	public boolean canAssess() {
		return !noAssessment.isSelected();
	}

	/**
	 * Sends the text in the Text Editor to the compiler Displays error dialog
	 * if there is an error. If the compile is successful the the tab is moved
	 * to the segment tab
	 * 
	 */
	public void compileProgram() {
		StringBuffer program = new StringBuffer(textEditor.getText());
		compiler = new HighLevelCompiler(program);
		view.assemblyTextEditor().setText("");

		if (!compiler.getCompiler().getError()
				&& compiler.compiledInstructions().size() != 0) {
			addInstructionsToAssembler();
			view.getTabs().setSelectedIndex(0);
			compiler.getCompiler().reset();
		} else {
			textEditor.findAndHighlight(compiler.getCompiler().errorPointStr(), 1);

			JOptionPane.showMessageDialog(this, "There is an error at "
					+ compiler.getCompiler().errorPointStr(), "Compiler Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}

	/**
	 * This passes the assembly instructions that are generated from the
	 * compiler to the assembly text editor. If the assessment box is selected
	 * then some assembly lines are missed out, otherwise display all the code
	 * 
	 */
	public void addInstructionsToAssembler() {
		String lineSep = System.getProperty("line.separator");
		int size = compiler.compiledInstructions().size();
		Random r = new Random();
		int randomIncompletes = r.nextInt(size / 2) + 1;
		Integer[] array = new Integer[randomIncompletes];

		for (int j = 0; j < randomIncompletes; j++) {
			int randomLine = r.nextInt(size);
			array[j] = randomLine;

		}
		view.getAssignmentFillIn().reset();
		for (int i = 0; i < size; i++) {
			if (inArray(array, i) && canAssess()) {
				view.getAssignmentFillIn().addAnswer(
						compiler.compiledInstructions().get(i).assembly, i);
				view.assemblyTextEditor().appendString("INCOMPLETE!!");
			} else {
				view.assemblyTextEditor().appendString(
						compiler.compiledInstructions().get(i).assembly);
			}

			if (i != (size - 1))
				view.assemblyTextEditor().appendString(lineSep);
		}

	}

	public HighLevelCompiler getCompiler() {
		return compiler;
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param array
	 *            - Array of Integers
	 * @param check
	 *            - the value to check in the array
	 * @return returns true if <code>check</code> is within the array, otherwise
	 *         false
	 */
	public boolean inArray(Integer[] array, int check) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == check) {
				return true;
			}
		}
		return false;
	}

	class compilerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			compileProgram();
		}
	}

	class HelpListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			compilerHelp.setVisible(true);
			compilerHelp.setLocation(new Point(view.getX()
					+ (view.getWidth() / 2 - 50), view.getY()
					+ (view.getHeight() / 2 - 50)));
		}
	}

	/**
	 * Updates the text area with the template that the user selects from the
	 * Combo box
	 * 
	 */
	class comboChangeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String fileName = samplePrograms.getItemAt(samplePrograms
					.getSelectedIndex()) + ".txt";
			fileName = fileName.replaceAll(" ", "_");

			ClassLoader CLDR = this.getClass().getClassLoader();
			InputStream inputStream = CLDR
					.getResourceAsStream("gui/sample_programs/" + fileName);
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
			textEditor.setText(content);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		// setVisible(false);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		setLocation(view.getX() + view.getWidth() + 15, view.getY() + 100);
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

}
