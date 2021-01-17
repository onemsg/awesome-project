package dm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import dm.model.DataLocation;

/**
 * DataSetRepository
 */
public interface DataSetRepository extends Repository<DataLocation, Long> {

    @Query("SELECT data FROM DataLocation data WHERE id=:id")
    @Transactional(readOnly = true)
    public DataLocation findById(@Param("id") Long id);

    @Query("SELECT data FROM DataLocation data WHERE name=:name")
    @Transactional(readOnly = true)
    public DataLocation findByName(@Param("name") String name);

    @Query("SELECT path FROM DataLocation WHERE name=:name")
    @Transactional(readOnly = true)
    public String findPathByName(@Param("name") String name);

    @Query("SELECT data FROM DataLocation data")
    @Transactional(readOnly = true)
    public List<DataLocation> findAll();

    @Query("SELECT name FROM DataLocation")
    @Transactional(readOnly = true)
    public List<String> findAllName();
}