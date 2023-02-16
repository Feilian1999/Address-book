package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class contactController {
	private static final String URL = "jdbc:mysql://127.0.0.1/member?serverTimezone=GMT";
	private static final String USERNAME = "java";
	private static final String PASSWORD = "java";
	private PreparedStatement selectAllPeople;
	private PreparedStatement selectPartPeople;
	private PreparedStatement deleteOldPerson;
	private PreparedStatement lastPerson;
	public static PreparedStatement revisePerson;
	public static PreparedStatement insertNewPerson;
	public static Connection connection; // manages connection

	public static int length;
	ObservableList<People> data;

	public static List<People> peopleList;
	private ResultSet resultSet;
	private String[] options = { "�����", "�R��" };// JOptionPane.showOptionDialog
	public static int choice;
	public static People rowData;
	public String searchWord;

	@FXML
	private ImageView addIcon;
	@FXML
	private TableView<People> tableView;
	@FXML
	private TableColumn<People, String> nameColumn;
	@FXML
	private Button searchButton;
	@FXML
	private TextField textField;

	@FXML
	private void initialize() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);// MYSQL������
			selectAllPeople = connection.prepareStatement("SELECT * FROM people");
			selectPartPeople = connection.prepareStatement("SELECT * FROM people WHERE name LIKE ? OR phone LIKE ?");
			deleteOldPerson = connection.prepareStatement("DELETE FROM people WHERE MemberID = ?");
			lastPerson = connection.prepareStatement("SELECT MAX(MemberID) AS LatestPerson FROM people");
			resultSet = lastPerson.executeQuery();
			if (resultSet.first()) {
				length = resultSet.getInt("LatestPerson") + 1;// �N�ثe�̫�@��ID�ǥX��
				System.out.println(length);
			}
			resultSet = selectAllPeople.executeQuery();
			peopleList = new ArrayList<People>();

			while (resultSet.next()) {
				peopleList.add(new People(resultSet.getInt("MemberID"), resultSet.getString("Name"),
						resultSet.getString("Type"), resultSet.getString("Phone")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				resultSet.close();
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();

			}
		}

		data = FXCollections.observableArrayList(peopleList);

		nameColumn.setCellValueFactory(new PropertyValueFactory("Name"));// ��ܸ��
		tableView.setItems(data);

		doubleClicked();
	}

	@FXML
	private void newPerson() {
		choice = 2;
		changeScene(choice);
	}

	@FXML
	private void searchPeople() {
		try {
			searchWord = textField.getText();
			selectPartPeople.setString(1, "%" + searchWord + "%");
			selectPartPeople.setString(2, "%" + searchWord + "%");
			resultSet = selectPartPeople.executeQuery();

			if (!resultSet.next()) {
				tableView.getItems().clear();
			} else {
				resultSet.beforeFirst();
				refreshData();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doubleClicked() {
		tableView.setRowFactory(tv -> {
			TableRow<People> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					People rowData = row.getItem();
					this.rowData = rowData;// �N��ưO���U�ӡA�H�K�b�ĤG�ӵe���ϥ�
					choice = JOptionPane.showOptionDialog(null, "�q�ܸ��X: " + rowData.getPhone(), "�p���H��T", // �ǥ�choice�P�_�����e����O�_�ݭn�N��ƶ�W
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
					changeScene(rowData.getMemberID());
				}
			});
			return row;
		});
	}

	private void changeScene(int id) {
		if (choice == 0) {// ��אּ�ק����
			Parent pane = null;
			try {
				pane = FXMLLoader.load(getClass().getResource("Update.fxml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addIcon.getScene().setRoot(pane);
		} else if (choice == 1) {// ��U���p���H�R��
			deletePerson(id);
		} else if (choice == 2) {

			Parent pane = null;
			try {
				pane = FXMLLoader.load(getClass().getResource("Update.fxml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addIcon.getScene().setRoot(pane);
		}
	}

	private void deletePerson(int id) {// �ٮtMemberID�V�e����!
		try {
			deleteOldPerson.setInt(1, id);
			deleteOldPerson.executeUpdate();
//			deleteOldPerson.close();
			resultSet = selectAllPeople.executeQuery();// refreshData���e�m
			refreshData();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void refreshData() {
		try {
			peopleList.clear();
			while (resultSet.next()) {
				peopleList.add(new People(resultSet.getInt("MemberID"), resultSet.getString("Name"),
						resultSet.getString("Type"), resultSet.getString("Phone")));

				tableView.getItems().clear();
				tableView.refresh();
				tableView.getItems().addAll(peopleList);
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
	}

}
