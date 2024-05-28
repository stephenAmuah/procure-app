package stephenaamuah.prmnt_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stephenaamuah.prmnt_application.model.Item;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}