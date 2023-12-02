package ke.co.shofcosacco.app.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ke.co.shofcosacco.app.database.dao.ProfilePictureDao;
import ke.co.shofcosacco.app.database.models.ProfilePicture;

@Database(entities = {ProfilePicture.class}, version = 1, exportSchema = false)
public  abstract class AppDatabase extends RoomDatabase {

    public abstract ProfilePictureDao profileDao();

}