package me.jacobbrunson.GridGame.launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;

public class Main extends JFrame implements ActionListener {

	JButton playButton;
	JLabel text = new JLabel("Checking for updates");
	File gameFile;

	public Main() {
		super("GridGame by Jacob Brunson");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));

		content.add(text);

		JPanel serverPane = new JPanel();
		serverPane.setLayout(new BoxLayout(serverPane, BoxLayout.LINE_AXIS));
		JTextField serverAddress = new JTextField();
		serverAddress.setMaximumSize(serverAddress.getPreferredSize());
		serverPane.add(new JTextField(20));
		playButton = new JButton("Play");
		playButton.setEnabled(false);
		playButton.addActionListener(this);
		serverPane.add(playButton);
		content.add(serverPane);

		content.add(Box.createVerticalGlue());

		pack();
		setVisible(true);

		gameFile = new File("GridGameBin.jar");

		update();

	}

	public void update() {
		try {
			File versionFile = new File("game.version");
			String currentVersion;
			if (versionFile.exists()) {
				Scanner scanner = new Scanner(new File("game.version"));
				currentVersion = scanner.nextLine();
			} else {
				currentVersion = "0.0";
			}


			URL url = new URL("http://jacobbrunson.me/GridGame/checkupdate.php?version=" + currentVersion);
			URLConnection connection = url.openConnection();
			connection.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String version;
			if (!(version = in.readLine()).equals("1") || !(gameFile.exists())) {
				System.out.println("Outdated. Installed version: " + currentVersion + " | Current version: " + version);
				text.setText("Updating...");
				URL download = new URL("http://jacobbrunson.me/GridGame/data/GridGame.jar");
				ReadableByteChannel rbc = Channels.newChannel(download.openStream());
				FileOutputStream fos = new FileOutputStream("GridGameBin.jar");
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

				PrintWriter writer = new PrintWriter("game.version", "UTF-8");
				writer.println(version);
				writer.close();
				playButton.setEnabled(true);
			} else {
				System.out.println("Up to date: Version " + currentVersion);
				playButton.setEnabled(true);
			}
			text.setText("Up to date!");
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Main();

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			Desktop.getDesktop().open(gameFile);
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
