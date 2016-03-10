import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FileCustomClass {

	// create a file named .directory listing all directory and files in every
	// subdirectory of "path". Recursive calls.	
	private static void createDirectoryFiles(Path path) throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			// build a string with files and directories
			StringBuffer pathNames = new StringBuffer();
			
			for (Path entry : stream) {

				if (Files.isDirectory(entry)) {
					createDirectoryFiles(entry);
				}
				// adding filenames and directories names to the string buffer.
				pathNames.append(entry.toString());
				pathNames.append(System.lineSeparator());
			}
			Path documentPath = Paths.get(path.toString(),".directory");
			
			// on crée le fichier s'il n'existe pas
			if (Files.exists(documentPath)){
				System.out.println(documentPath+" exists");
			}
			else
			{
				Files.createFile(documentPath);
				System.out.println(documentPath+" created");
			}
			Files.write(documentPath, pathNames.toString().getBytes());
			System.out.println(documentPath+" updated");
		}
	}

	// Creates subDirectories called sub1/subsub1 and sub2/subsub2
	private static void createSubDirectory(Path subDirectory) {
		if (Files.exists(subDirectory)) {
			System.out.println(subDirectory + "already exists");
		} else {
			System.out.println("Creating directory " + subDirectory);
			try {
				Files.createDirectories(subDirectory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// copy a file to a specified path, and adds at the beginning name of directory and date
	private static void copyFileToPath(Path filePath, Path directoryPath) throws IOException
	{
		
		// get copied file path
		Path fileCopied = Paths.get(directoryPath.toString(),filePath.getFileName().toString());
		
		// copy file
		Files.copy(filePath, fileCopied,  StandardCopyOption.REPLACE_EXISTING);
		
		List<String> fileContent = new ArrayList<>();
		
		// reading lines of file
		fileContent.addAll(Files.readAllLines(fileCopied));
		
		// on ajoute au début le nom du dossier et la date du jour
		fileContent.add(0, directoryPath.toString() + " " + new Date().toString());
		
		Files.write(fileCopied, fileContent);
		
	}
	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		System.out.println("Saisissez un chemin");
		String saisie = scan.nextLine();

		// java nio
		// Paths et Files servent à remplacer File
		Path path = Paths.get(saisie);

		// ajout de 2 sous répertoires /sub1/subsub1 et sub2/subsub2 dans le
		// chemin saisi par l'utilisateur
		Path subsub1 = Paths.get(path.toString(), "sub1", "subsub1");
		Path subsub2 = Paths.get(path.toString(), "sub2", "subsub2");

		createSubDirectory(subsub1);
		createSubDirectory(subsub2);

		// on visite tous les répertoires, et pour chaque répertoire on crée un
		// fichier .directory
		// qui liste le contenu du répertoire
		try {
			createDirectoryFiles(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Chemin du fichier à copier vers le dossier saisi");
		Path filePath = Paths.get(scan.nextLine());
		
		try {
			// copy file
			copyFileToPath(filePath, path);
			// update .directory files
			createDirectoryFiles(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
