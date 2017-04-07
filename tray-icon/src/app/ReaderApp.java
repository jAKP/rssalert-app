package app;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.ImageIcon;

import builder.FeedMessageUtil;
import builder.SimpleCacheManager;
import builder.TableBuilder;
import builder.UserDefinedException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.FeedMessage;
import model.RSS;
import read.RSSFeedParser;

public class ReaderApp extends Application {
	private static final String ADD_A_NEW_FEED_URL = "add a new feed url";
	private static final String ADD2 = "add";
	private static final String ALL = "All";
	private static final String CHECK = "check";
	private static final String CLOSE_PNG = "close.png";
	private static final String COGS3_PNG = "cogs3.png";
	private static final String CONFIGURE = "Configure";
	private static final String DELETE = "delete";
	private static final String DELIMITER = ";";
	private static final String EMPTY = "";
	private static final String ENTER_RSS_FEED_NAME = "Enter RSS feed name";
	private static final String ENTER_RSS_FEED_URL = "Enter RSS feed URL";
	private static final String EXIT = "Exit";
	private static final String FX_BACKGROUND_COLOR_1D1D1D = "-fx-background-color: #1d1d1d;";
	private static final String FX_BACKGROUND_COLOR_1D1D1D2 = "-fx-background-color: #1d1d1d; ";
	private static final String FX_BACKGROUND_COLOR_DAE7F3 = "-fx-background-color:#dae7f3;";
	private static final String FX_BACKGROUND_COLOR_LIGHTGRAY = "-fx-background-color: lightgray;";
	private static final String FX_BACKGROUND_COLOR_TRANSPARENT = "-fx-background-color:transparent;";
	private static final String FX_TEXT_FILL_RED = "-fx-text-fill: red;";
	private static final String FX_TEXT_FILL_WHITE = "-fx-text-fill: white;";
	private static final String ICON_RSS_PNG = "icon-rss.png";
	private static final String MY_SKIN_CSS = "my-skin.css";
	private static final String NAME2 = "name";
	private static final String NOT_OK_PNG = "not_ok.png";
	private static final String OK_PNG = "ok.png";
	private static final String PLEASE_CONFIGURE_RSS_FEEDS_UR_LS = "Please configure RSS Feeds URLs.";
	private static final String RESET2 = "reset";
	private static final String REVERT_ALL_CHANGES = "revert all changes";
	private static final String RSS_FEED_READER = "RSS Feed Reader";
	private static final String RSS_FEEDS = "RSS Feeds :";
	private static final String RSS_GIF = "rss.gif";
	// private static List<RSS> RSS_LIST = null;

	private static final String SAVE_ALL = "save all";
	private static final String SAVE2 = "save";
	private static final String SHOW_FEEDS = "Show Feeds";
	static final Tooltip tooltip = null;
	private static final String TOOLTIP_MESSAGE = " is not a valid RSS feed.\n";
	private static final String TRAY_ICON = "tray icon";
	private static final String URL2 = "url";

	public static void main(final String[] args) throws IOException, java.awt.AWTException {
		Application.launch(args);
	}

	TextField name = null;

	private final Timeline notificationTimer = new Timeline();

	java.awt.MenuItem play;

	private boolean PLAY = true;

	private final String PLAY_MENU = "Pause";

	Button remove = null;
	List<FeedMessage> result = null;
	private Stage stage;

	TextField url = null;

	int urlCount;
	private final FeedMessageUtil util = new FeedMessageUtil();

	Button verify = null;

	private double xOffset = 0;

	private double yOffset = 0;

	/**
	 * Sets up a system tray icon for the application.
	 *
	 * @throws CustomException
	 */
	private void addAppToTray() {
		java.awt.Toolkit.getDefaultToolkit();
		if (!java.awt.SystemTray.isSupported()) {
			Platform.exit();
		}

		final java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
		final TrayIcon trayIcon = new TrayIcon(createImage(ReaderApp.RSS_GIF, ReaderApp.TRAY_ICON));

		trayIcon.addActionListener(event -> Platform.runLater(this::showFeedList));

		final java.awt.MenuItem showDetails = new java.awt.MenuItem(ReaderApp.SHOW_FEEDS);
		showDetails.addActionListener(event -> Platform.runLater(this::showFeedList));

		final java.awt.MenuItem configure = new java.awt.MenuItem(ReaderApp.CONFIGURE);
		configure.addActionListener(event -> Platform.runLater(this::showSettingsPage));

		play = new java.awt.MenuItem(PLAY_MENU);
		play.addActionListener(event -> Platform.runLater(this::adjustPlay));

		final java.awt.Font defaultFont = java.awt.Font.decode(null);
		final java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);

		showDetails.setFont(boldFont);

		final java.awt.MenuItem exitItem = new java.awt.MenuItem(ReaderApp.EXIT);
		exitItem.addActionListener(event -> {
			notificationTimer.stop();
			Platform.exit();
			tray.remove(trayIcon);
		});

		final java.awt.PopupMenu popup = new java.awt.PopupMenu();
		popup.add(showDetails);
		popup.add(configure);
		popup.addSeparator();
		popup.add(play);
		popup.addSeparator();
		popup.add(exitItem);
		trayIcon.setPopupMenu(popup);
		trayIcon.setToolTip(ReaderApp.RSS_FEED_READER);

		// For each entry compare the latest feed with the old latest feed
		final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(60), ev -> {
			if (PLAY) {
				for (final Map.Entry<String, String> entry : util.getRSS_MAP().entrySet()) {
					final FeedMessage latestFeed = util.getLatestFeedMsg(entry.getKey());
					for (final RSS rssOld : getRSSListCache().get("RSS_LIST")) {
						if (!rssOld.getFeedMessages().isEmpty() && rssOld.getRssLink().equals(entry.getValue())) {
							final FeedMessage oldLatestFeed = rssOld.getLatestFeedMessage();
							if (checkForLatestFeed(latestFeed, oldLatestFeed)) {
								showLatestFeed(latestFeed);
							}
						}
					}
				}
			}
			getRSSListCache().put("RSS_LIST", getRSSList());
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

		try {
			tray.add(trayIcon);
		} catch (final Exception e) {
			throw new UserDefinedException(e.getMessage(), "Please check");
		}
	}

	public void adjustPlay() {
		PLAY = !PLAY;
		if (play.getLabel().equals("Play")) {
			play.setLabel("Pause");
		} else {
			play.setLabel("Play");
		}
	}

	public boolean checkForLatestFeed(final FeedMessage latestFeed, final FeedMessage oldLatestFeed) {
		Date latestPubDate = util.getDate(latestFeed.getPubDate());
		Date oldLatestPubDate = util.getDate(oldLatestFeed.getPubDate());
		boolean b = latestPubDate.compareTo(oldLatestPubDate) > 0;
		if (b) {
			System.err.println("latestFeed: " + latestFeed.toString());
			System.err.println("oldLatFeed:" + oldLatestFeed.toString());
		}
		return b;
	}

	protected java.awt.Image createImage(final String path, final String name) {
		final URL imageURL = ReaderApp.class.getResource(path);
		if (imageURL == null) {
			return null;
		} else {
			return new ImageIcon(imageURL, name).getImage();
		}
	}

	public void createRowTemplate(final GridPane grid, final int size) {
		name = new TextField(ReaderApp.EMPTY);
		name.setId(ReaderApp.NAME2);
		name.setPromptText(ReaderApp.ENTER_RSS_FEED_NAME);
		name.setPrefColumnCount(10);
		name.setText(ReaderApp.EMPTY);
		GridPane.setConstraints(name, 1, size);
		grid.getChildren().add(name);

		url = new TextField(ReaderApp.EMPTY);
		url.setId(ReaderApp.URL2);
		url.setPromptText(ReaderApp.ENTER_RSS_FEED_URL);
		url.setPrefColumnCount(30);
		url.setText(ReaderApp.EMPTY);
		GridPane.setConstraints(url, 2, size);
		grid.getChildren().add(url);

		verify = new Button(ReaderApp.CHECK);
		GridPane.setConstraints(verify, 3, size);
		grid.getChildren().add(verify);

		remove = new Button(ReaderApp.DELETE);
		GridPane.setConstraints(remove, 4, size);
		grid.getChildren().add(remove);
	}

	void deleteRow(final GridPane grid, final int row) {
		final Set<Node> deleteNodes = new HashSet<>();
		for (final Node child : grid.getChildren()) {
			final Integer rowIndex = GridPane.getRowIndex(child);
			final int r = rowIndex == null ? 0 : rowIndex;
			if (r > row) {
				GridPane.setRowIndex(child, r - 1);
			} else if (r == row) {
				deleteNodes.add(child);
			}
		}
		grid.getChildren().removeAll(deleteNodes);
	}

	public Scene getNewScene() {
		final Scene scene = new Scene(new Group());
		scene.getStylesheets().add(getClass().getResource(ReaderApp.MY_SKIN_CSS).toExternalForm());
		scene.setFill(Color.LIGHTGRAY);
		return scene;
	}

	TextField getRowTextField(final GridPane grid, final int row, final String id) {
		for (final Node child : grid.getChildren()) {
			final Integer rowIndex = GridPane.getRowIndex(child);
			final int r = rowIndex == null ? 0 : rowIndex;
			if (r > row) {
				GridPane.setRowIndex(child, r - 1);
			} else if ((r == row) && (child instanceof TextField) && ((TextField) child).getId().equals(id)) {
				return (TextField) child;
			}
		}
		return null;
	}

	public int getUrlCount() {
		return urlCount;
	}

	private void hide(final Stage stage) {
		Platform.runLater(() -> {
			if (SystemTray.isSupported()) {
				stage.hide();
			} else {
				System.exit(0);
			}
		});
	}

	public void makeGridPane(final GridPane grid) {
		grid.getChildren().clear();

		final Map<String, String> rssNameMap = util.getRSS_MAP();
		urlCount = rssNameMap.size();

		grid.setVgap(5);
		grid.setHgap(5);
		int i = 0;
		if (urlCount > 0) {
			for (final Map.Entry<String, String> entry : util.getRSS_MAP().entrySet()) {
				name = new TextField(ReaderApp.EMPTY);
				name.setId(ReaderApp.NAME2);
				name.setPromptText(ReaderApp.ENTER_RSS_FEED_NAME);
				name.setPrefColumnCount(10);
				name.setText(entry.getKey());
				GridPane.setConstraints(name, 1, i + 1);
				grid.getChildren().add(name);

				url = new TextField(ReaderApp.EMPTY);
				url.setId(ReaderApp.URL2);
				url.setPromptText(ReaderApp.ENTER_RSS_FEED_URL);
				url.setPrefColumnCount(30);
				url.setText(entry.getValue());
				GridPane.setConstraints(url, 2, i + 1);
				grid.getChildren().add(url);

				verify = new Button(ReaderApp.CHECK);
				GridPane.setConstraints(verify, 3, i + 1);
				grid.getChildren().add(verify);

				remove = new Button(ReaderApp.DELETE);
				GridPane.setConstraints(remove, 4, i + 1);
				grid.getChildren().add(remove);

				setButtonEvents(grid);
				i++;
			}
		} else {
			name = new TextField(ReaderApp.EMPTY);
			name.setId(ReaderApp.NAME2);
			name.setPromptText(ReaderApp.ENTER_RSS_FEED_NAME);
			name.setPrefColumnCount(10);
			name.setText(ReaderApp.EMPTY);
			GridPane.setConstraints(name, 1, 1);
			grid.getChildren().add(name);

			url = new TextField(ReaderApp.EMPTY);
			url.setId(ReaderApp.URL2);
			url.setPromptText(ReaderApp.ENTER_RSS_FEED_URL);
			url.setPrefColumnCount(30);
			url.setText(ReaderApp.EMPTY);
			GridPane.setConstraints(url, 2, 1);
			grid.getChildren().add(url);

			verify = new Button(ReaderApp.CHECK);
			GridPane.setConstraints(verify, 3, 1);
			grid.getChildren().add(verify);

			remove = new Button(ReaderApp.DELETE);
			GridPane.setConstraints(remove, 4, 1);
			grid.getChildren().add(remove);
			setButtonEvents(grid);

			urlCount = 1;
		}
		stage.sizeToScene();
	}

	private HBox makeRSSFeedFunctions(final GridPane grid) {
		final HBox hbox = new HBox();
		hbox.setStyle(ReaderApp.FX_BACKGROUND_COLOR_1D1D1D);
		hbox.setSpacing(360);

		final Label label = new Label();
		label.setText(ReaderApp.RSS_FEEDS);
		label.setPadding(new Insets(5, 5, 5, 5));
		label.setStyle(ReaderApp.FX_TEXT_FILL_WHITE);

		final HBox hbbtn = new HBox();
		hbbtn.setStyle(ReaderApp.FX_BACKGROUND_COLOR_1D1D1D);

		final Button add = new Button(ReaderApp.ADD2);
		add.setTooltip(new Tooltip(ReaderApp.ADD_A_NEW_FEED_URL));

		final Button save = new Button(ReaderApp.SAVE2);
		save.setTooltip(new Tooltip(ReaderApp.SAVE_ALL));

		final Button reset = new Button(ReaderApp.RESET2);
		reset.setTooltip(new Tooltip(ReaderApp.REVERT_ALL_CHANGES));

		hbbtn.setSpacing(10);
		hbbtn.getChildren().addAll(add, save, reset);

		add.setOnAction(e -> {
			urlCount = urlCount + 1;

			createRowTemplate(grid, urlCount);

			setButtonEvents(grid);
			setUrlCount(urlCount);

			stage.sizeToScene();
		});

		save.setOnAction(e -> {
			util.deleteFile();
			urlCount = 0;
			for (final Node child : grid.getChildren()) {
				if ((child instanceof Button) && ((Button) child).getText().equals(ReaderApp.CHECK)) {
					validateRssAndUpdateNode(grid, child, true);
				}
			}
			util.readRssMapFromFile();
		});
		reset.setOnAction(e -> makeGridPane(grid));

		hbox.getChildren().addAll(label, hbbtn);
		return hbox;
	}

	public void makeSceneDraggable(final Scene node) {
		node.setOnMousePressed(event -> {
			xOffset = stage.getX() - event.getScreenX();
			yOffset = stage.getY() - event.getScreenY();
		});

		node.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() + xOffset);
			stage.setY(event.getScreenY() + yOffset);
		});
	}

	public HBox makeTitleHeader() {
		final HBox hbox = new HBox();
		final HBox hbbtn = new HBox();

		final Label label = new Label(ReaderApp.CONFIGURE);
		hbox.setSpacing(570);
		label.setPadding(new Insets(5, 0, 0, 0));

		final Button cbtn = new Button(ReaderApp.EMPTY);
		cbtn.setOnMouseClicked(t -> hide(stage));

		final Button sbtn = new Button(ReaderApp.EMPTY);
		sbtn.setOnMouseClicked(t -> showFeedList());

		setButtonBackground(cbtn, ReaderApp.CLOSE_PNG);
		setButtonBackground(sbtn, ReaderApp.ICON_RSS_PNG);

		hbbtn.setSpacing(1);

		hbbtn.getChildren().addAll(sbtn, cbtn);
		hbox.getChildren().addAll(label, hbbtn);
		return hbox;
	}

	public void setButtonBackground(final Button cbtn, final String icon) {
		cbtn.setStyle(ReaderApp.FX_BACKGROUND_COLOR_TRANSPARENT);
		cbtn.setOnMouseEntered(t -> cbtn.setStyle(ReaderApp.FX_BACKGROUND_COLOR_DAE7F3));
		cbtn.setOnMouseExited(t -> cbtn.setStyle(ReaderApp.FX_BACKGROUND_COLOR_TRANSPARENT));
		cbtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
	}

	public void setButtonEvents(final GridPane grid) {
		remove.setOnAction(e -> {
			deleteRow(grid, GridPane.getRowIndex((Node) e.getSource()));
			setUrlCount(getUrlCount() - 1);
		});
		verify.setOnAction(e -> {
			validateRssAndUpdate(grid, e);
			setUrlCount(getUrlCount());
		});
	}

	void setRowText(final GridPane grid, final int row, final String url) {
		for (final Node child : grid.getChildren()) {
			final Integer rowIndex = GridPane.getRowIndex(child);
			final int r = rowIndex == null ? 0 : rowIndex;
			if (r > row) {
				GridPane.setRowIndex(child, r - 1);
			} else if ((r == row) && (child instanceof TextField)
					&& ((TextField) child).getId().equals(ReaderApp.URL2)) {
				((TextField) child).setText(url);
			}
		}
	}

	public void setUrlCount(final int urlCount) {
		this.urlCount = urlCount;
	}

	private RSS getRSS(final Entry<String, String> entry) {
		final RSS rss = new RSS();
		List<FeedMessage> feedMessages = util.getFeedMessages(entry.getKey());
		rss.setFeedMessages(feedMessages);
		rss.setRssLink(entry.getValue());
		rss.setRssName(entry.getKey());
		if (!feedMessages.isEmpty()) {
			System.out.println("latest: " + feedMessages.get(0).getName() + " : " + feedMessages.get(0).getPubDate()
					+ " : " + feedMessages.get(0).getTitle());
			rss.setLatestFeedMessage(feedMessages.get(0));
		}

		return rss;
	}

	private List<RSS> getRSSList() {
		final List<RSS> rssLst = new ArrayList<>();
		for (final Map.Entry<String, String> entry : util.getRSS_MAP().entrySet()) {
			rssLst.add(getRSS(entry));
		}
		System.out.println("");
		return new ArrayList<RSS>(new LinkedHashSet<RSS>(rssLst));
	}

	private void showFeedList() {
		util.readRssMapFromFile();
		List<RSS> rssLists = getRSSList();
		getRSSListCache().put("RSS_LIST", rssLists);
		urlCount = rssLists.size();

		stage.hide();
		final Scene scene = getNewScene();
		stage.setTitle(ReaderApp.RSS_FEED_READER);
		stage.centerOnScreen();

		final HBox headerBox = new HBox();
		final HBox cornerBtnsBox = new HBox();

		final Label label = new Label(ReaderApp.RSS_FEED_READER);
		label.setPadding(new Insets(5, 0, 0, 0));
		headerBox.setSpacing(645);

		final Button cbtn = new Button(ReaderApp.EMPTY);
		setButtonBackground(cbtn, ReaderApp.CLOSE_PNG);
		cbtn.setOnMouseClicked(t -> hide(stage));

		final Button sbtn = new Button(ReaderApp.EMPTY);
		setButtonBackground(sbtn, ReaderApp.COGS3_PNG);
		sbtn.setOnMouseClicked(t -> showSettingsPage());

		cornerBtnsBox.setSpacing(1);

		cornerBtnsBox.getChildren().addAll(sbtn, cbtn);
		headerBox.getChildren().addAll(label, cornerBtnsBox);

		final TableView<FeedMessage> feedTable = TableBuilder.buildUpon(FeedMessage.class);
		feedTable.setRowFactory(tableView -> {
			final TableRow<FeedMessage> row = new TableRow<FeedMessage>() {
				Tooltip ttip = new Tooltip();

				@Override
				public void updateItem(final FeedMessage feed, final boolean empty) {
					super.updateItem(feed, empty);
					if (!empty) {
						ttip.setText(feed.getTitle());
						setTooltip(ttip);
					}
				}
			};
			row.setOnMouseClicked(event -> {
				if ((event.getClickCount() == 2) && (!row.isEmpty())) {
					util.openBrowser(row.getItem().getLink());
				}
			});
			return row;
		});

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(5, 5, 10, 5));
		vbox.setPrefSize(850, 450);

		final Separator separator2 = new Separator();
		separator2.setOrientation(Orientation.HORIZONTAL);

		final HBox hbox = new HBox();
		hbox.setStyle(ReaderApp.FX_BACKGROUND_COLOR_LIGHTGRAY);
		hbox.setSpacing(20);

		result = new ArrayList<>();
		rssLists.forEach(e -> result.addAll(e.getFeedMessages()));
		sortCollection();

		final ObservableList<FeedMessage> data = FXCollections.observableArrayList(result);

		final ComboBox<String> selectBox = new ComboBox<>();
		final List<String> list = util.getRSS_NameList();
		Collections.reverse(list);
		list.add(ReaderApp.ALL);
		Collections.reverse(list);
		selectBox.getItems().addAll(list);
		selectBox.getSelectionModel().selectFirst();
		selectBox.valueProperty().addListener((ChangeListener<String>) (ov, t, t1) -> {
			feedTable.getItems().clear();
			if (t1 == ReaderApp.ALL) {
				result = util.getAllFeedMessages();
			} else {
				result = util.getFeedMessages(t1);
			}
			feedTable.setItems(FXCollections.observableArrayList(result));
			feedTable.refresh();
		});

		hbox.getChildren().addAll(selectBox);
		hbox.setPadding(new Insets(0, 0, 2, 0));
		feedTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		feedTable.setItems(data);
		feedTable.setEditable(true);

		if (!rssLists.isEmpty()) {
			vbox.getChildren().addAll(headerBox, separator2, hbox, feedTable);
		} else {
			final Label noDataLabel = new Label(ReaderApp.PLEASE_CONFIGURE_RSS_FEEDS_UR_LS);
			noDataLabel.setStyle(ReaderApp.FX_TEXT_FILL_RED);
			vbox.getChildren().addAll(headerBox, noDataLabel, feedTable);
		}

		((Group) scene.getRoot()).getChildren().addAll(vbox);

		makeSceneDraggable(scene);

		stage.setScene(scene);
		stage.setWidth(850);
		stage.setHeight(450);

		stage.show();
	}

	public SimpleCacheManager getRSSListCache() {
		return SimpleCacheManager.getInstance();
	}

	private void showLatestFeed(final FeedMessage message) {
		stage.hide();
		final Label label = getLatestFeedLabel(message.getName());

		final Scene scene = getNewScene();
		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.setPrefSize(600, 150);
		vbox.setStyle(ReaderApp.FX_BACKGROUND_COLOR_1D1D1D);

		final Separator separator2 = new Separator();
		separator2.setOrientation(Orientation.HORIZONTAL);

		Label label2 = getLatestFeedLabel(message.getTitle());
		label2.setOnMouseClicked(t -> util.openBrowser(message.getLink()));
		label2.setCursor(Cursor.HAND);

		vbox.getChildren().addAll(label, separator2, label2);
		((Group) scene.getRoot()).getChildren().addAll(vbox);
		scene.setOnMouseClicked(t -> hide(stage));

		makeSceneDraggable(scene);

		stage.sizeToScene();
		stage.setScene(scene);
		stage.show();
	}

	public Label getLatestFeedLabel(final String text) {
		final Label label = new Label(ReaderApp.EMPTY);
		label.setText(text);
		label.setStyle(ReaderApp.FX_TEXT_FILL_WHITE);
		label.setWrapText(true);
		label.setTextAlignment(TextAlignment.JUSTIFY);
		return label;
	}

	private void showSettingsPage() {
		stage.hide();
		final Scene scene = getNewScene();
		makeSceneDraggable(scene);
		stage.centerOnScreen();
		stage.setWidth(710);
		stage.setTitle(ReaderApp.CONFIGURE);
		stage.setResizable(true);

		final GridPane grid = new GridPane();
		grid.setPrefWidth(950);
		final HBox titleHeaderHBox = makeTitleHeader();
		final HBox rssFunctionsHBox = makeRSSFeedFunctions(grid);
		rssFunctionsHBox.setPadding(new Insets(5, 5, 5, 5));

		makeGridPane(grid);
		grid.setPadding(new Insets(0, 5, 5, 0));
		grid.setStyle(ReaderApp.FX_BACKGROUND_COLOR_1D1D1D2);

		final VBox vbox1 = new VBox();

		final ScrollPane scroll = new ScrollPane();
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scroll.setStyle(ReaderApp.FX_BACKGROUND_COLOR_1D1D1D2);
		scroll.setPadding(new Insets(5, 5, 5, 5));
		scroll.setContent(grid);
		scroll.autosize();

		final VBox vbox = new VBox();
		vbox.setSpacing(8);
		vbox.setPrefHeight(250);
		vbox.setPrefWidth(710);
		vbox.setStyle(ReaderApp.FX_BACKGROUND_COLOR_1D1D1D);
		vbox.setPadding(new Insets(5, 5, 5, 5));
		vbox.getChildren().addAll(titleHeaderHBox, scroll);

		vbox1.setPadding(new Insets(5, 5, 5, 5));
		vbox1.getChildren().addAll(titleHeaderHBox, rssFunctionsHBox, vbox);

		((Group) scene.getRoot()).getChildren().addAll(vbox1);

		stage.setScene(scene);
		stage.show();
	}

	public void sortCollection() {
		Collections.sort(result, (m1, m2) -> util.getDate(m2.getPubDate()).compareTo(util.getDate(m1.getPubDate())));
	}

	@Override
	public void start(final Stage stage) {
		this.stage = stage;
		Platform.setImplicitExit(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.getIcons().add(new Image(getClass().getResourceAsStream(ReaderApp.RSS_GIF)));

		javax.swing.SwingUtilities.invokeLater(() -> addAppToTray());
		stage.setOnCloseRequest(t -> hide(stage));
		showFeedList();
	}

	boolean validateRSS(final String url) {
		RSSFeedParser parser = null;
		try {
			parser = new RSSFeedParser(url);
		} catch (final Exception e) {
			return false;
		}
		return parser.readFeed() != null;
	}

	public void validateRssAndUpdate(final GridPane grid, final ActionEvent e) {
		final Node node = (Node) e.getSource();
		validateRssAndUpdateNode(grid, node, false);
	}

	public void validateRssAndUpdateNode(final GridPane grid, final Node node, final boolean save) {
		final TextField urlTextField = getRowTextField(grid, GridPane.getRowIndex(node), ReaderApp.URL2);
		final TextField nameTextField = getRowTextField(grid, GridPane.getRowIndex(node), ReaderApp.NAME2);

		final String urlText = urlTextField.getText();
		final String nameText = nameTextField.getText();

		if (!urlText.isEmpty() && validateRSS(urlText)) {
			((Button) node).setGraphic(new ImageView(new Image(getClass().getResourceAsStream(ReaderApp.OK_PNG))));
			if (save) {
				util.writeFile(nameText + ReaderApp.DELIMITER + urlText);
				urlCount = urlCount + 1;
			}
		} else {
			((Button) node).setGraphic(new ImageView(new Image(getClass().getResourceAsStream(ReaderApp.NOT_OK_PNG))));
			final Tooltip ttip = new Tooltip(urlText + ReaderApp.TOOLTIP_MESSAGE);
			urlTextField.setTooltip(ttip);
			((Button) node).setTooltip(ttip);
		}
	}

}
