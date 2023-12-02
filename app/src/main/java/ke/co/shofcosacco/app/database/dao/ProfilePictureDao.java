package ke.co.shofcosacco.app.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ke.co.shofcosacco.app.database.models.ProfilePicture;

@Dao
public interface ProfilePictureDao {
    @Insert
    void insert(ProfilePicture profilePicture);

    @Update
    void update(ProfilePicture profilePicture);
    @Query("SELECT * FROM profile_pictures where phone=:phone ORDER BY id DESC limit 1")
    ProfilePicture getProfilePic(String phone);

}
