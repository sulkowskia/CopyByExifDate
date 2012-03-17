import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;


public class FileRenamingThread extends SwingWorker<Void, String> implements IMessageHandler, ICancellable
{
	private File _source_path;
	private File _destination_path;
	private IMessageHandler _message_handler;

	FileRenamingThread(File source_path, File destination_path, IMessageHandler message_handler)
	{
		_source_path = source_path;
		_destination_path = destination_path;
		_message_handler = message_handler;
	}
	
	@Override
	protected Void doInBackground() throws Exception
	{
		IFileHandler fh = new JpegFileHandler(_destination_path, this);
		DirectoryWalker dw = new DirectoryWalker(this);
		dw.run(_source_path, fh);
		return null;
	}
	
	@Override
	protected void process(List<String> messages)
	{
		for (String s : messages)
		{
			_message_handler.handleMessage(s);
		}
	}
	
	 @Override
	 public void done()
	 {
		 JOptionPane.showMessageDialog(null, "Files moved", "Done", JOptionPane.INFORMATION_MESSAGE);
	 }

	@Override
	public void handleMessage(String s)
	{
		publish(s);
	}
	
	@Override
	public boolean active()
	{
		return !super.isCancelled();
	}
}
