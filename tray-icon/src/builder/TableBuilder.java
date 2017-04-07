package builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableBuilder<M> extends TableView<M> {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> TableView<T> buildUpon(final Class<T> model) {
		final TableView<T> table = new TableView<>();
		final ArrayList<TableColumn<T, ?>> columns = new ArrayList<>();
		final Set<String> methodNames = TableBuilder.getMethodNames(model.getDeclaredMethods());

		for (final Field field : model.getDeclaredFields()) {
			final String fieldName = field.getName();
			final String getterName = "get" + TableBuilder.capitalizeFirst(fieldName);
			final String setterName = "set" + TableBuilder.capitalizeFirst(fieldName);
			final boolean notAnnotated = field.getAnnotation(IgnoreTable.class) == null;

			if (notAnnotated && methodNames.contains(getterName) && methodNames.contains(setterName)) {
				final TableColumn column = new TableColumn(TableBuilder.camelCaseToTitleCase(fieldName));
				if (fieldName.equalsIgnoreCase("name")) {
					column.setMaxWidth(140);
				}
				if (fieldName.equalsIgnoreCase("title")) {
					column.setMaxWidth(530);
				}
				if (fieldName.equalsIgnoreCase("pubDate")) {
					column.setMaxWidth(200);
				}
				column.setSortable(false);
				column.setCellValueFactory(TableBuilder.getCellFactory(model, field.getType(), fieldName));
				columns.add(column);
			}
		}
		table.getColumns().addAll(columns);
		return table;
	}

	private static String camelCaseToTitleCase(final String camelCaseString) {
		String titleCaseString = "";
		if ((camelCaseString != null) && (camelCaseString.length() > 0)) {
			titleCaseString = TableBuilder.splitCamelCase(TableBuilder.capitalizeFirst(camelCaseString));
		}
		return titleCaseString;
	}

	private static String capitalizeFirst(final String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	private static <S, T> PropertyValueFactory<S, T> getCellFactory(final Class<S> model, final Class<T> fieldType,
			final String fieldName) {
		return new PropertyValueFactory<>(fieldName);
	}

	private static Set<String> getMethodNames(final Method[] methods) {
		final Set<String> membersNames = new TreeSet<>();
		for (final Method member : methods) {
			membersNames.add(member.getName());
		}
		return membersNames;
	}

	private static String splitCamelCase(final String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

	private TableBuilder() {
	}

}