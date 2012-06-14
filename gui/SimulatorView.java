package gui;

import instructions.ManageInstructions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import assignment.AssignmentFillIn;

import lexical_and_syntax_analysis.SymbolTable;
import lexical_and_syntax_analysis.TokenLabel;
import memory.MemoryStack;
import cpu.Simulator_X86;

/**
 * The view part of the MVC this contains all the display components. The user
 * can interact with the components
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class SimulatorView extends JFrame implements Observer {

	private JPanel registerPanel;
	private JPanel flagPanel;
	private JPanel toolPanel;
	private JPanel feedbackPanel;
	private JPanel memoryPanel;
	private JPanel rfmContainer;
	private JButton clearTool;
	private JButton stepTool;
	private JButton resetTool;
	private JButton runTool;
	private JButton stopToggleTool;
	private JButton zoomInTool;
	private JButton compilerTool;
	private JButton templateTool;
	private JSlider runSpeed;
	private JMenuBar menu;
	private JLabel axRegister;
	private JLabel bxRegister;
	private JLabel cxRegister;
	private JLabel dxRegister;
	private JLabel csRegister;
	private JLabel ipRegister;
	private JLabel dsRegister;
	private JLabel siRegister;
	private JLabel ssRegister;
	private JLabel spRegister;
	private JLabel bpRegister;
	private JLabel sliderValue;
	private JLabel informationLabel;
	private JLabel registerAssignment;
	public JTextField axField;
	public JTextField bxField;
	public JTextField cxField;
	public JTextField dxField;
	public JTextField csField;
	public JTextField ipField;
	public JTextField dsField;
	public JTextField siField;
	public JTextField ssField;
	public JTextField spField;
	public JTextField bpField;
	private JPanel axRegisterHolder;
	private JPanel bxRegisterHolder;
	private JPanel cxRegisterHolder;
	private JPanel dxRegisterHolder;
	private JPanel csRegisterHolder;
	private JPanel ipRegisterHolder;
	private JPanel dsRegisterHolder;
	private JPanel siRegisterHolder;
	private JPanel ssRegisterHolder;
	private JPanel spRegisterHolder;
	private JPanel bpRegisterHolder;
	JTabbedPane viewTabbedPane;
	TextEditor editorPane;
	private DefaultTableModel model;
	private DefaultTableModel dataModel;
	private Simulator_X86 simulator;
	public SegmentTable codeSegment;
	private JMenuItem windowConsoleItem;
	private JMenuItem windowHighLevelItem;
	Color backgroundColor = new Color(144, 202, 253);
	private ArrayList<JTextField> textFieldContainer = new ArrayList<JTextField>();
	private JMenuItem exitItem;
	private JMenuItem aboutItem;
	private DefaultTableModel stackModel;
	private HighLevelGUI highlevelGUI;
	private JMenuItem helpItem;

	public SimulatorView(Simulator_X86 simulator) {
		super("Chris McCaw - Assembly Language Simulator");
		this.simulator = simulator;
		setSize(new Dimension(800, 700));

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setResizable(false);

		registerPanel = new JPanel();
		registerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

		registerPanel.setPreferredSize(new Dimension(110, 300));
		registerPanel.setBackground(backgroundColor);
		registerPanel.setOpaque(false);

		axRegister = new JLabel("AX:");
		bxRegister = new JLabel("BX:");
		cxRegister = new JLabel("CX:");
		dxRegister = new JLabel("DX:");
		csRegister = new JLabel("CS:");
		ipRegister = new JLabel("IP:  ");
		dsRegister = new JLabel("DS:");
		siRegister = new JLabel("SI:  ");
		ssRegister = new JLabel("SS:");
		spRegister = new JLabel("SP:");
		bpRegister = new JLabel("BP:");

		axField = new JTextField(5);
		axField.setName("ax");
		axField.setEditable(false);
		textFieldContainer.add(axField);

		bxField = new JTextField(5);
		bxField.setName("bx");
		bxField.setEditable(false);
		textFieldContainer.add(bxField);

		cxField = new JTextField(5);
		cxField.setName("cx");
		cxField.setEditable(false);
		textFieldContainer.add(cxField);

		dxField = new JTextField(5);
		dxField.setName("dx");
		dxField.setEditable(false);
		textFieldContainer.add(dxField);

		csField = new JTextField(5);
		csField.setEditable(false);

		ipField = new JTextField(5);
		ipField.setEditable(false);

		dsField = new JTextField(5);
		dsField.setEditable(false);

		siField = new JTextField(5);
		siField.setEditable(false);

		ssField = new JTextField(5);
		ssField.setEditable(false);

		spField = new JTextField(5);
		spField.setName("sp");
		spField.setEditable(false);
		textFieldContainer.add(spField);

		bpField = new JTextField(5);
		bpField.setName("bp");
		bpField.setEditable(false);
		textFieldContainer.add(bpField);

		axRegisterHolder = new JPanel();
		axRegisterHolder.add(axRegister);
		axRegisterHolder.add(axField);

		bxRegisterHolder = new JPanel();
		bxRegisterHolder.add(bxRegister);
		bxRegisterHolder.add(bxField);

		cxRegisterHolder = new JPanel();
		cxRegisterHolder.add(cxRegister);
		cxRegisterHolder.add(cxField);

		dxRegisterHolder = new JPanel();
		dxRegisterHolder.add(dxRegister);
		dxRegisterHolder.add(dxField);

		csRegisterHolder = new JPanel();
		ipRegisterHolder = new JPanel();
		dsRegisterHolder = new JPanel();
		siRegisterHolder = new JPanel();
		ssRegisterHolder = new JPanel();
		spRegisterHolder = new JPanel();
		bpRegisterHolder = new JPanel();

		csRegisterHolder.add(csRegister);
		csRegisterHolder.add(csField);

		ipRegisterHolder.add(ipRegister);
		ipRegisterHolder.add(ipField);

		dsRegisterHolder.add(dsRegister);
		dsRegisterHolder.add(dsField);

		ssRegisterHolder.add(ssRegister);
		ssRegisterHolder.add(ssField);

		spRegisterHolder.add(spRegister);
		spRegisterHolder.add(spField);

		bpRegisterHolder.add(bpRegister);
		bpRegisterHolder.add(bpField);

		registerAssignment = new JLabel("");
		registerAssignment.setPreferredSize(new Dimension(100, 30));
		registerPanel.add(registerAssignment);
		registerPanel.add(csRegisterHolder);
		registerPanel.add(ipRegisterHolder);
		registerPanel.add(axRegisterHolder);
		registerPanel.add(bxRegisterHolder);
		registerPanel.add(cxRegisterHolder);
		registerPanel.add(dxRegisterHolder);
		registerPanel.add(dsRegisterHolder);
		registerPanel.add(ssRegisterHolder);
		registerPanel.add(spRegisterHolder);
		registerPanel.add(bpRegisterHolder);

		flagPanel = new JPanel();
		flagPanel.setPreferredSize(new Dimension(500, 50));
		flagPanel.setBackground(backgroundColor);
		flagPanel.setOpaque(false);

		menu = new JMenuBar();
		// menu.setPreferredSize(new Dimension(800,100));
		menu.setLayout(new FlowLayout(FlowLayout.LEFT));
		JMenu fileMenu = new JMenu("File");
		exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
		menu.add(fileMenu);

		// JMenu windowMenu = new JMenu("Window");
		windowConsoleItem = new JMenuItem("Console");
		windowHighLevelItem = new JMenuItem("High-Level");
		// windowMenu.add(windowConsoleItem);
		// windowMenu.add(windowHighLevelItem);
		// menu.add(windowMenu);

		JMenu helpMenu = new JMenu("Help");
		helpItem = new JMenuItem("Help");
		aboutItem = new JMenuItem("About");
		helpMenu.add(helpItem);
		helpMenu.add(aboutItem);
		menu.add(helpMenu);

		toolPanel = new JPanel();
		// toolPanel.setPreferredSize(new Dimension(500, 100));
		toolPanel.setBackground(backgroundColor);
		toolPanel.setOpaque(false);

		JPanel toolPanelSub = new JPanel();
		compilerTool = new JButton("Assemble");
		resetTool = new JButton("Reset");
		templateTool = new JButton("Templates");
		ImageIcon stopIcon = new ImageIcon(new Object().getClass().getResource(
				"/images/stop.png"));
		stopToggleTool = new JButton(stopIcon);
		stopToggleTool.setToolTipText("Stop");
		stopToggleTool.setEnabled(false);

		clearTool = new JButton("Clear");
		ImageIcon stepIcon = new ImageIcon(new Object().getClass().getResource(
				"/images/step.png"));
		stepTool = new JButton(stepIcon);
		stepTool.setToolTipText("Step");
		stepTool.setEnabled(false);
		ImageIcon runIcon = new ImageIcon(new Object().getClass().getResource(
				"/images/run.png"));
		runTool = new JButton(runIcon);
		runTool.setToolTipText("Run");
		runTool.setEnabled(false);

		runSpeed = new JSlider();
		runSpeed.setMaximum(500);
		runSpeed.setMajorTickSpacing(100);
		runSpeed.setMinorTickSpacing(100);
		runSpeed.setPaintTicks(true);
		runSpeed.setMinimum(100);
		runSpeed.setValue(100);
		runSpeed.setBackground(backgroundColor);
		runSpeed.setOpaque(false);
		sliderValue = new JLabel("Slowest");
		sliderValue.setPreferredSize(new Dimension(80, 25));
		runSpeed.setPreferredSize(new Dimension(150, 40));

		toolPanelSub.add(clearTool);

		toolPanelSub.add(templateTool);
		toolPanelSub.add(compilerTool);
		toolPanelSub.add(resetTool);
		toolPanelSub.add(stepTool);
		toolPanelSub.add(stopToggleTool);
		toolPanelSub.add(runTool);
		toolPanelSub.add(runSpeed);

		toolPanelSub.add(sliderValue);
		toolPanel.add(toolPanelSub);
		toolPanelSub.setBackground(backgroundColor);
		toolPanelSub.setOpaque(false);
		informationLabel = new JLabel();
		informationLabel.setPreferredSize(new Dimension(100, 100));
		feedbackPanel = new JPanel();
		feedbackPanel.setPreferredSize(new Dimension(10, 100));
		feedbackPanel.add(informationLabel);
		feedbackPanel.setBackground(backgroundColor);
		feedbackPanel.setOpaque(false);

		viewTabbedPane = new JTabbedPane();

		// viewTabbedPane.setUI(new InvisibleTabs());

		memoryPanel = new JPanel();
		memoryPanel.setPreferredSize(new Dimension(300, 300));
		memoryPanel.setBackground(new Color(246, 253, 219));
		// memoryPanel.setBackground(backgroundColor);

		editorPane = new TextEditor();

		JScrollPane editorScroll = new JScrollPane(editorPane);

		viewTabbedPane.addTab("Editor", editorScroll);
		viewTabbedPane.addTab("Segment Registers", memoryPanel);
		// viewTabbedPane.addTab("Memory", createMemoryTable());

		viewTabbedPane.setSelectedIndex(0);

		rfmContainer = new JPanel();
		rfmContainer.setLayout(new BorderLayout());
		rfmContainer.add(registerPanel, BorderLayout.WEST);
		rfmContainer.add(viewTabbedPane, BorderLayout.CENTER);
		highlevelGUI = new HighLevelGUI(this);
		
		rfmContainer.setBackground(backgroundColor);
		rfmContainer.setOpaque(false);

		add(menu);
		add(toolPanel);
		add(rfmContainer);
		add(flagPanel);

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Creates a new JTable <code>SegmentTable</code> with the columns "offset"
	 * and "value" and put
	 * 
	 * @return (JScrollPane) The JScrollPane that contains a table which is used
	 *         for the data segment
	 */
	public JScrollPane createDataTable() {

		dataModel = new DefaultTableModel() {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("Offset");
		temp.add("Value");
		SegmentTable segment = new SegmentTable(dataModel, temp);
		int vColIndex = 0;
		TableColumn col = segment.getColumnModel().getColumn(vColIndex);
		int width = 110;
		col.setPreferredWidth(width);

		JScrollPane jScrollPane = new JScrollPane(segment);
		jScrollPane.setPreferredSize(new Dimension(200, 350));

		return jScrollPane;
	}

	/**
	 * Creates a new JTable <code>SegmentTable</code> with the columns "offset"
	 * and "content" and put
	 * 
	 * @return (JScrollPane) The JScrollPane that contains a table which is used
	 *         for the stack segment
	 */
	public JScrollPane createStackTable() {

		stackModel = new DefaultTableModel() {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("Offset");
		temp.add("Content");
		SegmentTable segment = new SegmentTable(stackModel, temp);
		int vColIndex = 0;
		TableColumn col = segment.getColumnModel().getColumn(vColIndex);
		int width = 60;
		col.setPreferredWidth(width);
		JScrollPane jScrollPane = new JScrollPane(segment);
		jScrollPane.setPreferredSize(new Dimension(200, 350));

		return jScrollPane;
	}

	public void clearStackTable() {
		Runnable r;
		r = new Runnable(){

			@Override
			public void run() {
				while (stackModel.getRowCount() > 0) {
					stackModel.removeRow(0);
				}
				
			}
		};
		new Thread(r).start();

	}

	/**
	 * Creates a new JTable <code>SegmentTable</code> with the columns "IP" and
	 * "Instruction" and put
	 * 
	 * @return (JScrollPane) The JScrollPane that contains a table which is used
	 *         for the code segment
	 */
	public JScrollPane createTable(boolean start) {

		model = new DefaultTableModel() {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("IP");
		temp.add("Instruction");
		codeSegment = new SegmentTable(model, temp);
		int vColIndex = 1;
		TableColumn col = codeSegment.getColumnModel().getColumn(vColIndex);
		int width = 90;
		col.setPreferredWidth(width);
		JScrollPane jScrollPane = new JScrollPane(codeSegment);
		jScrollPane.setPreferredSize(new Dimension(250, 350));

		return jScrollPane;
	}

	public ArrayList<JTextField> getTextFields() {
		return textFieldContainer;
	}

	/**
	 * Loads the program in the assembly text area and passes to the CPU for
	 * processing Creates the data segment, stack segment and code segment
	 * 
	 */
	public void assemblyProgram() { // TODO Auto-generated method stub

		resetEverything();
		simulator.reset();

		simulator.loadProgram(new StringBuffer(editorPane.getText()));
		if (simulator.canExecute()) {
			JPanel dataTable = new JPanel(new BorderLayout());
			JLabel dataLabel = new JLabel("Data Segment");
			dataLabel.setForeground(new Color(23, 88, 154));
			dataLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
			dataTable.add(dataLabel, BorderLayout.NORTH);
			dataTable.add(createDataTable(), BorderLayout.SOUTH);
			memoryPanel.add(dataTable);
			dataTable.setBackground(new Color(246, 253, 219));

			JPanel codeTable = new JPanel(new BorderLayout());
			JLabel codeLabel = new JLabel("Code Segment");
			codeLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
			codeLabel.setForeground(new Color(23, 88, 154));
			codeTable.add(codeLabel, BorderLayout.NORTH);
			codeTable.add(createTable(true), BorderLayout.SOUTH);
			memoryPanel.add(codeTable);
			codeTable.setBackground(new Color(246, 253, 219));

			JPanel stackTable = new JPanel(new BorderLayout());
			JLabel stackLabel = new JLabel("Stack Segment");
			stackLabel.setForeground(new Color(23, 88, 154));
			stackLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
			stackTable.add(stackLabel, BorderLayout.NORTH);
			stackTable.add(createStackTable(), BorderLayout.SOUTH);
			memoryPanel.add(stackTable);
			memoryPanel.setBackground(new Color(246, 253, 219));
			stackTable.setBackground(new Color(246, 253, 219));

			addInstructions();
			addDataInstructions();
					
			repaint();
		}
	}

	public JLabel registerAssignmentLabel() {
		return registerAssignment;
	}

	/**
	 * Retrieves program counter
	 * 
	 * @param program
	 *            assembly language program
	 * @return (int) program counter
	 */
	public void addInstructions() {
		
		for (int i = 0; i < simulator.getCodeSegmentFile().size(); i++) {
			boolean end = false;
			String instruction = simulator.getCodeSegmentFile().get(i)
					.toString();
			
			String label = "";
			String endlabel = "";
			 Set<String> table = SymbolTable.getInstance().getSymbolTable().keySet();
			
			for(String key: table){				
				int location = SymbolTable.getInstance().getLocation(key);
				String type = SymbolTable.getInstance().getType(key);
				if(location == i && type.equals("code")){
					label = " " + key;
				}
				if(location > (simulator.getCodeSegmentFile().size()-1) && i == simulator.getCodeSegmentFile().size()-1){
					endlabel = key;
					end = true;
				}
			}
			String[] temp = { (simulator.getCodeSegmentFile().get(i).getSize() * (i+1)) + " "+label, instruction };
			model.addRow(temp);
			if(end){
				String[] endTemp = { (simulator.getCodeSegmentFile().get(i).getSize() * (i+1) + 2) + " "+endlabel, " " };
				model.addRow(endTemp);				
			}
		}
	}

	/**
	 * Adds rows to the data segment table. Data is retrieved from the memory
	 * <code>Memory</code> class
	 * 
	 */
	public void addDataInstructions() {
		for (int i = 0; i < simulator.getMemory().dataSegmentStart; i++) {
			int value = simulator.getMemory().getValue(
					simulator.getMemory().dataSegmentStart + i);
			if (value != 0) {
				String[] temp = {
						simulator.getMemory().dataSegmentStart + (i) + " ("
								+ simulator.getGlobalName(i) + ")", value + "" };
				dataModel.addRow(temp);
			} else {
				break;
			}
		}
	}

	public void addStepListener(ActionListener step) {
		stepTool.addActionListener(step);
	}

	public TextEditor assemblyTextEditor() {
		return editorPane;
	}

	public JTabbedPane getTabs() {
		return viewTabbedPane;

	}

	/**
	 * Resets the GUI back to its initial state
	 * 
	 */
	public void resetEverything() {

		memoryPanel.removeAll();
		resetFieldValues();
		repaint();
	}
	
	public void resetFieldValues(){
		axField.setText("0");
		bxField.setText("0");
		cxField.setText("0");
		dxField.setText("0");
		csField.setText(Integer.toString(simulator.getMemory().codeSegmentStart));
		ipField.setText("0");
		dsField.setText(Integer.toString(simulator.getMemory().dataSegmentStart));
		siField.setText("0");
		ssField.setText(Integer.toString(simulator.getMemory().stackSegmentStart));
		spField.setText("1000");
		bpField.setText("1000");
	}

	public void setSliderValue() {
		String sliderLabel = "";
		double maxvalue = runSpeed.getMaximum();
		double value = runSpeed.getValue();
		double slower = maxvalue / 3;
		double slow = maxvalue / 2;
		double fast = maxvalue / 1.2;
		if (value <= slower)
			sliderLabel = "Slowest";
		if (value > slower && value < slow)
			sliderLabel = "Slow";
		if (value > slow && value < fast)
			sliderLabel = "Fast";
		if (value > fast)
			sliderLabel = "Fastest";

		sliderValue.setText(sliderLabel);
	}

	/**
	 * @return
	 * @uml.property name="sliderValue"
	 */
	public int getSliderValue() {
		return (runSpeed.getMaximum() - runSpeed.getValue())
				+ runSpeed.getMinimum();
	}

	public void addClearListener(ActionListener clear) {
		clearTool.addActionListener(clear);
	}

	public void addRunListener(ActionListener run) {
		runTool.addActionListener(run);
	}

	public void addTemplateBtnListener(ActionListener template) {
		templateTool.addActionListener(template);
	}

	public JButton getStopToggleBtn() {
		return stopToggleTool;
	}

	public void addStopListener(ActionListener stop) {
		stopToggleTool.addActionListener(stop);
	}

	public void addHighLevelWindowListener(ActionListener window) {
		windowHighLevelItem.addActionListener(window);
	}

	public void addAboutWindowListener(ActionListener aboutWindow) {
		aboutItem.addActionListener(aboutWindow);
	}

	public void addResetListener(ActionListener reset) {
		resetTool.addActionListener(reset);
	}

	public void addHelpWindowListener(ActionListener helpWindow) {
		helpItem.addActionListener(helpWindow);
	}

	public void addExitListener(ActionListener exit) {
		exitItem.addActionListener(exit);
	}

	public void addConsolelWindowListener(ActionListener window) {
		windowConsoleItem.addActionListener(window);
	}

	public void addZoomInListener(ActionListener zoomin) {
		zoomInTool.addActionListener(zoomin);
	}

	public void addCompilerListener(ActionListener compiler) {
		compilerTool.addActionListener(compiler);
	}

	public void addSliderListener(ChangeListener slider) {
		runSpeed.addChangeListener(slider);
	}

	public JButton stepButton() {
		return stepTool;
	}

	public JButton runButton() {
		return runTool;
	}


	public void clearTextFields() {
		for (int i = 0; i < textFieldContainer.size(); i++) {
			textFieldContainer.get(i).setBackground(Color.WHITE);
		}
		ipField.setBackground(Color.WHITE);
	}

	/**
	 * This is used by the Observer design pattern. This method is called when
	 * there is a change has been notified
	 * 
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (o instanceof ManageInstructions) {
			clearTextFields();

			for (int i = 0; i < textFieldContainer.size(); i++) {
				String registerName = textFieldContainer.get(i).getName();
				if (!textFieldContainer
						.get(i)
						.getText()
						.equals(Integer.toString(simulator.getRegisters()
								.getRegisterValue(registerName)))) {
					textFieldContainer.get(i).setBackground(
							new Color(51, 204, 255));
				}
				textFieldContainer.get(i).setText(
						Integer.toString(simulator.getRegisters()
								.getRegisterValue(registerName)));
			}

			csField.setText(Integer.toString(simulator.getMemory().codeSegmentStart));
			if (Integer.toString(simulator.getProgramCounter()) != ipField
					.getText()) {
				ipField.setBackground(new Color(51, 204, 255));
			}
			ipField.setText(Integer.toString((simulator.getProgramCounter()*2)+2));
			dsField.setText(Integer.toString(simulator.getMemory().dataSegmentStart));
			ssField.setText(Integer.toString(simulator.getMemory().stackSegmentStart));
		}
		if (o instanceof MemoryStack) {

			ArrayList stack = simulator.getMemory().getStack().stack();
			while (stackModel.getRowCount() > 0) {
				stackModel.removeRow(0);
			}

			int bp = simulator.getMemory().getStack().oldbp;
			for (int i = 0; i < stack.size(); i++) {
				String operator = "+";
				int n = (stack.size() - i) * 2;
				if (i < bp && bp != 0) {
					operator = "+";
					n = (bp - i) * 2;
				}
				if (bp == 0) {
					operator = "-";
				}
				if (i > bp && bp != 0) {
					operator = "-";
				}
				if (i == bp && bp != 0) {
					n = 0;
				}

				String[] stackRow = { "bp " + operator + " " + n,
						stack.get(i).toString() };
				stackModel.addRow(stackRow);
			}
		}
	}

	public AssignmentFillIn getAssignmentFillIn() {
		// TODO Auto-generated method stub
		return simulator.getAssignmentFillIn();
	}

	public HighLevelGUI getHighLevelPanel() {
		// TODO Auto-generated method stub
		return highlevelGUI;
	}

}
