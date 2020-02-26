package com.dms.nasaapi.db.marsRoverPhotos

import android.util.Log
import androidx.paging.DataSource
import com.dms.nasaapi.model.MarsPhoto
import java.util.concurrent.Executor

class MrpLocalCache(private val mrpDAO: MrpDAO,
                    private val ioExecutor: Executor) {

    /**
     * Insert a list of repos in the database, on a background thread.
     */
    fun insert(marsPhotos: List<MarsPhoto>, insertFinished: ()->Unit){
        ioExecutor.execute{
        Log.d("MrpLocalCache","inserting ${marsPhotos.size} repos")
            mrpDAO.insert(marsPhotos)
            insertFinished()
        }
    }
    /**
     * Request a LiveData<List<Repo>> from the Dao, based on a repo name. If the name contains
     * multiple words separated by spaces, then we're emulating the GitHub API behavior and allow
     * any characters between the words.
     * @param name repository name
     */
    fun getAllMarsPhoto():DataSource.Factory<Int,MarsPhoto>{
        // appending '%' so we can allow other characters to be before and after the query string
       // val query = "%${name.replace(' ', '%')}%" если более сложный запрос, заменяем пробелы процентами
// @Query("SELECT * FROM repos WHERE (name LIKE :queryString) OR (description LIKE " +
//            ":queryString) ORDER BY stars DESC, name ASC")
//    fun reposByName(queryString: String): DataSource.Factory<Int,Repo>
        return mrpDAO.getAllMarsPhotos()
    }


}