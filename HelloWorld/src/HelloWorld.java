import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class HelloWorld {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ExecutionException
	 */
	@SuppressWarnings("serial")
	public static void main(String[] args) throws IOException,
			ExecutionException {
		File locCVGFile = new File(
				"C:\\Users\\vmorampudi\\Downloads\\loccvgSinglePeril.txt");

		if (!locCVGFile.exists())
			throw new IOException("LocCvg file not found.");
		else {
			System.out.println("LocCvg file path is valid");
		}

		FileInputStream locCvgFileInputStream = new FileInputStream(locCVGFile);

		DataInputStream locCvgDataInputStream = new DataInputStream(
				locCvgFileInputStream);

		BufferedReader locCvgreader = null;
		try {
			locCvgreader = new BufferedReader(new InputStreamReader(
					locCvgDataInputStream));

			String fileHeader = locCvgreader.readLine();
			if (fileHeader == null || fileHeader == "") {

				throw new IOException("LocCvg file is empty.");
			}

			String[] headerFields = fileHeader.split("~");

			int locIdIndex = -1;
			int perilIndex = -1;

			for (int i = 0; i < headerFields.length; i++) {
				System.out.println(headerFields[i]);

				if (headerFields[i].equalsIgnoreCase("LOCID")) {
					locIdIndex = i;
				} else if (headerFields[i].equalsIgnoreCase("PERIL")) {
					perilIndex = i;
				}
			}

			if (locIdIndex == -1 || perilIndex == 1) {
				throw new ExecutionException(
						"Either locid or peril code is not in the file header",
						new Throwable());
			}

			System.out.println("LocIdIndex= " + locIdIndex);
			System.out.println("PerilCodeIndex= " + perilIndex);

			Map<Integer, Integer> locationCountByPeril = new HashMap<Integer, Integer>() {
				{
					put(3, 0);
				}
			};

			String fileContentLine;
			while ((fileContentLine = locCvgreader.readLine()) != null) {
				String[] lineSplit = fileContentLine.split("~");
				int perilCode = Integer.parseInt(lineSplit[perilIndex]);

				int locCount = locationCountByPeril.get(perilCode);

				locationCountByPeril.put(perilCode, ++locCount);
			}

			DumpToFile(locationCountByPeril);
		} finally {
			if (locCvgreader != null)
				locCvgreader.close();
		}

		System.in.read();
	}

	/**
	 * @param locationCountByPeril
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void DumpToFile(Map<Integer, Integer> locationCountByPeril)
			throws FileNotFoundException, IOException {
		BufferedWriter locPerilWriter = null;
		try {
			FileOutputStream locPerilFileOutputStream = new FileOutputStream(
					"C:\\Users\\vmorampudi\\Downloads\\locPeril.txt");
			locPerilWriter = new BufferedWriter(new OutputStreamWriter(
					new DataOutputStream(locPerilFileOutputStream)));

			System.out.println("Peril~LocationCount");
			for (Map.Entry<Integer, Integer> locIdPerilSet : locationCountByPeril
					.entrySet()) {
				String temp = locIdPerilSet.getKey() + "~"
						+ locIdPerilSet.getValue() + "~";
				locPerilWriter.write(temp);
				((BufferedWriter) locPerilWriter).newLine();
				System.out.println(temp);
			}
		} finally {
			if (locPerilWriter != null)
				locPerilWriter.close();
		}
	}
}
