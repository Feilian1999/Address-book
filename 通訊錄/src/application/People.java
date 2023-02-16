package application;

import java.beans.EventHandler;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableCell;

public class People{
	private final SimpleIntegerProperty MemberID;
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;
    private final SimpleStringProperty phone;

    People(int MemberID, String Name, String Type, String Phone) {
        this.MemberID = new SimpleIntegerProperty(MemberID);
        this.name = new SimpleStringProperty(Name);
        this.type = new SimpleStringProperty(Type);
        this.phone = new SimpleStringProperty(Phone);
        
        
    }

    public String getName() {
        return name.get();
    }

    public void setName(String Name) {
        name.set(Name);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String Type) {
        type.set(Type);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setEmail(String Phone) {
        phone.set(Phone);
    }

	public int getMemberID() {
		return MemberID.get();
	}
	public void setMemberID(int MemberID) {
		this.MemberID.set(MemberID);
	}
}
