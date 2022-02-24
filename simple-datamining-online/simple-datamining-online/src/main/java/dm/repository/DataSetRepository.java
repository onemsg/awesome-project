package dm.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import dm.entity.DataLocation;

/**
 * DataSetRepository
 */
@Transactional()
@org.springframework.stereotype.Repository
public interface DataSetRepository extends Repository<DataLocation, Long> {

    @Query("SELECT * FROM dataset data WHERE id=:id")
    public DataLocation findById(@Param("id") Long id);

    @Query("SELECT * FROM dataset data WHERE name=:name")
    public DataLocation findByName(@Param("name") String name);

    @Query("SELECT path FROM dataset WHERE name=:name")
    public String findPathByName(@Param("name") String name);

    @Query("SELECT * FROM dataset data")
    public List<DataLocation> findAll();

    @Query("SELECT name FROM dataset")
    public List<String> findAllName();

    @Modifying
    @Query("INSERT INTO dataset(name, path) VALUES (:name, :path)")
    public int save(String name, String path);

}