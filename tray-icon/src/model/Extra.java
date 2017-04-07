package model;

import java.awt.Desktop;
import java.net.URI;

import builder.UserDefinedException;
import javafx.scene.control.Hyperlink;

public class Extra {
	Hyperlink link;

	public Hyperlink getLink() {
		link.setOnAction(e -> openBrowser());
		return link;
	}

	public void openBrowser() {
		try {
			Desktop.getDesktop().browse(new URI(link.getText().trim()));
		} catch (final Exception e) {
			throw new UserDefinedException(e.getMessage(), "Please check");
		}
	}

	public void setLink(final Hyperlink hyperlink) {
		link = hyperlink;
	}
}
