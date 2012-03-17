import java.io.File;

class DirectoryWalker
{
	private ICancellable _cancellable;

	public DirectoryWalker(ICancellable cancellable)
	{
		_cancellable = cancellable;
	}
	
	void run(File source_dir, IFileHandler handler)
	{
		traverse(source_dir, handler);
	}

	private void traverse(File source_path, IFileHandler handler)
	{
		if (!_cancellable.active())
		{
			return;
		}
		
		if (source_path.isDirectory())
		{
			for (File f : source_path.listFiles())
			{
				traverse(f, handler);
			}
		}
		else
		{
			if (source_path.getName().toLowerCase().endsWith(".jpg"))
			{
				handler.handle(source_path);
			}
		}
	}
}
