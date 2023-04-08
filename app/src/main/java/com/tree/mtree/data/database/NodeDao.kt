package com.example.treestructure.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tree.mtree.data.database.model.NodeDbModel

@Dao
interface NodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNode(nodeDbModel: NodeDbModel)

    @Query("DELETE FROM nodes WHERE parentId=:nodeDbModel.parentId AND id=:nodeDbModel.id")
    suspend fun deleteNode(nodeDbModel: NodeDbModel)

    @Query("SELECT * FROM nodes WHERE parentId=:parentId AND id=:id LIMIT 1")
    suspend fun getNode(parentId: Int, id: Int): NodeDbModel

    @Query("SELECT * FROM nodes WHERE parentId=:parentId")
    suspend fun getNodes(parentId: Int): List<NodeDbModel>
}