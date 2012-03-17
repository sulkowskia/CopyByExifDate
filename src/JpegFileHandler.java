import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.ttt.image.InvalidImageFormatException;
import net.ttt.image.exif.ExifDecoder;

class JpegFileHandler implements IFileHandler
{
	private File _destination_root;
	private IMessageHandler _message_handler;

	public JpegFileHandler(File destination_root, IMessageHandler message_handler)
	{
		_destination_root = destination_root;
		_message_handler = message_handler;
	}

	static Date getFileDate(File f) throws IOException,
			InvalidImageFormatException
	{
		ExifDecoder dec = new ExifDecoder(f);
		return dec.getCreationDate();
	}

	static String appendZero(int i)
	{
		if (i < 10)
		{
			return "0" + i;
		}
		return Integer.toString(i);
	}

	static String subPathFromDate(Date d)
	{
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(d);
		return File.separator + cal.get(Calendar.YEAR) + File.separator
				+ appendZero(cal.get(Calendar.MONTH) + 1);
	}

	void moveFile(File from, File to) throws IOException
	{
		_message_handler.handleMessage(from.toString() + " -> " + to);
		new File(to.getParent()).mkdirs();
		if (!from.renameTo(to))
		{
			throw new IOException("Cannot move the file " + from + " to " + to);
		}
	}

	@Override
	public void handle(File f)
	{
		try
		{
			moveFile(f, getDestinationPath(f));
		}
		catch (Exception e)
		{
			_message_handler.handleMessage("Error processing " + f + e.getMessage());
		}
	}

	private File getDestinationPath(File f) throws IOException,
			InvalidImageFormatException
	{
		Date d = getFileDate(f);
		return new File(_destination_root + subPathFromDate(d) + File.separator
				+ f.getName());
	}
}
