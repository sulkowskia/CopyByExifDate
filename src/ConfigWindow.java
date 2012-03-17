import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.Point;
import javax.swing.JList;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


@SuppressWarnings("serial")
public class ConfigWindow extends JDialog implements IMessageHandler
{

	private final JPanel contentPanel = new JPanel();
	private JTextField edit_from;
	private JTextField edit_to;
	private File _dir_from = new File("/Users/Alek/Pictures/imported_not_sorted");
	private File _dir_to = new File("/Users/Alek/Pictures/zdjecia");
	private JButton startButton;
	private JButton closeButton;
	private JPanel buttonPane;
	private DefaultListModel _list_model = new DefaultListModel();
	private JPanel panel;
	private JLabel lblFrom;
	private FileRenamingThread _worker_thread;
	private JList _message_list;
	
	private static File getDir(String start_dir, Component parent)
	{
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setSelectedFile(new File(start_dir));
		int returnVal = jfc.showOpenDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			return jfc.getSelectedFile();
		}
		return null;
	}


	/**
	 * Create the dialog.
	 */
	public ConfigWindow()
	{
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) 
			{
				closeMainWindow();
			}
		});
		setLocation(new Point(0, 32));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(800, 500, 891, 573);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{711, 0};
		gbl_contentPanel.rowHeights = new int[]{25, 29, 446, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weightx = 1.0;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPanel.add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		lblFrom = new JLabel("From:");
		panel.add(lblFrom);
		
		edit_from = new JTextField();
		panel.add(edit_from);
		edit_from.setText(_dir_from.toString());
		edit_from.setColumns(10);
		
		JButton button_from = new JButton("...");
		panel.add(button_from);
		button_from.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_dir_from = getDir(edit_from.getText(), panel);
				if (_dir_from != null)
				{
					edit_from.setText(_dir_from.toString());
				}
			}
		});
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.weightx = 1.0;
		gbc_panel_1.anchor = GridBagConstraints.NORTH;
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		contentPanel.add(panel_1, gbc_panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JLabel lblTo = new JLabel("To    :");
		panel_1.add(lblTo);
		
		edit_to = new JTextField();
		panel_1.add(edit_to);
		edit_to.setText(_dir_to.toString());
		edit_to.setColumns(10);
		
		JButton button_to = new JButton("...");
		panel_1.add(button_to);
		button_to.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_dir_to = getDir(edit_to.getText(), panel);
				if (_dir_to != null)
				{
					edit_to.setText(_dir_to.toString());
				}
			}
		});
		_message_list = new JList(_list_model);
		JScrollPane scroll_pane = new JScrollPane(_message_list);
		GridBagConstraints gbc__message_list = new GridBagConstraints();
		gbc__message_list.weighty = 1.0;
		gbc__message_list.weightx = 1.0;
		gbc__message_list.fill = GridBagConstraints.BOTH;
		gbc__message_list.gridx = 0;
		gbc__message_list.gridy = 2;
		contentPanel.add(scroll_pane, gbc__message_list);
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				startButton = new JButton("Start");
				startButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						startButton.setEnabled(false);
						startProcessing();
					}
				});
				startButton.setActionCommand("Start");
				buttonPane.add(startButton);
				getRootPane().setDefaultButton(startButton);
			}
			{
				closeButton = new JButton("Close");
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
				closeButton.setActionCommand("Cancel");
				buttonPane.add(closeButton);
			}
		}
	}

	@Override
	public void handleMessage(String s)
	{
		_list_model.addElement(s);
		_message_list.ensureIndexIsVisible(_list_model.getSize() - 1);
	}
	
	private void closeMainWindow()
	{
		if (_worker_thread != null)
		{
			_worker_thread.cancel(false);
			_worker_thread = null;
		}
	}
	
	private void startProcessing()
	{
		if (_worker_thread == null)
		{
			_worker_thread = new FileRenamingThread(_dir_from, _dir_to, this);
			_worker_thread.execute();
		}
		else
		{
			handleMessage("Renaming already running");
		}
	}
}
