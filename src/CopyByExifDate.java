import javax.swing.JOptionPane;

public class CopyByExifDate
{	
	public static void main(String args[])
	{
		try
		{
			ConfigWindow dlg = new ConfigWindow();	
			dlg.setModal(true);
			dlg.setVisible(true);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
