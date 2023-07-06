import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.*;
import javax.swing.event.*;

class MyFrame extends JFrame implements ActionListener, DocumentListener
{
	private JMenuBar mb;
	private JMenu menu_file, menu_edit;
	private JMenuItem mi;
	private JScrollPane sp;
	private JTextArea ta, ta1;
	private JFileChooser jsp;
	private FileNameExtensionFilter fne;
	private boolean file_direct_save = false;
	private boolean is_file_saved = true;
	private MyFrame me = this;
	private String prev_txt = "";
	private String cur_txt = "";


	public MyFrame()
	{
		super("Simple Notepad");

		mb = new JMenuBar();
		this.setJMenuBar(mb);

		menu_file = new JMenu("File");
		mb.add(menu_file);

		String text1[] = {"new", "open", "save", "save_as", "exit"};
		
		for(int i = 0; i<text1.length; i++)
		{
			if(i > 2)
			menu_file.addSeparator();

			mi = new JMenuItem(text1[i], new ImageIcon("images/"+text1[i]+".jpg"));
			menu_file.add(mi);
			mi.addActionListener(this);
		}

		menu_edit = new JMenu("Edit");
		mb.add(menu_edit);
		String text2[] = {"cut", "copy", "paste"};

		for(int i = 0; i<text2.length; i++)
		{
			mi = new JMenuItem(text2[i], new ImageIcon("images/"+text2[i]+".jpg"));
			menu_edit.add(mi);
			mi.addActionListener(this);
		}
		ta1 = new JTextArea();
		this.add(ta1, BorderLayout.NORTH);
		ta1.setEditable(false);
		ta1.setText(" Unknown File");

		ta = new JTextArea();
		ta.setFont(new Font("serif", Font.BOLD, 18));
		sp = new JScrollPane(ta);
		this.add(sp, BorderLayout.CENTER);
		ta.getDocument().addDocumentListener(this);

		// jsp = new JFileChooser("E:/1. 5th Sem/Advanced JAVA/Practice/SWING");
		jsp = new JFileChooser();

		String desc[] = {"C File", "CPP File", "JAVA File", "TEXT File"};
		String extension[] = {"c", "cpp", "java", "txt"};
		

		for(int i = 0; i<desc.length; i++)
		{
			fne = new FileNameExtensionFilter(desc[i], extension[i]);
			jsp.setFileFilter(fne);
		}

		this.setVisible(true);
		this.setSize(500, 400);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				if(!is_file_saved)
					{
						int reply = JOptionPane.showConfirmDialog(me, "Do you want to save the file",	 "Unsaved File", JOptionPane.YES_NO_OPTION);
						if(reply == JOptionPane.YES_OPTION)
						{
							save();
						}
					}

			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String text = e.getActionCommand().toLowerCase();


		switch(text)
		{

			case "new": 
					if(!is_file_saved)
					{
						int reply = JOptionPane.showConfirmDialog(this, "Do you want to save the file",	 "Unsaved File", JOptionPane.YES_NO_OPTION);
						if(reply == JOptionPane.YES_OPTION)
						{
							save();
						}
					}
					ta.setText(""); 
					prev_txt = "";
					file_direct_save=false; 
					break;
			case "open":
					if(!is_file_saved)
					{
						int reply = JOptionPane.showConfirmDialog(this, "Do you want to save the file",	 "Unsaved File", JOptionPane.YES_NO_OPTION);
						if(reply == JOptionPane.YES_OPTION)
						{
							save();
						}
					}
					open();
					break;
			case "save": 
					save();
					break;
			case "save_as":
					save_as(true);
					break;
			case "cut": ta.cut(); break;
			case "copy": ta.copy(); break;
			case "paste": ta.paste(); break;
			case "exit": 
					if(!is_file_saved)
					{
						int reply = JOptionPane.showConfirmDialog(this, "Do you want to save the file",	 "Unsaved File", JOptionPane.YES_NO_OPTION);
						if(reply == JOptionPane.YES_OPTION)
						{
							save();
						}
					}
					this.dispose(); 
					break;
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		
		textChanged();


	}
	@Override
	public void removeUpdate(DocumentEvent e)
	{
		
		textChanged();
	}
	@Override
	public void changedUpdate(DocumentEvent e)
	{
		textChanged();
		


	}

	public void textChanged()
	{
		cur_txt = ta.getText();
		// System.out.println("cur_txt: "+cur_txt);
		// System.out.println("prev_txt: "+prev_txt);

		if(!cur_txt.equals(prev_txt))
		is_file_saved = false;
	else {
		is_file_saved = true;
	}

		if(!is_file_saved){
			if(!ta1.getText().endsWith("*"))
			ta1.setText(ta1.getText()+ " *");
		}
		else
		{
			String t = ta1.getText();
			ta1.setText(t.substring(0, t.length()-2));
		}
	}

	public void save()
	{
		try
		{
			if(file_direct_save)
			{
				String txt = ta1.getText();
				FileOutputStream fout = new FileOutputStream(txt.substring(0, txt.length()-2));
				String data = ta.getText();
				byte arr[] = data.getBytes();
				fout.write(arr);
				fout.close();
				JOptionPane.showMessageDialog(this, "File Saved");
				file_direct_save = true;
				is_file_saved = true;
				prev_txt = ta.getText();
			}
			else
			{
				save_as(false);


			}
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex);
		}
	}

	public void save_as(boolean cond)
	{
		try
		{
			int result = jsp.showSaveDialog(this);

			if(result == JFileChooser.APPROVE_OPTION)
			{
				File f = jsp.getSelectedFile();
				FileOutputStream fout = new FileOutputStream(f);
				String data = ta.getText();
				byte arr[] = data.getBytes();
				fout.write(arr);
				fout.close();

				if(cond){
					ta.setText("");
					ta1.setText("Unknown File");
				}
				JOptionPane.showMessageDialog(this, "File Saved");
				file_direct_save = true;
				is_file_saved = true;
				prev_txt = ta.getText();
			}
		} 
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex);
		}
	}

	public void open()
	{
		try
					{
						int result = jsp.showOpenDialog(this);
						if(result == JFileChooser.APPROVE_OPTION)
						{
							File f = jsp.getSelectedFile();
							FileInputStream fin = new FileInputStream(f);
							int size = fin.available();
							byte arr[] = new byte[size];
							fin.read(arr);
							String data = new String(arr);
							fin.close();
							ta.setText(data);
							file_direct_save = true;
							
							ta1.setText(f.getAbsolutePath());
							// JOptionPane.showMessageDialog(this, f.getAbsolutePath()); 
							is_file_saved = true;	
							prev_txt = ta.getText();

						}						
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(this, ex);
					}
	}

}



class Sample
{
	public static void main(String[] args) {
		new MyFrame();
	}
}