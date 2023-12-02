package ke.co.shofcosacco.app.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "profile_pictures")
public class ProfilePicture {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String phone;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] image;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}
