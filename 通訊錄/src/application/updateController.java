package application;

import java.awt.Choice;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.mysql.cj.ParseInfo;
import com.mysql.cj.protocol.Resultset;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class updateController {

	@FXML
	private TextField MemberID;
	@FXML
	private TextField name;
	@FXML
	private ComboBox<String> type;
	@FXML
	private TextField phone;
	@FXML
	private Button cancelButton;
	@FXML
	private Button confirmButton;
	

	@FXML
	public void initialize() {
		try {
			type.getItems().addAll("cell", "company", "home");
			type.getSelectionModel().select("cell");
			type.getStyleClass().add("center-aligned");
			phone.setPromptText("09-XXXX-XXXX");
			MemberID.setText(Integer.toString(contactController.length));//給予最大MemberID+1
			setInfo();

			contactController.insertNewPerson = contactController.connection
					.prepareStatement("INSERT INTO people " + "(MemberID, name, type, phone) " + "VALUES (?, ?, ?, ?)");
			contactController.revisePerson = contactController.connection
					.prepareStatement("UPDATE people SET name = ?, type = ?, phone = ? WHERE MemberID = ?");					
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void cancel() {
		changeScene();
	}

	@FXML
	private void confirm() {
		if(!phone.getText().matches("[0]{1}[9]{1}[0-9]{8}") && type.getSelectionModel().getSelectedItem().toString().equals("cell")) {
			JOptionPane.showMessageDialog(null, "開頭為09，後面8碼");
		}else if(!phone.getText().matches("[0]{1}[0-9]{8}") && !phone.getText().matches("[0]{1}[0-9]{9}") &&
				type.getSelectionModel().getSelectedItem().toString().equals("home")) {
			JOptionPane.showMessageDialog(null, "開頭為0，後面8或9碼");
		}else if(!phone.getText().matches("[0]{1}[0-9]{8}") && !phone.getText().matches("[0]{1}[0-9]{9}") &&
				type.getSelectionModel().getSelectedItem().toString().equals("company")) {
			JOptionPane.showMessageDialog(null, "開頭為0，後面8或9碼");
		}else {
		if (contactController.choice == 2) {
			addPerson(Integer.parseInt(MemberID.getText()), name.getText(),
					type.getSelectionModel().getSelectedItem().toString(), phone.getText());
		} else if (contactController.choice == 0) {
			System.out.println(Integer.parseInt(MemberID.getText()));//printID測試
			revisePerson(name.getText(), type.getSelectionModel().getSelectedItem().toString(), phone.getText(),
					Integer.parseInt(MemberID.getText()));
		}
		changeScene();
		}
	}

	public int addPerson(Integer MemberID, String Name, String Type, String Phone) {
		int result = 0;
		// set parameters, then execute insertNewPerson
		try {
			contactController.insertNewPerson.setInt(1, MemberID);
			contactController.insertNewPerson.setString(2, Name);
			contactController.insertNewPerson.setString(3, Type);
			contactController.insertNewPerson.setString(4, Phone);

			// insert the new entry; returns # of rows updated
			result = contactController.insertNewPerson.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		return result;
	}

	private void revisePerson(String name, String type, String phone, Integer MemberID) {
		try {
			contactController.revisePerson.setInt(4, MemberID);
			contactController.revisePerson.setString(1, name);
			contactController.revisePerson.setString(2, type);
			contactController.revisePerson.setString(3, phone);
			int result = contactController.revisePerson.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void boxChange() {
		switch (type.getSelectionModel().getSelectedItem().toString()) {
		case "cell":
			phone.setPromptText("09-XXXX-XXXX");
			break;
		case "company":
			phone.setPromptText("0 + 8或9碼");
			break;
		case "home":
			phone.setPromptText("0 + 8或9碼");
			break;

		default:
			break;
		}
	}

	private void changeScene() {
		Parent pane = null;
		try {
			pane = FXMLLoader.load(getClass().getResource("Contacts.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		type.getScene().setRoot(pane);
	}

	private void setInfo() {
		if (contactController.choice == 0) {
			MemberID.setText(Integer.toString(contactController.rowData.getMemberID()));
			name.setText(contactController.rowData.getName());
			type.getSelectionModel().select((contactController.rowData.getType()));
			phone.setText(contactController.rowData.getPhone());
		}
	}
}
